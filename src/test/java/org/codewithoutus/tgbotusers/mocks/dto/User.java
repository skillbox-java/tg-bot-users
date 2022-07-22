package org.codewithoutus.tgbotusers.mocks.dto;

import lombok.Builder;

public class User extends com.pengrad.telegrambot.model.User {
    private Long id;
    private Boolean is_bot;
    private String first_name;
    private String last_name;
    private String username;

    @Builder
    public User(Long id) {
        super(id);
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public Boolean isBot() {
        return is_bot;
    }

    @Override
    public String firstName() {
        return first_name;
    }

    @Override
    public String lastName() {
        return last_name;
    }

    @Override
    public String username() {
        return username;
    }
}
