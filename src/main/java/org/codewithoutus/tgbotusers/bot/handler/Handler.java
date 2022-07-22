package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Handler {

    public boolean tryHandle(Update update) {
        try {
            return handle(update);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    abstract protected boolean handle(Update update);
}