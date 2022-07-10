package org.codewithoutus.tgbotusers.service;

import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.config.GroupConfig;
import org.codewithoutus.tgbotusers.model.UserJoining;
import org.codewithoutus.tgbotusers.service.enums.ButtonText;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private static final String NOTIFICATION_TEMPLATE =
            "\uD83C\uDF89 В \"%s\" группу вступил юбилейный пользователь %s (%s), %s. \n" + "Время вступления %s";
    private static final String NO_NICK = "ника нет";
    
    private final GroupConfig groupConfig;
    private final TelegramBot bot;
    
    
    public void notifyModerators(UserJoining userJoining) {
        Map<String, ?> notification = createNotification(userJoining);
        sendNotification(notification, userJoining);
    }
    
    private Map<String, ?> createNotification(UserJoining userJoining) {
        Map<String, ?> joiningData = getJoiningData(userJoining);
        
        String text = String.format(NOTIFICATION_TEMPLATE, joiningData.get("chatName"),
                joiningData.get("userName"), joiningData.get("nickName"),
                joiningData.get("number"), joiningData.get("joinTime"));
        InlineKeyboardMarkup keyboard = createKeyboard(userJoining);
        
        return Map.of("text", text, "keyboard", keyboard);
    }
    
    // TODO organize mailing
    private void sendNotification(Map<String, ?> notification, UserJoining userJoining) {
        groupConfig.getModeratorChats();
        
        var sendMessage = new SendMessage(
                userJoining.getChatId(),
                (String) notification.get("text"))
                .replyMarkup((InlineKeyboardMarkup) notification.get("keyboard"));
        
        bot.execute(sendMessage);
    }
    
    private Map<String, ?> getJoiningData(UserJoining userJoining) {
        
        Long chatId = userJoining.getChatId();
        var getChat = new GetChat(chatId);
        var getChatResponse = bot.execute(getChat);
        String chatName = getChatResponse.chat().title();
        
        Long userId = userJoining.getUserId();
        var getChatMember = new GetChatMember(chatId, userId);
        var getChatMemberResponse = bot.execute(getChatMember);
        User user = getChatMemberResponse.chatMember().user();
        String firstName = user.firstName();
        String lastName = user.lastName();
        String userName = firstName + ((lastName == null) ? "" : (" " + lastName));
        
        String nickName = user.username();
        nickName = nickName == null ? NO_NICK : nickName;
        
        Integer number = userJoining.getNumber();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
        String joinTime = userJoining.getJoinTime().format(formatter);
        
        return Map.of("chatName", chatName,
                "userName", userName,
                "nickName", nickName,
                "number", number,
                "joinTime", joinTime);
    }
    
    private InlineKeyboardMarkup createKeyboard(UserJoining userJoining) {
        
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[]{
                new InlineKeyboardButton(ButtonText.CONGRATULATE.getSolution())
                        .callbackData(getCongratulateCallbackData(userJoining)),
                new InlineKeyboardButton(ButtonText.DECLINE.getSolution())
                        .callbackData(getDeclineCallbackData(userJoining))};
        inlineKeyboardMarkup.addRow(buttons);
        
        return inlineKeyboardMarkup;
    }
    
    private String getCongratulateCallbackData(UserJoining userJoining) {
        JsonObject jsonObject = getJsonObject(userJoining);
        jsonObject.addProperty("command", "congratulate");
        return jsonObject.getAsString();
    }
    
    private String getDeclineCallbackData(UserJoining userJoining) {
        JsonObject jsonObject = getJsonObject(userJoining);
        jsonObject.addProperty("command", "decline");
        return jsonObject.getAsString();
    }
    
    @NotNull
    private JsonObject getJsonObject(UserJoining userJoining) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userJoining.getUserId());
        jsonObject.addProperty("chatId", userJoining.getChatId());
        return jsonObject;
    }
    
}
