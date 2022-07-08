package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.ChatJoinRequest;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Deprecated
@RequiredArgsConstructor
public class UpdateProcessor {
    
    private final ChatJoinRequestHandler chatJoinRequestHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final MessageHandler messageHandler;
    
    private Update currentUpdate;
    
    public void updateProcess(Update update) {
        currentUpdate = update;
        
        ChatJoinRequest chatJoinRequest = currentUpdate.chatJoinRequest();
        if (chatJoinRequest != null) {
            handleChatJoinRequest(chatJoinRequest);
        }
    
        CallbackQuery callbackQuery = currentUpdate.callbackQuery();
        if (callbackQuery != null) {
            handleCallback(callbackQuery);
        }
        
        Message message = currentUpdate.message();
        if (message != null) {
            handleCommand(message);
        }
    }
    
    private void handleChatJoinRequest(ChatJoinRequest chatJoinRequest) {
    
    }
    
    private void handleCallback(CallbackQuery callbackQuery) {
    
    }
    
    private void handleCommand(Message message) {
        MessageEntity[] entities = message.entities();
        if (entities != null && entities.length > 0) {
        
        }
    }
}
