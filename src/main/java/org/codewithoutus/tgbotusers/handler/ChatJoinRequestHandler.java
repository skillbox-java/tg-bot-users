package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.ChatJoinRequest;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.service.BotExecutorService;
import org.codewithoutus.tgbotusers.service.ChatJoinRequestService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatJoinRequestHandler implements Handler {
    private static final int MULTIPLICITY = 500;    // TODO replace on config
    private static final int INCREMENTAL_SAVES = 2; // TODO replace on config
    
    private final BotExecutorService botExecutorService;
    private final ChatJoinRequestService chatJoinRequestService;
    
    @Override
    public boolean handle(Update update) {
        
        ChatJoinRequest chatJoinRequest = update.chatJoinRequest();
        if (chatJoinRequest == null) {
            return false;
        }
        
        // TODO проверить из чата модераторов
        
        long chatId = chatJoinRequest.chat().id();
        int count = botExecutorService.getCount(chatId);
        if (count % MULTIPLICITY > INCREMENTAL_SAVES) {
            return false;
        }
    
        User user = chatJoinRequest.from();
        if (user.isBot()) {
            return false;
        }
        
        int date = chatJoinRequest.date();
    
        Map<String, ?> userData = Map.of(
                "userId", user.id(),
                "chatId", chatId,
                "count", count,
                "date", date);
        
        chatJoinRequestService.process(userData);
        
        return true;
    }
    
}
