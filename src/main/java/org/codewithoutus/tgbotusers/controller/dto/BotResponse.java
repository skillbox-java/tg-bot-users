package org.codewithoutus.tgbotusers.controller.dto;

import lombok.Data;
import org.codewithoutus.tgbotusers.bot.enums.BotStatus;

@Data
public final class BotResponse {
    private final boolean ok;
    private final BotStatus status;
}