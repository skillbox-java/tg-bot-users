package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.UpdateUtils;
import org.codewithoutus.tgbotusers.bot.enums.BotCommand;
import org.codewithoutus.tgbotusers.bot.keyboard.CongratulationDecisionKeyboard;
import org.codewithoutus.tgbotusers.bot.keyboard.KeyboardUtils;
import org.codewithoutus.tgbotusers.bot.service.NotificationService;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.bot.service.TemplateEngine;
import org.codewithoutus.tgbotusers.config.ChatSettings;
import org.codewithoutus.tgbotusers.config.NotificationTemplates;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.service.ChatModeratorService;
import org.codewithoutus.tgbotusers.model.service.ChatUserService;
import org.codewithoutus.tgbotusers.model.service.UserJoiningService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class LuckyCommandHandler extends Handler {

    private final ChatSettings chatSettings;
    private final NotificationTemplates notificationTemplates;
    private final NotificationService notificationService;
    private final TelegramService telegramService;
    private final TemplateEngine templateEngine;

    private final ChatModeratorService chatModeratorService;
    private final ChatUserService chatUserService;
    private final UserJoiningService userJoiningService;

    @Override
    protected boolean handle(Update update) {
        BotCommand command = UpdateUtils.getBotCommand(update);
        if (command != BotCommand.LUCKY_LIST && command != BotCommand.CHOOSE_LUCKY) {
            return false;
        }

        Long moderatorChatId = UpdateUtils.getChatId(update);
        if (!UpdateUtils.isPrivateMessageFromAdmin(update, chatSettings)
                && !chatModeratorService.existsByChatId(moderatorChatId)) {
            return false;
        }

        String commandText = UpdateUtils.getMessageText(update);
        Matcher matcher = command.getRegex().matcher(commandText);
        if (!matcher.matches()) {
            log.warn("Wrong command to bot {}", commandText);
            return false;
        }

        switch (command) {
            case LUCKY_LIST -> handleLuckyList(matcher, moderatorChatId);
            case CHOOSE_LUCKY -> handleChooseLucky(matcher, moderatorChatId);
        }
        return true;
    }

    private void handleChooseLucky(Matcher matcher, Long moderatorChatId) {
        String chatId = matcher.group(2);
        List<Long> luckyChatsIds = getLuckyChats(chatId, moderatorChatId)
                .stream()
                .map(ChatUser::getChatId)
                .toList();

        List<UserJoining> luckyOnes = userJoiningService.findNotCongratulatedByChatIds(luckyChatsIds);
        if (luckyOnes.isEmpty()) {
            telegramService.sendMessage(new SendMessage(moderatorChatId, "Пока ещё нет счастливчиков"));
            return;
        }

        for (UserJoining userJoining : luckyOnes) {
            InlineKeyboardMarkup keyboard = KeyboardUtils
                    .createKeyboard(CongratulationDecisionKeyboard.class, String.valueOf(userJoining.getId()));

            String notificationText = templateEngine
                    .buildFromTemplate(notificationTemplates.getJoinUserInfo(), userJoining, false);

            notificationService.sendModeratorNotification(moderatorChatId, userJoining, notificationText, keyboard);
        }
    }

    private void handleLuckyList(Matcher matcher, Long moderatorChatId) {
        String chatId = matcher.group(2);
        List<ChatUser> luckyChats = getLuckyChats(chatId, moderatorChatId);
        if (luckyChats.isEmpty()) {
            telegramService.sendMessage(new SendMessage(moderatorChatId, "Нет данных о чатах пользователей"));
            return;
        }

        telegramService.sendMessage(new SendMessage(moderatorChatId, createLuckyListText(luckyChats))
                .parseMode(ParseMode.Markdown));
    }

    private List<ChatUser> getLuckyChats(String chatId, Long moderatorChatId) {
        if (chatId == null) {
            return chatModeratorService
                    .findByChatId(moderatorChatId)
                    .map(ChatModerator::getChatUsers)
                    .orElseGet(ArrayList::new);
        } else {
            return chatUserService
                    .findByChatId(Long.parseLong(chatId))
                    .stream()
                    .toList();
        }
    }

    private String createLuckyListText(List<ChatUser> luckyChats) {
        List<Long> chatsIds = luckyChats.stream()
                .map(ChatUser::getChatId)
                .toList();

        StringBuilder resultBuilder = new StringBuilder();
        String template = notificationTemplates.getJoinUserInfo();
        userJoiningService.findByChatIds(chatsIds)
                .stream()
                .collect(Collectors.groupingBy(x -> String.valueOf(x.getChatId())))
                .forEach((groupId, joiningList) -> {
                    resultBuilder
                            .append("==================================================").append(System.lineSeparator())
                            .append("Группа: *")
                            .append(telegramService.getChat(Long.parseLong(groupId)).title())
                            .append("*")
                            .append(System.lineSeparator())
                            .append("==================================================").append(System.lineSeparator());
                    joiningList.forEach(joining -> {
                        resultBuilder
                                .append(templateEngine.buildFromTemplate(template, joining, true))
                                .append(System.lineSeparator())
                                .append(System.lineSeparator());
                    });
                    resultBuilder.append(System.lineSeparator());
                });

        return resultBuilder.isEmpty() ? "Пока ещё нет счастливчиков" : resultBuilder.toString();
    }
}