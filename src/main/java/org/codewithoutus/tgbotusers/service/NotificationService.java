package org.codewithoutus.tgbotusers.service;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final String NO_NICK = "ника нет";

    private final TelegramBot bot;

//    public void notifyModerators(UserJoining userJoining) {
//        String text = createTextNotification(userJoining);
//        sendNotification(text, userJoining);
//    }
//
//    public void congratulateUser(Long chatId, String userName, Integer number) {
//        // TODO use 'number' from config
//
//        String text = String.format(CONGRATULATION_TEMPLATE, userName, number);
//        bot.execute(new SendMessage(chatId, text));
//    }
//
//    private String createTextNotification(UserJoining userJoining) {
//        Map<String, ?> joiningData = getJoiningData(userJoining);
//
//        return String.format(NOTIFICATION_TEMPLATE, joiningData.get("chatName"),
//                joiningData.get("userName"), joiningData.get("nickName"),
//                joiningData.get("number"), joiningData.get("joinTime"));
//    }
//
//    // TODO organize mailing
//    private void sendNotification(String text, UserJoining userJoining) {
//        List<ModeratorChat> moderatorChats = userChatService.getUserChats()
//                .get(userJoining.getChatId()).getModeratorChats();
//
//        for (ModeratorChat moderatorChat : moderatorChats) {
//            Long moderatorChatId = moderatorChat.getChatId();
//            var sendMessage = new SendMessage(moderatorChatId, text)
//                    .replyMarkup(createKeyboard(userJoining, moderatorChatId));
//
//            bot.execute(sendMessage);
//        }
//    }
//
//    private Map<String, ?> getJoiningData(UserJoining userJoining) {
//
//        Long chatId = userJoining.getChatId();
//        var getChat = new GetChat(chatId);
//        var getChatResponse = bot.execute(getChat);
//        String chatName = getChatResponse.chat().title();
//
//        Long userId = userJoining.getUserId();
//        var getChatMember = new GetChatMember(chatId, userId);
//        var getChatMemberResponse = bot.execute(getChatMember);
//        User user = getChatMemberResponse.chatMember().user();
//        String firstName = user.firstName();
//        String lastName = user.lastName();
//        String userName = firstName + ((lastName == null) ? "" : (" " + lastName));
//
//        String nickName = user.username();
//        nickName = nickName == null ? NO_NICK : nickName;
//
//        Integer number = userJoining.getNumber();
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
//        String joinTime = userJoining.getJoinTime().format(formatter);
//
//        return Map.of("chatName", chatName,
//                "userName", userName,
//                "nickName", nickName,
//                "number", number,
//                "joinTime", joinTime);
//    }
//
//    private InlineKeyboardMarkup createKeyboard(UserJoining userJoining, Long moderatorChatId) {
//
//        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
//
//        InlineKeyboardButton[] buttons = new InlineKeyboardButton[]{
//                new InlineKeyboardButton(ButtonText.CONGRATULATE.getSolution())
//                        .callbackData(getCongratulateCallbackData(userJoining, moderatorChatId)),
//                new InlineKeyboardButton(ButtonText.DECLINE.getSolution())
//                        .callbackData(getDeclineCallbackData(userJoining, moderatorChatId))};
//        inlineKeyboardMarkup.addRow(buttons);
//
//        return inlineKeyboardMarkup;
//    }
//
//    private String getCongratulateCallbackData(UserJoining userJoining, Long moderatorChatId) {
//        JsonObject jsonObject = getJsonObject(userJoining, moderatorChatId);
//        jsonObject.addProperty("command", "congratulate");
//        return jsonObject.getAsString();
//    }
//
//    private String getDeclineCallbackData(UserJoining userJoining, Long moderatorChatId) {
//        JsonObject jsonObject = getJsonObject(userJoining, moderatorChatId);
//        jsonObject.addProperty("command", "decline");
//        return jsonObject.getAsString();
//    }
//
//    @NotNull
//    private JsonObject getJsonObject(UserJoining userJoining, Long moderatorChatId) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("moderatorChatId", moderatorChatId);
//        jsonObject.addProperty("userId", userJoining.getUserId());
//        jsonObject.addProperty("chatId", userJoining.getChatId());
//        return jsonObject;
//    }
//
//    public void removeKeyboardFromNotification() {
//        // TODO implement
//    }
}