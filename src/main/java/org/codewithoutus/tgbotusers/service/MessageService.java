package org.codewithoutus.tgbotusers.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageService implements TelegramEntityService {
    
    @Override
    public void process(Map<String, ?> messageData) {
    
        // TODO implement logic
        
    }
}
