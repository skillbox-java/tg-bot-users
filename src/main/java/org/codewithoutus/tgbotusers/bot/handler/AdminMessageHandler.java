package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
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
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

@Component
@RequiredArgsConstructor
public class AdminMessageHandler extends Handler {
    
    private static final String OK_ADD_MODER = "OK add moder chat № %s ";
    private static final String OK_ADD_USER = "OK add user chat № %s";
    private static final String OK_BIND = "OK bind user chat № %s and moder chat № %s ";
    private static final String OK_UNBIND = "OK unbind user chat № %s and moder chat № %s ";
    private static final String OK_DEL_MODER = "OK delete moder chat № %s ";
    private static final String OK_DEL_USER = "OK delete user chat № %s ";
    
    private static final String MODER_EXISTS = "Moder chat with id = %s exists";
    private static final String USER_EXISTS = "User chat with id = %s exists";
    private static final String MODER_NOT_FOUND = "Moder chat with id = %s not found";
    private static final String USER_NOT_FOUND = "User chat with id = %s not found";
    
    private static final String FAIL_ADD_MODER = "FAIL add moder chat  № %s ";
    private static final String FAIL_ADD_USER = "FAIL add user chat  № %s ";
    private static final String ALREADY_BIND = "ALREADY bind user chat № %s and moder chat № %s ";
    private static final String ALREADY_UNBIND = "ALREADY unbind user chat № %s and moder chat № %s ";
    private static final String FAIL_DEL_MODER = "FAIL delete moder chat № %s ";
    private static final String FAIL_DEL_USER = "FAIL delete user chat № %s ";
    
    
    private final TelegramService telegramService;
    private final ChatSettings chatSettings;
    
