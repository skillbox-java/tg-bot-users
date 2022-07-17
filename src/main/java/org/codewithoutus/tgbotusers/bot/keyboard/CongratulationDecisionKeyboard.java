package org.codewithoutus.tgbotusers.bot.keyboard;

import lombok.Getter;
import org.codewithoutus.tgbotusers.bot.enums.BotCommand;

@Getter
public enum CongratulationDecisionKeyboard implements Keyboard {

    CONGRATULATE(BotCommand.CONGRATULATE, "Поздравить \uD83E\uDD73"),
    DECLINE(BotCommand.DECLINE, "Отклонить 🚫");

    private final BotCommand botCommand;
    private final String representation;

    CongratulationDecisionKeyboard(BotCommand botCommand, String representation) {
        this.botCommand = botCommand;
        this.representation = representation;
    }
}