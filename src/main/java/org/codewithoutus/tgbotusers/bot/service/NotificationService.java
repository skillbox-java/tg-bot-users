package org.codewithoutus.tgbotusers.bot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.keyboard.CongratulationDecisionKeyboard;
import org.codewithoutus.tgbotusers.bot.keyboard.KeyboardUtils;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.entity.TelegramCallbackData;
import org.codewithoutus.tgbotusers.model.entity.UserJoiningNotification;
import org.codewithoutus.tgbotusers.model.service.ChatModeratorService;
import org.codewithoutus.tgbotusers.model.service.TelegramCallbackDataService;
import org.codewithoutus.tgbotusers.model.service.UserJoiningNotificationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final TelegramService telegramService;
    private final TelegramCallbackDataService telegramCallbackDataService;
    private final ChatModeratorService chatModeratorService;
    private final UserJoiningNotificationService userJoiningNotificationService;

    @Transactional
    public void notifyModeratorsAboutUserJoining(Long chatId, Long userId, LocalDateTime joinTime, int number, int anniversaryNumber) {
        TelegramCallbackData callbackData = telegramCallbackDataService.saveMap(Map.of(
                "chatId", chatId,
                "userId", userId,
                "joinTime", joinTime,
                "number", number,
                "anniversaryNumber", anniversaryNumber));

        InlineKeyboardMarkup keyboard = KeyboardUtils.createKeyboard(
                CongratulationDecisionKeyboard.class,
                String.valueOf(callbackData.getId()));

        String notificationText = callbackData.toString(); // TODO: подтянуть шаблон

        List<ChatModerator> moderators = chatModeratorService.findByChatUsersId(chatId);
        for (ChatModerator moderator : moderators) {
            SendMessage message = new SendMessage(moderator.getChatId(), notificationText).replyMarkup(keyboard);
            SendResponse response = telegramService.sendMessage(message);

            UserJoiningNotification notification = new UserJoiningNotification();
            notification.setChatId(chatId);
            notification.setUserId(userId);
            notification.setAnniversaryNumber(anniversaryNumber);
            notification.setSentMessageChatId(moderator.getChatId());
            notification.setSentMessageId(response.message().messageId());
            userJoiningNotificationService.save(notification);
        }
    }

    @Transactional
    public void notifyUserAboutJoining(Long chatId, Long userId, int anniversaryNumber) {
        // TODO: реализовать оповещение и всю запись в базу
    }

    public void deleteKeyboardFromJoiningNotification(Long chatId, Long userId, int anniversaryNumber) {
        deleteKeyboardFromJoiningNotifications(chatId, List.of(userId), anniversaryNumber);
    }

    @Transactional
    public void deleteKeyboardFromJoiningNotifications(Long chatId, List<Long> userIdList, int anniversaryNumber) {
        // TODO: надо проверить
        userJoiningNotificationService.findByChatIdAndAnniversaryNumber(chatId, anniversaryNumber)
                .stream()
                .filter(notification -> userIdList.contains(notification.getUserId()))
                .forEach(notification -> {
                    telegramService.editMessageReplyMarkup(
                            new EditMessageReplyMarkup(chatId, notification.getSentMessageId())
                                    .replyMarkup(new InlineKeyboardMarkup()));
                });
    }
}