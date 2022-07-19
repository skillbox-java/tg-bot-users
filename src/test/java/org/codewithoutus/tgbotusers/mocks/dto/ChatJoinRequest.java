package org.codewithoutus.tgbotusers.mocks.dto;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import lombok.Builder;

@Builder
public class ChatJoinRequest extends com.pengrad.telegrambot.model.ChatJoinRequest {
    private Chat chat;
    private User from;
    private Integer date;

    @Override
    public Chat chat() {
        return chat;
    }

    @Override
    public User from() {
        return from;
    }

    @Override
    public Integer date() {
        return date;
    }
}
