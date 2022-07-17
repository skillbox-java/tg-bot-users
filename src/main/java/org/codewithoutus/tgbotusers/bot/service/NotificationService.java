package org.codewithoutus.tgbotusers.bot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.keyboard.CongratulationDecisionKeyboard;
import org.codewithoutus.tgbotusers.bot.keyboard.KeyboardUtils;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.entity.UserJoiningNotification;
import org.codewithoutus.tgbotusers.model.service.ChatModeratorService;
import org.codewithoutus.tgbotusers.model.service.UserJoiningNotificationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final TelegramService telegramService;
    private final ChatModeratorService chatModeratorService;
    private final UserJoiningNotificationService userJoiningNotificationService;

    @Transactional
    public void notifyModeratorsAboutUserJoining(UserJoining userJoining) {
        List<ChatModerator> moderatorChats = chatModeratorService.findByChatUsersId(userJoining.getChatId());
        if (moderatorChats.isEmpty()) {
            log.warn("");
            return;
        }

        InlineKeyboardMarkup keyboard = KeyboardUtils
                .createKeyboard(CongratulationDecisionKeyboard.class, String.valueOf(userJoining.getId()));

        String notificationText = userJoining.toString(); // TODO: Алекс -- подтянуть шаблон

        for (ChatModerator moderatorChat : moderatorChats) {
            SendMessage message = new SendMessage(moderatorChat.getChatId(), notificationText).replyMarkup(keyboard);
            SendResponse response = telegramService.sendMessage(message);

            UserJoiningNotification notification = new UserJoiningNotification();
            notification.setUserJoining(userJoining);
            notification.setSentMessageChatId(moderatorChat.getChatId());
            notification.setSentMessageId(response.message().messageId());
            userJoiningNotificationService.save(notification);
        }
    }

    @Transactional
    public void notifyUserAboutAnniversaryJoining(UserJoining userJoining) {
        String notificationText = userJoining.toString(); // TODO: Алекс -- подтянуть шаблон

        SendMessage message = new SendMessage(userJoining.getChatId(), notificationText);
        telegramService.sendMessage(message);
    }

    @Transactional
    public void deleteKeyboardFromJoiningNotification(Long userId, Long chatId, int anniversaryNumber) {
        userJoiningNotificationService.findByUserIdAndChatIdAndAnniversaryNumber(userId, chatId, anniversaryNumber)
                .forEach(notification -> telegramService.removeKeyboardFromMessage(chatId, notification.getSentMessageId()));
    }

    @Transactional
    public void deleteKeyboardFromAllJoiningNotifications(Long chatId, int anniversaryNumber) {
        userJoiningNotificationService.findByChatIdAndAnniversaryNumber(chatId, anniversaryNumber)
                .forEach(notification -> telegramService.removeKeyboardFromMessage(chatId, notification.getSentMessageId()));
    }
}