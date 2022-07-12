package org.codewithoutus.tgbotusers.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import org.codewithoutus.tgbotusers.handler.Handler;

import java.util.List;

public class Bot extends TelegramBot {

    private final int longPollingTimeout;
    private List<Handler> updateHandlers;
    private BotStatus status;

    public Bot(String botToken, int longPollingTimeout) {
        super(botToken);
        this.longPollingTimeout = longPollingTimeout;
    }

    public BotStatus getStatus() {
        return status;
    }

    public void setUpdateHandlers(List<Handler> updateHandlers) {
        this.updateHandlers = updateHandlers;
    }

    public boolean start() {
        if (status == BotStatus.START) {
            return false;
        }
        startUpdatePolling();
        status = BotStatus.START;
        return true;
    }

    public boolean stop() {
        if (status == BotStatus.STOP) {
            return false;
        }
        stopUpdatePolling();
        status = BotStatus.STOP;
        return true;
    }

    private void startUpdatePolling() {
        GetUpdates timeout = new GetUpdates().timeout(longPollingTimeout);
        UpdatesListener updatesListener = (updates -> {
            updates.forEach(this::handleUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        setUpdatesListener(updatesListener, timeout);
    }

    private void stopUpdatePolling() {
        removeGetUpdatesListener();
    }

    private void handleUpdate(Update update) {
        for (Handler handler : updateHandlers) {
            if (handler.handle(update)) {
                return;
            }
        }
    }
}