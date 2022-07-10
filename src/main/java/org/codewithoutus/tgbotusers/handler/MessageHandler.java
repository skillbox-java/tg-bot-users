package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.service.MessageService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageHandler implements Handler {
    
    private final MessageService messageService;
    
    @Override
    public boolean handle(Update update) {
        
        // TODO implement logic
    
        Map<String, ?> messageData = Map.of();
    
        messageService.process(messageData);
    
        return true;
    }
}
