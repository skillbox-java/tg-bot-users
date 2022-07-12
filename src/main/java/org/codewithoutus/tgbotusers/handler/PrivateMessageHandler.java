package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.TelegramService;
import org.codewithoutus.tgbotusers.bot.UpdateService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrivateMessageHandler implements Handler {

    private static final String SORRY = "Sorry, functionality not implemented";
    private final TelegramService telegramService;
    private final UpdateService updateService;
    
    @Override
    public boolean handle(Update update) {
        if (!updateService.isPrivateMessage(update)) {
            return false;
        }
        telegramService.sendMessage(updateService.getChatId(update), SORRY);
        return true;

//        Message message = update.message();
//        MessageEntity[] entities = message.entities();
//        if (entities == null || entities.length <= 0) {
//            return false;
//        }
//
//        Optional<MessageEntity> botEntityOptional = Arrays.stream(message.entities())
//                .filter(entity -> entity.type().equals(MessageEntity.Type.bot_command)).findFirst();
//        if (botEntityOptional.isEmpty()) {
//            return false;
//        }
//
//        Long chatId = message.chat().id();
//        if (!moderatorChatService.getModeratorChats().containsKey(chatId)) {
//            return false;
//        }
//
//        MessageEntity entity = botEntityOptional.get();
//
//        messageService.process(message.text(), entity);
//
//        return true;
    }
}
