package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.enums.BotCommand;
import org.codewithoutus.tgbotusers.bot.keyboard.Template;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.service.ChatModeratorService;
import org.codewithoutus.tgbotusers.model.service.ChatUserService;
import org.codewithoutus.tgbotusers.model.service.UserJoiningService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component("UpdateMessageHandler")
@RequiredArgsConstructor
public class MessageHandler extends Handler {
    
    private final ChatModeratorService chatModeratorService;
    private final ChatUserService chatUserService;
    private final UserJoiningService userJoiningService;
    private final TelegramService telegramService;
    private final Template template;
    
    @Override
    protected boolean handle(Update update) {
        // TODO: Алекс -- реализовать вывод списка счастивчиков "/списокЮбилейный" или "/luckyList"
        
        // TODO: Алекс -- если на anniversary number не было поздравления, то выводятся все претенденты с кнопками CongratulationDecisionKeyboard
        // TODO: Алекс -- если поздравленного нет, то отклоненные тоже выводятся
        // TODO: Алекс -- все новые кнопки нужно записывать в таблицу UserJoiningNotification, чтобы потом также удалялись
        // TODO: Алекс -- можно попробовать выводить все-все чаты, а потом доработать -- на будущее
        
        Message message = update.message();
        Optional<MessageEntity> botEntityOptional = getBotCommandEntity(message);
        if (botEntityOptional.isEmpty()) {
            return false;
        }
    
        Long chatId = message.chat().id();
        if (!chatModeratorService.existsByChatId(chatId)) {
            return false;
        }
    
        MessageEntity entity = botEntityOptional.get();
    
        handleBotCommand(message.text(), entity, chatId);
        
        return false;
    }
    
    
    private Optional<MessageEntity> getBotCommandEntity(Message message) {
        
        if (message == null) {
            return Optional.empty();
        }
        
        MessageEntity[] entities = message.entities();
        if (entities == null || entities.length <= 0) {
            return Optional.empty();
        }
        
        return Arrays.stream(message.entities())
                .filter(entity -> entity.type().equals(MessageEntity.Type.bot_command))
                .findFirst();
    }
    
    private void handleBotCommand(String messageText, MessageEntity entity, Long moderatorChatId) {
        if (messageText.contains(BotCommand.LUCKY_LIST.getText())) {
            performLuckyList(messageText, entity, moderatorChatId);
        }
    }
    
    private void performLuckyList(String messageText, MessageEntity entity, Long moderatorChatId) {
        String luckyListText = "";
        String param = parseParam(messageText, entity);
        if (!param.isEmpty()) {
            Optional<ChatUser> chatUserOptional = chatUserService.findByName(param);
            if(chatUserOptional.isPresent()) {
                luckyListText = createLuckyListText(List.of(chatUserOptional.get()));
            }
        } else {
            ChatModerator chatModerator = chatModeratorService.findByChatId(moderatorChatId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "DB was changed after check exists with moderatorChatId=" + moderatorChatId));
            luckyListText = createLuckyListText(chatModerator.getChatUsers());
        }
        
        sendLuckyList(luckyListText, moderatorChatId);
    }
    
    private String parseParam(String text, MessageEntity entity) {
        String param = text.substring(entity.offset() + entity.length());
        return param.trim();
    }
    
    private String createLuckyListText(List<ChatUser> chatUsers) {
        
        // получить все объекты userJoining из базы
        List<UserJoining> userJoinings = new ArrayList<>();
        for (ChatUser chatUser : chatUsers) {
            List<UserJoining> joinings = userJoiningService.findByChatId(chatUser.getChatId());
            userJoinings.addAll(joinings);
        }
        
        userJoinings.sort(Comparator.naturalOrder());
        
        // получить актуальные данные по каждому объекту
        // собрать список в строку
        return userJoinings.stream()
                .map(this::buildUserInfo)
                .reduce((userInfo1, userInfo2) -> {
                    if (!userInfo1.isEmpty()) {
                        userInfo1.append("\n\n");
                    }
                    userInfo1.append(userInfo2);
                    return userInfo1;
                })
                .orElse(new StringBuilder())
                .toString();
    }
    
    private StringBuilder buildUserInfo(UserJoining userJoining) {
        return new StringBuilder(template.getUserInfoText(userJoining));
    }
    
    private void sendLuckyList(String luckyListText, Long moderatorChatId) {
        telegramService.sendMessage(new SendMessage(moderatorChatId, luckyListText));
    }

}
