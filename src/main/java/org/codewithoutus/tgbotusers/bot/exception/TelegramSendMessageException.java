package org.codewithoutus.tgbotusers.bot.exception;

public class TelegramSendMessageException extends RuntimeException {

    public TelegramSendMessageException(String message) {
        super(message);
    }
}