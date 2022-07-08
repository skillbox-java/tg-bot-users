package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Update;

import java.util.Map;

public interface Handler {
    Map<String, ?> handle(Update update);
}
