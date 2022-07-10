package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.service.CallbackQueryService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler implements Handler {
    
    private final CallbackQueryService callbackQueryService;
    
    @Override
    public boolean handle(Update update) {
        
        // TODO implement logic
        
        Map<String, ?> callbackQueryData = Map.of();
        
        callbackQueryService.process(callbackQueryData);
        
        return true;
    }
}
