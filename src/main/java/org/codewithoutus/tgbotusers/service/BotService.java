package org.codewithoutus.tgbotusers.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.config.BotSettings;
import org.codewithoutus.tgbotusers.config.GroupSettings;
import org.codewithoutus.tgbotusers.dto.BackendResponse;
import org.codewithoutus.tgbotusers.dto.enums.BotStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {
    
    private final BotSettings botSettings;
    private final TelegramBot bot;
    
    public BackendResponse start() {
        GetUpdates getUpdates = new GetUpdates().timeout(botSettings.getLongPollingTimeout());
        bot.setUpdatesListener(updates -> {
                    updates.forEach(this::process);
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                },
                getUpdates);
        return new BackendResponse(true, BotStatus.START);
    }
    
    public BackendResponse stop() {
        bot.removeGetUpdatesListener();
        return new BackendResponse(true, BotStatus.STOP);
    }
    
    private void process(Update update) {
    
    }
    
    public void sendMessage(Long chatId, String message) {
        bot.execute(new SendMessage(chatId, message));
    }
}
