package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UpdateHandler implements Handler {
    
    private final ChatJoinRequestHandler chatJoinRequestHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final MessageHandler messageHandler;
    
    @Override
    public boolean handle(Update update) {
        boolean chatJoinRequestHandlerResult = chatJoinRequestHandler.handle(update);
        boolean callbackHandlerResult = callbackQueryHandler.handle(update);
        boolean messageHandlerResult = messageHandler.handle(update);
    
        return chatJoinRequestHandlerResult || callbackHandlerResult || messageHandlerResult;
    }
}
