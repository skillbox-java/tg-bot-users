package org.codewithoutus.tgbotusers.bot.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.regex.Pattern;

@Getter
public enum BotCommand {

    LUCKY_LIST("/luckyList", "^/luckyList( (\\-?\\d*{18}))?"),
    CONGRATULATE("/congratulate", "^/congratulate$"),
    DECLINE("/decline", "^/decline");

    private final String text;
    private final Pattern regex;

    BotCommand(String text, String regex) {
        this.text = text;
        this.regex = Pattern.compile(regex);
    }

    public static BotCommand getByCommandText(String command) {
        return Arrays.stream(values())
                .filter(value -> command.startsWith(value.getText()))
                .findFirst()
                .orElse(null);
    }
}