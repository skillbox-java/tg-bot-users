package org.codewithoutus.tgbotusers.service;

import com.pengrad.telegrambot.model.Update;

public interface TelegramEntityService {
    
    void process(Update update);
}
