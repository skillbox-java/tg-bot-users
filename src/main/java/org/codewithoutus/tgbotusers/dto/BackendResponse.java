package org.codewithoutus.tgbotusers.dto;

import org.codewithoutus.tgbotusers.dto.enums.BotStatus;

public record BackendResponse(boolean ok, BotStatus status) {
}
