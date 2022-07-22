package org.codewithoutus.tgbotusers.bot.service;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.Bot;
import org.codewithoutus.tgbotusers.bot.enums.BotStatus;
import org.codewithoutus.tgbotusers.bot.handler.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotService {
    
    private final Bot bot;
    private final AdminMessageHandler adminMessageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final ChatJoinMessageHandler chatJoinMessageHandler;
    private final LuckyCommandHandler luckyListCommandHandler;
    private final PrivateMessageHandler privateMessageHandler;
    
    public boolean start() {
        if (bot.getStatus() == BotStatus.START) {
            return false;
        }
        startUpdatePolling();
        bot.setStatus(BotStatus.START);
        log.info("Bot running!");
        return true;
    }
    
    public boolean stop() {
        if (bot.getStatus() == BotStatus.STOP) {
            return false;
        }
        stopUpdatePolling();
        bot.setStatus(BotStatus.STOP);
        log.info("Bot stopped!");
        return true;
    }
    
    public BotStatus getStatus() {
        return bot.getStatus();
    }
    
    private void startUpdatePolling() {
        GetUpdates timeout = new GetUpdates().timeout(bot.getBotSettings().getLongPollingTimeout());
        UpdatesListener updatesListener = (updates -> {
            updates.forEach(this::handleUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        
        bot.setUpdatesListener(updatesListener, timeout);
    }
    
    private void stopUpdatePolling() {
        bot.removeGetUpdatesListener();
    }
    
    private void handleUpdate(Update update) {
        if (adminMessageHandler.tryHandle(update)
                || privateMessageHandler.tryHandle(update)
                || chatJoinMessageHandler.tryHandle(update)
                || callbackQueryHandler.tryHandle(update)
                || luckyListCommandHandler.tryHandle(update)) {
        }
    }
}
