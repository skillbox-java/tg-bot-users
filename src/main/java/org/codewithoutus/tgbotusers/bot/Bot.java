package org.codewithoutus.tgbotusers.bot;

import com.pengrad.telegrambot.TelegramBot;

public class Bot extends TelegramBot {

    private final BotService botService;
    private BotStatus status;

    public Bot(String botToken, BotService botService) {
        super(botToken);
        this.botService = botService;
    }

    public BotStatus getStatus() {
        return status;
    }

    public boolean start() {
        if (status == BotStatus.START) {
            return false;
        }
        botService.start();

        status = BotStatus.START;
        return true;
    }

    public boolean stop() {
        if (status == BotStatus.STOP) {
            return false;
        }
        botService.stop();

        status = BotStatus.STOP;
        return true;
    }
}