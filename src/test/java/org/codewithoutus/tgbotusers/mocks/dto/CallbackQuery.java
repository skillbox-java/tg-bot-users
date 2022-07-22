package org.codewithoutus.tgbotusers.mocks.dto;

import lombok.Builder;

@Builder
public class CallbackQuery extends com.pengrad.telegrambot.model.CallbackQuery {
    private String data;
}
