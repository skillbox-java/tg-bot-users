package org.codewithoutus.tgbotusers.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.config.BotSettings;
import org.codewithoutus.tgbotusers.handler.Handler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class BotService {

    private final Bot bot;
    private final BotSettings botSettings;
    private final List<Handler> updateHandlers;

    public void start() {
        GetUpdates timeout = new GetUpdates().timeout(botSettings.getLongPollingTimeout());
        Consumer<Update> updateHandler = (update -> {
            for (Handler handler : updateHandlers) {
                if (handler.handle(update)) {
                    return;
                }
            }
        });
        UpdatesListener updatesListener = (updates -> {
            updates.forEach(updateHandler);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        bot.setUpdatesListener(updatesListener, timeout);
    }

    public void stop() {
        bot.removeGetUpdatesListener();
    }
}
