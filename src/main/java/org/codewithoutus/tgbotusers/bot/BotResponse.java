package org.codewithoutus.tgbotusers.bot;

import lombok.Data;

@Data
public final class BotResponse {
    private final boolean ok;
    private final BotStatus status;
}