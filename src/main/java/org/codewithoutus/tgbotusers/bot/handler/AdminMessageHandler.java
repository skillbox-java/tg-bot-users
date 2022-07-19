package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.UpdateUtils;
import org.codewithoutus.tgbotusers.bot.enums.BotCommand;
import org.codewithoutus.tgbotusers.bot.keyboard.AdminKeyboard;
import org.codewithoutus.tgbotusers.bot.keyboard.KeyboardUtils;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
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
    private final ChatModeratorService chatModeratorService;
    private final ChatUserService chatUserService;

    //список команд
    //todo надо потом сделать привествие бота,что бы он сразу присал что если что есть help
//    private static final String HELP = "/help";
//    private static final String ADD_MODER_CHAT = "/addModerChat";
//    private static final String ADD_USER_CHAT = "/addUserChat";
//    private static final String BIND_USER_CHAT_TO_MODER = "/bindUserChatToModer";
//    private static final String UNBIND_USER_CHAT_TO_MODER = "/unbindUserChatToModer";

    private static final String OK_BIND = "Cool, user chat № %s , is add in moder chat № %s";
    private static final String NOT_BIND = "Oh, user chat № %s , is already in moder chat № %s";
    private static final String NOT_UNBIND = "Oh, user chat № %s , is not bind in moder chat № %s";
    private static final String OK_UNBIND = "Cool, user chat № %s , is unbind from moder chat № %s";
    private static final String OK_MODER = "Cool, moder chat № %s , is add in DB";
    private static final String OK_USER = "Cool, user chat № %s , is add in DB";
    private static final String NOT_MODER = "Oh, moder chat  № %s ,is already in the database";
    private static final String NOT_USER = "Oh, user chat  № %s ,is already in the database";
    private static final String ERROR = "Sorry, \" %s\" ,is invalid format ID";
    private static final String UNKNOW_COMMAND = "Sorry, bot does not know this  \"%s\" command";
    //todo вынести команды бота в бот сервис/в бот команд
//    List<String> listCommandAdd = Arrays.asList(HELP, ADD_MODER_CHAT, ADD_USER_CHAT, BIND_USER_CHAT_TO_MODER);
//    List<String> listCommandDelete = Arrays.asList(ADD_MODER_CHAT, ADD_USER_CHAT, BIND_USER_CHAT_TO_MODER);

    @Override
    protected boolean handle(Update update) {
        if (!UpdateUtils.isPrivateMessageFromAdmin(update)) {
            return false;
        }

        String text = UpdateUtils.getMessageText(update);
        Long chatId = UpdateUtils.getChat(update).id();

        // TODO: Макс -- вначале строковыми командами, а потом можно попробовать кнопки
        // TODO сохраняем в бд группы с + а нужно с -
        //мой id=161855902  /11725

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

        long moderatorGroupId = Long.parseLong(matcher.group(2));
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
