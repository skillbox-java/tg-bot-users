package org.codewithoutus.tgbotusers.mocks.dto;

import lombok.Builder;

public class MessageEntity extends com.pengrad.telegrambot.model.MessageEntity {
    private Type type;
    private Integer offset;
    private Integer length;

    @Builder
    public MessageEntity(Type type, Integer offset, Integer length) {
        super(type, offset, length);
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public Integer offset() {
        return offset;
    }

    @Override
    public Integer length() {
        return length;
    }
}
