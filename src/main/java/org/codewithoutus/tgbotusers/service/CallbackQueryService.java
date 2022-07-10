package org.codewithoutus.tgbotusers.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CallbackQueryService implements TelegramEntityService {
    
    @Override
    public void process(Map<String, ?> callbackQueryData) {
    
        // TODO implement logic
        
    }
}
