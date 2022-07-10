package org.codewithoutus.tgbotusers.service;

import com.pengrad.telegrambot.model.Update;

import java.util.Map;

public interface TelegramEntityService {
    
    void process(Map<String, ?> data);
}
