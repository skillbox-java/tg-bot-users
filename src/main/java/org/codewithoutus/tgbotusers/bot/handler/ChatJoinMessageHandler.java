package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.UpdateUtils;
import org.codewithoutus.tgbotusers.bot.service.NotificationService;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.config.ChatSettings;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.codewithoutus.tgbotusers.model.service.ChatUserService;
import org.codewithoutus.tgbotusers.model.service.UserJoiningService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class ChatJoinMessageHandler extends Handler {

    private final ChatSettings chatSettings;
    private final ChatUserService chatUserService;
    private final TelegramService telegramService;
    private final NotificationService notificationService;
    private final UserJoiningService userJoiningService;

    @Override
    protected boolean handle(Update update) {
        User[] users = UpdateUtils.getNewChatMembers(update);
        if (users == null || users.length == 0) {
            return false;
        }
        long chatId = UpdateUtils.getChat(update).id();
        if (!chatUserService.existByChatId(chatId)) {
            return false;
        }

        Instant instant = Instant.ofEpochSecond(update.message().date());
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        int chatNumbersCount = telegramService.getChatMembersCount(chatId);

        for (int i = 0; i < users.length; i++) {
            User user = users[i];
            int joinNumber = chatNumbersCount - users.length + 1 + i;
            int anniversaryNumber = chatSettings.getAnniversaryJoinNumber(chatId, joinNumber);
            if (anniversaryNumber == 0 || userJoiningService.userWasAlreadyJoinedToChat(chatId, user.id())) {
                return false;
            }

            UserJoining userJoining = new UserJoining();
            userJoining.setChatId(chatId);
            userJoining.setUserId(user.id());
            userJoining.setJoinTime(dateTime);
            userJoining.setNumber(joinNumber);
            userJoining.setAnniversaryNumber(anniversaryNumber);
            userJoining.setStatus(CongratulateStatus.WAIT);
            userJoining = userJoiningService.save(userJoining);

            // если не было поздравненных в чате с таким порядковым номером
            if (!userJoiningService.existCongratulatedUser(chatId, anniversaryNumber)) {
                notificationService.notifyModeratorsAboutUserJoining(userJoining);
            }
        }
        return true;
    }
}
