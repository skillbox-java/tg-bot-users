package org.codewithoutus.tgbotusers.bot.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.regex.Pattern;

@Getter
public enum BotCommand {

    CONGRATULATE("/congratulate", "T"),
    DECLINE("/decline", "T"),

    LUCKY_LIST("/luckyList", "T( (ID))?"),

    ADD_MODER_CHAT("/addModerChat", "T (ID)"),
    ADD_USER_CHAT("/addUserChat", "T (ID)"),
    DELETE_MODER_CHAT("/deleteModerChat", "T (ID)"),
    DELETE_USER_CHAT("/deleteUserChat", "T (ID)"),
    BIND_USER_CHAT_TO_MODER("/bindUserChatToModer", "T (ID) (ID)"),
    UNBIND_USER_CHAT_FROM_MODER("/unbindUserChatFromModer", "T (ID) (ID)"),

    HELP("/help", "T");

    private static final String ID_REGEX = "(\\-?\\d*{18})";
    private final Pattern regex;
    private final String text;
    private final String help;

    BotCommand(String text, String regex) {
        this.text = text;
        this.help = regex.replace("T", text);
        this.regex = Pattern.compile("^" + regex
                .replace("T", text)
                .replace("(ID)", ID_REGEX)
                + "$");
    }

    public static BotCommand getByCommandText(String command) {
        return Arrays.stream(values())
                .filter(value -> command.startsWith(value.getText()))
                .findFirst()
                .orElse(null);
    }
}