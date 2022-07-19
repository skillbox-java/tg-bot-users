package org.codewithoutus.tgbotusers.mocks.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Message extends com.pengrad.telegrambot.model.Message {
    private Integer message_id;
    private User from;
    private Integer date;
    private Chat chat;
    private Integer forward_date;
    private String text;
    private MessageEntity[] entities;

    @Override
    public Integer messageId() {
        return message_id;
    }

    @Override
    public com.pengrad.telegrambot.model.User from() {
        return from;
    }

    @Override
    public Integer date() {
        return date;
    }

    @Override
    public com.pengrad.telegrambot.model.Chat chat() {
        return chat;
    }

    @Override
    public Integer forwardDate() {
        return forward_date;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public com.pengrad.telegrambot.model.MessageEntity[] entities() {
        return entities;
    }
}
