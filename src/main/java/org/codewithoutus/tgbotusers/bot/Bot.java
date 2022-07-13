package org.codewithoutus.tgbotusers.bot;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Getter;
import lombok.Setter;
import org.codewithoutus.tgbotusers.bot.enums.BotStatus;
import org.codewithoutus.tgbotusers.config.BotSettings;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Bot extends TelegramBot {

    private final BotSettings botSettings;
    private BotStatus status;

    public Bot(BotSettings botSettings) {
        super(botSettings.getBotToken());
        this.botSettings = botSettings;
    }
}