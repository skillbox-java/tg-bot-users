package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.TelegramService;
import org.codewithoutus.tgbotusers.bot.UpdateService;
import org.codewithoutus.tgbotusers.bot.enums.CongratulationDecisionKeyboard;
import org.codewithoutus.tgbotusers.model.UserJoining;
import org.codewithoutus.tgbotusers.service.UserJoiningService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryHandler implements Handler {

    private final TelegramService telegramService;
    private final UpdateService updateService;
    private final UserJoiningService userJoiningService;

    @Override
    public boolean handle(Update update) {
        Map<String, String> callbackQueryData = updateService.getCallbackData(update);
        if (callbackQueryData == null) {
            return false;
        }

        // Congratulation decision
        Optional<CongratulationDecisionKeyboard> key = CongratulationDecisionKeyboard.defineKey(callbackQueryData);
        if (key.isPresent()) {
            CongratulationDecisionKeyboard decision = key.get();
//            UserJoining userJoining = userJoiningService.findByUserIdAndChatId(userId, chatId);
//            userJoining.setStatus(decision.getStatus());
//            userJoiningService.save(userJoining);
            if (decision == CongratulationDecisionKeyboard.CONGRATULATE) {
                // send message
            }
            return true;
        }
//
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

        return false;
    }
}
