package org.codewithoutus.tgbotusers.bot.keyboard;

import lombok.Getter;
import org.codewithoutus.tgbotusers.bot.enums.BotCommands;

@Getter
public enum CongratulationDecisionKeyboard implements Keyboard {

    CONGRATULATE(BotCommands.CONGRATULATE, "Поздравить \uD83E\uDD73"),
    DECLINE(BotCommands.DECLINE, "Отклонить 🚫");

    private final BotCommands botCommand;
    private final String representation;

    CongratulationDecisionKeyboard(BotCommands botCommand, String representation) {
        this.botCommand = botCommand;
        this.representation = representation;
    }
}