package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Update;

public interface Handler {
    boolean handle(Update update);
}