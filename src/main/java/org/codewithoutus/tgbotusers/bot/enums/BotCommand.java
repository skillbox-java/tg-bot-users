package org.codewithoutus.tgbotusers.bot.enums;

import lombok.Getter;

@Getter
public enum BotCommand {

    LUCKY_LIST("/luckyList"),
    CONGRATULATE("/congratulate"),
    DECLINE("/decline");

    private final String text;

    BotCommand(String text) {
        this.text = text;
    }
}