package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.ChatJoinRequest;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.TelegramService;
import org.codewithoutus.tgbotusers.service.ChatJoinRequestService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatJoinRequestHandler implements Handler {

    private final TelegramService botExecutorService;
    private final ChatJoinRequestService chatJoinRequestService;
    
    @Override
    public boolean handle(Update update) {
        ChatJoinRequest chatJoinRequest = update.chatJoinRequest();
        if (chatJoinRequest == null) {
            return false;
        }
        
//        // TODO проверить из чата модераторов
//
//        long chatId = chatJoinRequest.chat().id();
//        int count = botExecutorService.getCount(chatId);
//        if (count % MULTIPLICITY > INCREMENTAL_SAVES) {
//            return false;
//        }
//
//        User user = chatJoinRequest.from();
//        if (user.isBot()) {
//            return false;
//        }
//
//        int date = chatJoinRequest.date();
//
//        Map<String, ?> userData = Map.of(
//                "userId", user.id(),
//                "chatId", chatId,
//                "count", count,
//                "date", date);
//
//        chatJoinRequestService.process(userData);
        
        return true;
    }
}
