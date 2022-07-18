package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.UpdateUtils;
import org.codewithoutus.tgbotusers.bot.enums.BotCommand;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.bot.service.TemplateEngine;
import org.codewithoutus.tgbotusers.config.NotificationTemplates;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.entity.ChatUser;
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
public class LuckyListCommandHandler extends Handler {

    private final ChatModeratorService chatModeratorService;
    private final ChatUserService chatUserService;
    private final UserJoiningService userJoiningService;
    private final TelegramService telegramService;
    private final NotificationTemplates notificationTemplates;
    private final TemplateEngine templateEngine;

    @Override
    protected boolean handle(Update update) {
        // TODO: Алекс -- реализовать вывод списка счастивчиков "/списокЮбилейный" или "/luckyList"

        // TODO: Алекс -- если на anniversary number не было поздравления, то выводятся все претенденты с кнопками CongratulationDecisionKeyboard
        // TODO: Алекс -- если поздравленного нет, то отклоненные тоже выводятся
        // TODO: Алекс -- все новые кнопки нужно записывать в таблицу UserJoiningNotification, чтобы потом также удалялись
        // TODO: Алекс -- можно попробовать выводить все-все чаты, а потом доработать -- на будущее

        BotCommand command = UpdateUtils.getBotCommand(update);
        if (command != BotCommand.LUCKY_LIST) {
            return false;
        }

        Long moderatorChatId = UpdateUtils.getChat(update).id();
        if (!UpdateUtils.isPrivateMessageFromAdmin(update) && !chatModeratorService.existsByChatId(moderatorChatId)) {
            return false;
        }

        String commandText = UpdateUtils.getMessageText(update);
        Matcher matcher = command.getRegex().matcher(commandText);
        if (!matcher.matches()) {
            log.warn("Wrong command to bot {}", commandText);
            return false;
        }

        String chatId = matcher.group(2);
        List<ChatUser> luckyChats = getLuckyChats(chatId, moderatorChatId);
        if (luckyChats.isEmpty()) {
            telegramService.sendMessage(new SendMessage(moderatorChatId, "Нет данных о чатах пользователей"));
            return true;
        }

        telegramService.sendMessage(new SendMessage(moderatorChatId, createLuckyListText(luckyChats)));
        return true;
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
                            .append("Группа: ").append(groupId).append(System.lineSeparator());
                    joiningList.forEach(joining -> {
                        resultBuilder
                                .append(templateEngine.buildFromTemplate(template, joining, true))
                                .append(System.lineSeparator());
                    });
                    resultBuilder.append(System.lineSeparator());
                });

        return resultBuilder.isEmpty() ? "Пока ещё нет счастливчиков" : resultBuilder.toString();
    }
}