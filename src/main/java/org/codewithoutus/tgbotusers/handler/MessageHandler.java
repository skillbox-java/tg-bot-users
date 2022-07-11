package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.service.MessageService;
import org.codewithoutus.tgbotusers.service.ModeratorChatService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageHandler implements Handler {
    
    private final MessageService messageService;
    private final ModeratorChatService moderatorChatService;
    
    @Override
    public boolean handle(Update update) {
        
        Message message = update.message();
        MessageEntity[] entities = message.entities();
        if (entities == null || entities.length <= 0) {
            return false;
        }
        
        Optional<MessageEntity> botEntityOptional = Arrays.stream(message.entities())
                .filter(entity -> entity.type().equals(MessageEntity.Type.bot_command)).findFirst();
        if (botEntityOptional.isEmpty()) {
            return false;
        }
        
        Long chatId = message.chat().id();
        if (!moderatorChatService.getModeratorChats().containsKey(chatId)) {
            return false;
        }
        
        MessageEntity entity = botEntityOptional.get();
        
        messageService.process(message.text(), entity);
        
        return true;
    }
}
