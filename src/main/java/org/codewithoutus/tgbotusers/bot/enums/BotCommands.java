package org.codewithoutus.tgbotusers.bot.enums;

import lombok.Getter;

@Getter
public enum BotCommands {
    
    LUCKY_LIST("/luckyList");
    
    private final String command;
    
    BotCommands(String command) {
        this.command = command;
    }
}
