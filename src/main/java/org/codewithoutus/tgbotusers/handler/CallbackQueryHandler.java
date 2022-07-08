package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Update;

import java.util.Collections;
import java.util.Map;

public class CallbackQueryHandler implements Handler {
    
    @Override
    public Map<String, ?> handle(Update update) {
        return Collections.emptyMap();
    }
}
