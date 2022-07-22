package org.codewithoutus.tgbotusers.bot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
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
            sendModeratorNotification(moderatorChat.getChatId(), userJoining, notificationText, keyboard);
         }
        log.debug("Finish notifying about user(id={}) joining", userJoining.getUserId());
    }

    @Transactional
    public void sendModeratorNotification(Long moderatorChatId, UserJoining userJoining, String notificationText, InlineKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage(moderatorChatId, notificationText)
                .replyMarkup(keyboard)
                .parseMode(ParseMode.Markdown);
        SendResponse response = telegramService.sendMessage(message);
        log.debug("ModerChat(id={}) notified about user(id={}) joining. Status={}",
                moderatorChatId, userJoining.getUserId(), response.isOk());

        UserJoiningNotification notification = new UserJoiningNotification();
        notification.setUserJoining(userJoining);
        notification.setSentMessageChatId(response.message().chat().id());
        notification.setSentMessageId(response.message().messageId());
        notification.setHasKeyboard(true);
        userJoiningNotificationService.save(notification);
        log.debug("Saved to DB notifying ModerChat(id={}) about user(id={}) joining",
                moderatorChatId, userJoining.getUserId());
    }

    @Transactional
    public void notifyUserAboutAnniversaryJoining(UserJoining userJoining) {
        log.debug("Start congratulate user(id={}, number={}) joining", userJoining.getUserId(), userJoining.getNumber());
        String notificationText = templateEngine
                .buildFromTemplate(notificationTemplates.getJoinCongratulation(), userJoining, false);

        SendMessage message = new SendMessage(userJoining.getChatId(), notificationText).parseMode(ParseMode.Markdown);
        SendResponse response = telegramService.sendMessage(message);
        log.debug("Finish congratulate user(id={}). Status={}", userJoining.getUserId(), response.isOk());
    }

    @Transactional
    public void deleteKeyboardFromJoiningNotification(Long userId, Long chatId, int anniversaryNumber) {
        log.debug("Start keyboard deleting about user(id={}, number={}) joining", userId, anniversaryNumber);
        userJoiningNotificationService
                .findByChatIdAndUserIdAndKeyboardStatus(chatId, userId, true)
                .forEach(this::deleteKeyboard);
        log.debug("Finish keyboard deleting about user(id={}, number={}) joining", userId, anniversaryNumber);
    }

    @Transactional
    public void deleteKeyboardFromAllJoiningNotifications(Long chatId, int anniversaryNumber) {
        log.debug("Start keyboard deleting about chat(id={}) {} joining", chatId, anniversaryNumber);
        userJoiningNotificationService
                .findByChatIdAndAnniversaryNumberAndKeyboardStatus(chatId, anniversaryNumber, true)
                .forEach(this::deleteKeyboard);
        log.debug("Finish keyboard deleting about chat(id={}) {} joining", chatId, anniversaryNumber);
    }

    @Transactional
    private void deleteKeyboard(UserJoiningNotification notification) {
        telegramService.removeKeyboardFromMessage(notification.getSentMessageChatId(), notification.getSentMessageId());
        notification.setHasKeyboard(false);
    }
}