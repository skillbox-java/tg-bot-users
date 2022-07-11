package org.codewithoutus.tgbotusers.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.GetUpdates;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.config.AppConfig;
import org.codewithoutus.tgbotusers.dto.BackendResponse;
import org.codewithoutus.tgbotusers.dto.enums.BotStatus;
import org.codewithoutus.tgbotusers.handler.UpdateHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {
    
    private final AppConfig appConfig;
    private final TelegramBot bot;
    private final UpdateHandler updateHandler;
    private BotStatus status;
    
    public BackendResponse start() {
        if (status != null && status.equals(BotStatus.START)) {
            return new BackendResponse(false, status);
        }
        
        GetUpdates getUpdates = new GetUpdates().timeout(appConfig.getLongPollingTimeout());
        bot.setUpdatesListener(updates -> {
                    updates.forEach(updateHandler::handle);
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                },
                getUpdates);
        status = BotStatus.START;
        
        return new BackendResponse(true, status);
    }
    
    public BackendResponse stop() {
        if (status == null || status.equals(BotStatus.STOP)) {
            return new BackendResponse(false, status);
        }
        
        bot.removeGetUpdatesListener();
        status = BotStatus.STOP;
        
        return new BackendResponse(true, status);
    }
    
}
