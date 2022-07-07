package org.codewithoutus.tgbotusers.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.config.AppConfig;
import org.codewithoutus.tgbotusers.config.GroupConfig;
import org.codewithoutus.tgbotusers.dto.BackendResponse;
import org.codewithoutus.tgbotusers.dto.enums.BotStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {
    
    private final AppConfig appConfig;
    private final GroupConfig groupConfig;
    private final TelegramBot bot;
    private BotStatus status;
    
    
    public BackendResponse start() {
        if (status.equals(BotStatus.START)) {
            return new BackendResponse(false, status);
        }
        
        GetUpdates getUpdates = new GetUpdates().timeout(appConfig.getLongPollingTimeout());
        bot.setUpdatesListener(updates -> {
                    updates.forEach(this::process);
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                },
                getUpdates);
        status = BotStatus.START;
        return new BackendResponse(true, status);
    }
    
    public BackendResponse stop() {
        status = BotStatus.STOP;
        bot.removeGetUpdatesListener();
        return new BackendResponse(true, status);
    }
    
    
    private void process(Update update) {
    
    }
    
    
    public BackendResponse sendMessage(String message) {
        SendMessage sendMessage = new SendMessage(-644481529, message);
        bot.execute(sendMessage);
        return new BackendResponse(true, BotStatus.START);
    }
}
