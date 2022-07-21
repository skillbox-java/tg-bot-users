package org.codewithoutus.tgbotusers.bot.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.regex.Pattern;

@Getter
public enum BotCommand {

    CONGRATULATE("/congratulate", "T"),
    DECLINE("/decline", "T"),

    LUCKY_LIST("/luckyList", "T( {id}})?"),
    CHOOSE_LUCKY("/chooseLucky", "T( {id})?"),

    ADD_MODER_CHAT("/addModerChat", "T {id}"),
    ADD_USER_CHAT("/addUserChat", "T {id}"),
    DELETE_MODER_CHAT("/deleteModerChat", "T {id}"),
    DELETE_USER_CHAT("/deleteUserChat", "T {id}"),
    BIND_USER_CHAT_TO_MODER("/bindUserChatToModer", "T {id} {id}"),
    UNBIND_USER_CHAT_FROM_MODER("/unbindUserChatFromModer", "T {id} {id}"),

    HELP("/help", "T"),
    CURRENT_SETTINGS("/currentSettings", "T");

    private static final String ID_REGEX = "(\\-?\\d*{18})";
    private final Pattern regex;
    private final String text;
    private final String params;
    private final String help;

    BotCommand(String text, String regex) {
        this.text = text;
        this.params = regex.replace("T", "");
        this.help = regex.replace("T", text);
        this.regex = Pattern.compile("^" + regex
                .replace("T", text)
                .replace("{id}", ID_REGEX)
                + "$");
    }

    public static BotCommand getByCommandText(String command) {
        return Arrays.stream(values())
                .filter(value -> command.startsWith(value.getText()))
                .findFirst()
                .orElse(null);
    }
}