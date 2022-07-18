package org.codewithoutus.tgbotusers.bot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.keyboard.CongratulationDecisionKeyboard;
import org.codewithoutus.tgbotusers.bot.keyboard.KeyboardUtils;
import org.codewithoutus.tgbotusers.config.NotificationTemplates;
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

    private final NotificationTemplates notificationTemplates;
    private final TemplateEngine templateEngine;
    private final TelegramService telegramService;
    private final ChatModeratorService chatModeratorService;
    private final UserJoiningNotificationService userJoiningNotificationService;

    @Transactional
    public void notifyModeratorsAboutUserJoining(UserJoining userJoining) {
        log.debug("Start notifying about user(id={}, number={}) joining", userJoining.getUserId(), userJoining.getNumber());
        List<ChatModerator> moderatorChats = chatModeratorService.findByChatUsersId(userJoining.getChatId());
        if (moderatorChats.isEmpty()) {
            log.warn("No binding moderators to user chat {}", userJoining.getChatId());
            return;
        }

        InlineKeyboardMarkup keyboard = KeyboardUtils
                .createKeyboard(CongratulationDecisionKeyboard.class, String.valueOf(userJoining.getId()));

        String notificationText = templateEngine
                .buildFromTemplate(notificationTemplates.getJoinAlert(), userJoining, false);

        for (ChatModerator moderatorChat : moderatorChats) {
            SendMessage message = new SendMessage(moderatorChat.getChatId(), notificationText).replyMarkup(keyboard);
            SendResponse response = telegramService.sendMessage(message);
            log.debug("ModerChat(id={}) notified about user(id={}) joining. Status={}",
                    moderatorChat.getChatId(), userJoining.getUserId(), response.isOk());

            UserJoiningNotification notification = new UserJoiningNotification();
            notification.setUserJoining(userJoining);
            notification.setSentMessageChatId(moderatorChat.getChatId());
            notification.setSentMessageId(response.message().messageId());
            userJoiningNotificationService.save(notification);
            log.debug("Saved to DB notifying ModerChat(id={}) about user(id={}) joining",
                    moderatorChat.getChatId(), userJoining.getUserId());
        }
        log.debug("Finish notifying about user(id={}) joining", userJoining.getUserId());
    }

    @Transactional
    public void notifyUserAboutAnniversaryJoining(UserJoining userJoining) {
        log.debug("Start congratulate user(id={}, number={}) joining", userJoining.getUserId(), userJoining.getNumber());
        String notificationText = templateEngine
                .buildFromTemplate(notificationTemplates.getJoinCongratulation(), userJoining, false);

        SendMessage message = new SendMessage(userJoining.getChatId(), notificationText);
        SendResponse response = telegramService.sendMessage(message);
        log.debug("Finish congratulate user(id={}). Status={}", userJoining.getUserId(), response.isOk());
    }

    @Transactional
    public void deleteKeyboardFromJoiningNotification(Long userId, Long chatId, int anniversaryNumber) {
        log.debug("Start keyboard deleting about user(id={}, number={}) joining", userId, anniversaryNumber);
        userJoiningNotificationService
                .findByUserIdAndChatIdAndAnniversaryNumber(userId, chatId, anniversaryNumber)
                .forEach(this::deleteKeyboard);
        log.debug("Finish keyboard deleting about user(id={}, number={}) joining", userId, anniversaryNumber);
    }

    @Transactional
    public void deleteKeyboardFromAllJoiningNotifications(Long chatId, int anniversaryNumber) {
        log.debug("Start keyboard deleting about chat(id={}) {} joining", chatId, anniversaryNumber);
        userJoiningNotificationService
                .findByChatIdAndAnniversaryNumber(chatId, anniversaryNumber)
                .forEach(this::deleteKeyboard);
        log.debug("Finish keyboard deleting about chat(id={}) {} joining", chatId, anniversaryNumber);
    }

    private void deleteKeyboard(UserJoiningNotification notification) {
        telegramService.removeKeyboardFromMessage(notification.getSentMessageChatId(), notification.getSentMessageId());
    }
}