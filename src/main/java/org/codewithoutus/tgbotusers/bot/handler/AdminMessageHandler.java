package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.UpdateUtils;
import org.codewithoutus.tgbotusers.bot.enums.BotCommand;
import org.codewithoutus.tgbotusers.bot.keyboard.AdminKeyboard;
import org.codewithoutus.tgbotusers.bot.keyboard.KeyboardUtils;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.config.ChatSettings;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.codewithoutus.tgbotusers.model.service.ChatModeratorService;
import org.codewithoutus.tgbotusers.model.service.ChatUserService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdminMessageHandler extends Handler {

    private final TelegramService telegramService;
    private final ChatSettings chatSettings;

    private final ChatModeratorService chatModeratorService;
    private final ChatUserService chatUserService;

    private static final String OK_BIND = "Cool, user chat № %s , is add in moder chat № %s";
    private static final String NOT_BIND = "Oh, user chat № %s , is already in moder chat № %s";
    private static final String NOT_MODER = "Oh, moder chat  № %s ,is already in the database";
    private static final String NOT_USER = "Oh, user chat  № %s ,is already in the database";

    @Override
    protected boolean handle(Update update) {
        User user = UpdateUtils.getUser(update);
        if (user == null || !chatSettings.isAdminId(user.id()) || !UpdateUtils.isPrivateMessage(update)) {
            return false;
        }

        String text = UpdateUtils.getMessageText(update);
        Long chatId = UpdateUtils.getChat(update).id();

        AdminKeyboard command = KeyboardUtils.defineKey(AdminKeyboard.class, text).orElse(null);
        if (command == null) {
            return false;
        }
        return switch (command) {
            case HELP -> showHelp(chatId);
            case ADD_MODER_CHAT -> addModerChat(update, chatId, text);
            case ADD_USER_CHAT -> addUserChat(update, chatId, text);
            case BIND_USER_CHAT_TO_MODER -> bindUserChatToModer(update, chatId, text);
            default -> false;
        };
    }

    private boolean showHelp(Long chatId) {
        String text = "Список доступных команд: " + System.lineSeparator();
        text += Arrays.stream(AdminKeyboard.values())
                .map(command -> command.getBotCommand().getHelp())
                .collect(Collectors.joining(System.lineSeparator()));
        telegramService.sendMessage(new SendMessage(chatId, text));
        return true;
    }

    private boolean addModerChat(Update update, Long chatId, String text) {
        Matcher matcher = BotCommand.ADD_MODER_CHAT.getRegex().matcher(text);
        if (!matcher.matches()) {
            return false;
        }

        long moderatorGroupId = Long.parseLong(matcher.group(1));
        ChatModerator chatModerator = chatModeratorService.findByChatId(moderatorGroupId).orElse(null);
        if (chatModerator != null) {
            telegramService.sendMessage(new SendMessage(chatId, "Такой модератор уже есть"));
            return true;
        }

        ChatModerator newEntity = new ChatModerator();
        newEntity.setChatId(moderatorGroupId);
        chatModeratorService.save(newEntity);
        return true;
    }

    private boolean addUserChat(Update update, Long chatId, String text) {
        Matcher matcher = BotCommand.ADD_USER_CHAT.getRegex().matcher(text);
        if (!matcher.matches()) {
            return false;
        }

        long userGroupId = Long.parseLong(matcher.group(1));
        ChatUser chatUser = chatUserService.findByChatId(userGroupId).orElse(null);
        if (chatUser != null) {
            telegramService.sendMessage(new SendMessage(chatId, "Такая группа уже есть"));
            return true;
        }

        ChatUser newEntity = new ChatUser();
        newEntity.setChatId(userGroupId);
        chatUserService.save(newEntity);
        return true;
    }

    private boolean bindUserChatToModer(Update update, Long chatId, String text) {
        Matcher matcher = BotCommand.BIND_USER_CHAT_TO_MODER.getRegex().matcher(text);
        if (!matcher.matches()) {
            return false;
        }

        long userGroupId = Long.parseLong(matcher.group(1));
        ChatUser chatUser = chatUserService.findByChatId(userGroupId).orElse(null);
        if (chatUser == null) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(NOT_USER, userGroupId)));
            return true;
        }

        long moderatorGroupId = Long.parseLong(matcher.group(2));
        ChatModerator chatModerator = chatModeratorService.findByChatId(moderatorGroupId).orElse(null);
        if (chatModerator == null) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(NOT_MODER, moderatorGroupId)));
            return true;
        }

        if (chatModerator.getChatUsers().contains(chatUser)) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(NOT_BIND, userGroupId, moderatorGroupId)));
            return true;
        }

        chatModerator.getChatUsers().add(chatUser);
        chatModeratorService.save(chatModerator);
        telegramService.sendMessage(new SendMessage(chatId, String.format(OK_BIND, userGroupId, moderatorGroupId)));
        return true;
    }

    private boolean deleteUserChat(Update update, Long chatId, String text) {
        Matcher matcher = BotCommand.DELETE_USER_CHAT.getRegex().matcher(text);
        if (!matcher.matches()) {
            return false;
        }

        long userGroupId = Long.parseLong(matcher.group(1));
        ChatUser chatUser = chatUserService.findByChatId(userGroupId).orElse(null);
        if (chatUser == null) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(NOT_MODER, userGroupId)));
            return true;
        }

        if (!chatUser.getChatModerators().isEmpty()) {
            telegramService.sendMessage(new SendMessage(chatId, "Есть связные сущности у группы " + userGroupId));
            return true;
        }

        chatUserService.deleteById(chatUser.getId());
        telegramService.sendMessage(new SendMessage(chatId, "Удалили группу юзеров № " + userGroupId));
        return true;
    }

    private boolean deleteModerChat() {

        return false;
    }

    private boolean deleteUserChatFromModerChat() {

        return false;
    }
}
