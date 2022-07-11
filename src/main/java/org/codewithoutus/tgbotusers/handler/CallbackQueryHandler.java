package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.TelegramService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryHandler implements Handler {

    private final TelegramService telegramService;

    @Override
    public boolean handle(Update update) {
        Map<String, String> callbackQueryData = telegramService.getCallbackData(update);
        if (callbackQueryData == null) {
            return false;
        }

//        Long moderatorChatId = Long.valueOf(callbackQueryData.get("moderatorChatId"));
//        Long chatId = Long.valueOf(callbackQueryData.get("chatId"));
//        List<ModeratorChat> moderatorChats = userChatService.getUserChats()
//                .get(chatId).getModeratorChats();
//        boolean fromModeratorChat = moderatorChats.stream()
//                .anyMatch(chat -> chat.getChatId().equals(moderatorChatId));
//        if (!fromModeratorChat) {
//            return false;
//        }
//
//        Long userId = Long.valueOf(callbackQueryData.get("userId"));
//        UserJoining userJoining = userJoiningService.findByUserIdAndChatId(userId, chatId);
//        String command = callbackQueryData.get("command");
//        if (userJoining.getStatus().equals(CongratulateStatus.WAIT)) {
//            if (command.equals("congratulate")) {
//                callbackQueryService.process(userJoining, CongratulateStatus.CONGRATULATE);
//                return true;
//            } else if (command.equals("decline")) {
//                callbackQueryService.process(userJoining, CongratulateStatus.DECLINE);
//                return true;
//            } else {
//                throw new CommandNotFoundException("Wrong callback data. Callback data does not contain a command.");
//            }
//        }
//
        return false;
    }
}
