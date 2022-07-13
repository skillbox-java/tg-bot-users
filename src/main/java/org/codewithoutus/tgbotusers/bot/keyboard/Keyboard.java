package org.codewithoutus.tgbotusers.bot.keyboard;

import org.codewithoutus.tgbotusers.bot.enums.BotCommands;

public interface Keyboard {

    BotCommands getBotCommand();
    String getRepresentation();
}