    private final ChatModeratorService chatModeratorService;
    private final ChatUserService chatUserService;
    
    
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
            case CURRENT_SETTINGS -> showCurrentSettings(chatId);
            case ADD_MODER_CHAT -> addModerChat(chatId, text);
            case ADD_USER_CHAT -> addUserChat(chatId, text);
            case BIND_USER_CHAT_TO_MODER -> bindUserChatToModer(chatId, text);
            case UNBIND_USER_CHAT_FROM_MODER -> unbindUserChatFromModer(chatId, text);
            case DELETE_USER_CHAT -> deleteUserChat(chatId, text);
            case DELETE_MODER_CHAT -> deleteModerChat(chatId, text);
            default -> false;
        };
    }
    
    private boolean showHelp(Long chatId) {
        StringBuilder builder = new StringBuilder("Список доступных команд: ")
                .append(System.lineSeparator());
        Arrays.stream(AdminKeyboard.values())
                .forEach(command -> {
                    String text = command.getBotCommand().getText();
                    String params = command.getBotCommand().getParams();
                    builder.append("<code>").append(text).append("</code>").append(params)
                            .append(System.lineSeparator());
                });
        telegramService.sendMessage(new SendMessage(chatId, builder.toString()).parseMode(ParseMode.HTML));
        return true;
    }
    
    private boolean showCurrentSettings(Long chatId) {
        StringBuilder settings = new StringBuilder("Текущие настройки групп:")
                .append(System.lineSeparator());
        
        List<ChatModerator> chatModerators = chatModeratorService.findAll();
        List<ChatUser> chatUsers = chatUserService.findAll();
        
        chatModerators.forEach(moderChat -> {
            settings.append("    ").append(moderChat.getChatId()).append(':').append(System.lineSeparator());
            moderChat.getChatUsers().forEach(userChat -> {
                settings.append("            ").append(userChat.getChatId()).append(System.lineSeparator());
                chatUsers.remove(userChat);
            });
        });
        
        if (!chatUsers.isEmpty()) {
            settings.append("Не связанные группы:").append(System.lineSeparator());
            chatUsers.forEach(chatUser -> {
                settings.append("            ").append(chatUser.getChatId()).append(System.lineSeparator());
            });
        }
        
        telegramService.sendMessage(new SendMessage(chatId, settings.toString()));
        return true;
    }
    
    
    private boolean addModerChat(Long chatId, String text) {
        Matcher matcher = BotCommand.ADD_MODER_CHAT.getRegex().matcher(text);
        if (!matcher.matches()) {
            return false;
        }
        
        long moderatorGroupId = Long.parseLong(matcher.group(1));
        Optional<ChatModerator> chatModeratorOptional = chatModeratorService.findByChatId(moderatorGroupId);
        if (chatModeratorOptional.isPresent()) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(MODER_EXISTS, moderatorGroupId)));
            return true;
        }
    
        Optional<ChatUser> chatUserOptional = chatUserService.findByChatId(moderatorGroupId);
        if (chatUserOptional.isPresent()) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(USER_EXISTS, moderatorGroupId)));
            return true;
        }
        
        ChatModerator newEntity = new ChatModerator();
        newEntity.setChatId(moderatorGroupId);
        chatModeratorService.save(newEntity);
        telegramService.sendMessage(new SendMessage(chatId, String.format(OK_ADD_MODER, moderatorGroupId)));
        return true;
    }
    
    private boolean addUserChat(Long chatId, String text) {
        Matcher matcher = BotCommand.ADD_USER_CHAT.getRegex().matcher(text);
        if (!matcher.matches()) {
            return false;
        }
        
        long userGroupId = Long.parseLong(matcher.group(1));
        Optional<ChatUser> chatUserOptional = chatUserService.findByChatId(userGroupId);
        if (chatUserOptional.isPresent()) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(USER_EXISTS, userGroupId)));
            return true;
        }
    
        Optional<ChatModerator> chatModeratorOptional = chatModeratorService.findByChatId(userGroupId);
        if (chatModeratorOptional.isPresent()) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(MODER_EXISTS, userGroupId)));
            return true;
        }
        
        ChatUser newEntity = new ChatUser();
        newEntity.setChatId(userGroupId);
        chatUserService.save(newEntity);
        telegramService.sendMessage(new SendMessage(chatId, String.format(OK_ADD_USER, userGroupId)));
        return true;
    }
    
    private boolean bindUserChatToModer(Long chatId, String text) {
        Matcher matcher = BotCommand.BIND_USER_CHAT_TO_MODER.getRegex().matcher(text);
        if (!matcher.matches()) {
            return false;
        }
        
        long userGroupId = Long.parseLong(matcher.group(1));
        ChatUser chatUser = chatUserService.findByChatId(userGroupId).orElse(null);
        if (chatUser == null) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(USER_NOT_FOUND, userGroupId)));
            return true;
        }
        
        long moderatorGroupId = Long.parseLong(matcher.group(2));
        ChatModerator chatModerator = chatModeratorService.findByChatId(moderatorGroupId).orElse(null);
        if (chatModerator == null) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(MODER_NOT_FOUND, moderatorGroupId)));
            return true;
        }
        
        if (chatModerator.getChatUsers().contains(chatUser)) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(ALREADY_BIND, userGroupId, moderatorGroupId)));
            return true;
        }
        
        chatModerator.getChatUsers().add(chatUser);
        chatModeratorService.save(chatModerator);
        telegramService.sendMessage(new SendMessage(chatId, String.format(OK_BIND, userGroupId, moderatorGroupId)));
        return true;
    }
    
    
    private boolean unbindUserChatFromModer(Long chatId, String text) {
        Matcher matcher = BotCommand.UNBIND_USER_CHAT_FROM_MODER.getRegex().matcher(text);
        if (!matcher.matches()) {
            return false;
        }
        
        long userGroupId = Long.parseLong(matcher.group(1));
        ChatUser chatUser = chatUserService.findByChatId(userGroupId).orElse(null);
        if (chatUser == null) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(USER_NOT_FOUND, userGroupId)));
            return true;
        }
        
        long moderatorGroupId = Long.parseLong(matcher.group(2));
        ChatModerator chatModerator = chatModeratorService.findByChatId(moderatorGroupId).orElse(null);
        if (chatModerator == null) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(MODER_NOT_FOUND, moderatorGroupId)));
            return true;
        }
        
        if (!chatModerator.getChatUsers().contains(chatUser)) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(ALREADY_UNBIND, userGroupId, moderatorGroupId)));
            return true;
        }
        
        chatModerator.getChatUsers().remove(chatUser);
        chatModeratorService.save(chatModerator);
        telegramService.sendMessage(new SendMessage(chatId, String.format(OK_UNBIND, userGroupId, moderatorGroupId)));
        return true;
    }
    
    private boolean deleteUserChat(Long chatId, String text) {
        Matcher matcher = BotCommand.DELETE_USER_CHAT.getRegex().matcher(text);
        if (!matcher.matches()) {
            return false;
        }
        
        long userGroupId = Long.parseLong(matcher.group(1));
        ChatUser chatUser = chatUserService.findByChatId(userGroupId).orElse(null);
        if (chatUser == null) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(USER_NOT_FOUND, userGroupId)));
            return true;
        }
        
        if (!chatUser.getChatModerators().isEmpty()) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(FAIL_DEL_USER, userGroupId)));
            return true;
        }
        
        chatUserService.deleteById(chatUser.getId());
        telegramService.sendMessage(new SendMessage(chatId, String.format(OK_DEL_USER, userGroupId)));
        return true;
    }
    
    private boolean deleteModerChat(Long chatId, String text) {
        Matcher matcher = BotCommand.DELETE_MODER_CHAT.getRegex().matcher(text);
        if (!matcher.matches()) {
            return false;
        }
        
        long moderGroupId = Long.parseLong(matcher.group(1));
        ChatModerator chatModerator = chatModeratorService.findByChatId(moderGroupId).orElse(null);
        if (chatModerator == null) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(USER_NOT_FOUND, moderGroupId)));
            return true;
        }
        
        if (!chatModerator.getChatUsers().isEmpty()) {
            telegramService.sendMessage(new SendMessage(chatId, String.format(FAIL_DEL_MODER, moderGroupId)));
            return true;
        }
        
        chatModeratorService.deleteById(chatModerator.getId());
        telegramService.sendMessage(new SendMessage(chatId, String.format(OK_DEL_MODER, moderGroupId)));
        return true;
    }
}
