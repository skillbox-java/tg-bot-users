package org.codewithoutus.tgbotusers.service.enums;

import lombok.Getter;

@Getter
public enum BotCommand {
    
    LUCKY_LIST("/luckyList");
    
    private final String command;
    
    BotCommand(String command) {
        this.command = command;
    }
}
