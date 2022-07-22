package org.codewithoutus.tgbotusers.bot.keyboard;

import org.codewithoutus.tgbotusers.bot.enums.BotCommand;

public interface Keyboard {

    BotCommand getBotCommand();
    String getRepresentation();
}
