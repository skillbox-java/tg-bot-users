package org.codewithoutus.tgbotusers.service;

import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.config.GroupConfig;
import org.codewithoutus.tgbotusers.model.JoinedUser;
import org.codewithoutus.tgbotusers.service.enums.TextButton;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private static final String NOTIFICATION_TEMPLATE = "\uD83C\uDF89 В \"%s\" группу вступил юбилейный пользователь %s (%s), %s. \n" + "Время вступления %s";
    private static final String NO_NICK = "ника нет";
    
    private final GroupConfig groupConfig;
    private final TelegramBot bot;
    
    
    public void notifyModerators(JoinedUser joinedUser) {
        Map<String, ?> notification = createNotification(joinedUser);
        sendNotification(notification, joinedUser);
    }
    
    private Map<String, ?> createNotification(JoinedUser joinedUser) {
        Map<String, ?> joiningData = getJoiningData(joinedUser);
        
        String text = String.format(NOTIFICATION_TEMPLATE, joiningData.get("chatName"), joiningData.get("memberName"), joiningData.get("nickName"), joiningData.get("number"), joiningData.get("joinTime"));
        InlineKeyboardMarkup keyboard = createKeyboard(joinedUser);
        
        return Map.of("text", text, "keyboard", keyboard);
    }
    
    // TODO organize mailing
    private void sendNotification(Map<String, ?> notification, JoinedUser joinedUser) {
        groupConfig.getModeratorChats();
        
        var sendMessage = new SendMessage(
                joinedUser.getChatId(),
                (String) notification.get("text"))
                .replyMarkup((InlineKeyboardMarkup) notification.get("keyboard"));
        
        bot.execute(sendMessage);
    }
    
    private Map<String, ?> getJoiningData(JoinedUser joinedUser) {
        
        Long chatId = joinedUser.getChatId();
        var getChat = new GetChat(chatId);
        var getChatResponse = bot.execute(getChat);
        String chatName = getChatResponse.chat().title();
        
        Long userId = joinedUser.getUserId();
        var getChatMember = new GetChatMember(chatId, userId);
        var getChatMemberResponse = bot.execute(getChatMember);
        User user = getChatMemberResponse.chatMember().user();
        String firstName = user.firstName();
        String lastName = user.lastName();
        String memberName = firstName + ((lastName == null) ? "" : (" " + lastName));
        
        String userName = user.username();
        String nickName = userName == null ? NO_NICK : userName;
        
        Integer number = joinedUser.getNumber();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
        String joinTime = joinedUser.getJoinTime().format(formatter);
        
        return Map.of("chatName", chatName, "memberName", memberName, "nickName", nickName, "number", number, "joinTime", joinTime);
    }
    
    private InlineKeyboardMarkup createKeyboard(JoinedUser joinedUser) {
        
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[]{new InlineKeyboardButton(TextButton.CONGRATULATE.getSolution()).callbackData(getCongratulateCallbackData(joinedUser)), new InlineKeyboardButton(TextButton.DECLINE.getSolution()).callbackData(getDeclineCallbackData(joinedUser))};
        inlineKeyboardMarkup.addRow(buttons);
        
        return inlineKeyboardMarkup;
    }
    
    private String getCongratulateCallbackData(JoinedUser joinedUser) {
        JsonObject jsonObject = getJsonObject(joinedUser);
        jsonObject.addProperty("command", "congratulate");
        return jsonObject.getAsString();
    }
    
    private String getDeclineCallbackData(JoinedUser joinedUser) {
        JsonObject jsonObject = getJsonObject(joinedUser);
        jsonObject.addProperty("command", "decline");
        return jsonObject.getAsString();
    }
    
    @NotNull
    private JsonObject getJsonObject(JoinedUser joinedUser) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", joinedUser.getUserId());
        jsonObject.addProperty("chatId", joinedUser.getChatId());
        return jsonObject;
    }
    
}
