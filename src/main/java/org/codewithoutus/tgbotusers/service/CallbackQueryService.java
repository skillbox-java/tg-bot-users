package org.codewithoutus.tgbotusers.service;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CallbackQueryService implements TelegramEntityService {
    
    @Override
    public void process(Update update) {
    
    }
}
