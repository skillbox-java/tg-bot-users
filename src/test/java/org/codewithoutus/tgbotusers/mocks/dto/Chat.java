package org.codewithoutus.tgbotusers.mocks.dto;

import lombok.Builder;

@Builder
public class Chat extends com.pengrad.telegrambot.model.Chat {
    private Long id;
    private Type type;
    private String title;

    @Override
    public Long id() {
        return id;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public String title() {
        return title;
    }
}