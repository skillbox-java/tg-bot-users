package org.codewithoutus.tgbotusers.bot.enums;

import lombok.Getter;

@Getter
public enum BotCommands {

    LUCKY_LIST("/luckyList"),
    CONGRATULATE("/congratulate"),
    DECLINE("/decline");

    private final String text;

    BotCommands(String text) {
        this.text = text;
    }
}