package org.codewithoutus.tgbotusers.mocks.dto;

import lombok.Builder;

@Builder
public class Update extends com.pengrad.telegrambot.model.Update {
    private Message message;
    private CallbackQuery callback_query;
    private ChatJoinRequest chat_join_request;

    @Override
    public com.pengrad.telegrambot.model.Message message() {
        return message;
    }

    @Override
    public com.pengrad.telegrambot.model.CallbackQuery callbackQuery() {
        return callback_query;
    }

    @Override
    public com.pengrad.telegrambot.model.ChatJoinRequest chatJoinRequest() {
        return chat_join_request;
    }
}
