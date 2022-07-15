package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.ChatJoinRequest;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.service.NotificationService;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.config.ChatSettings;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.service.ChatUserService;
import org.codewithoutus.tgbotusers.model.service.UserJoiningService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChatJoinRequestHandler extends Handler {

    private final ChatSettings chatSettings;
    private final ChatUserService chatUserService;
    private final TelegramService telegramService;
    private final NotificationService notificationService;
    private final UserJoiningService userJoiningService;

    @Override
    protected boolean handle(Update update) {
        ChatJoinRequest chatJoinRequest = update.chatJoinRequest();
        if (chatJoinRequest == null) {
            return false;
        }

        // TODO: Pavel (подумать) -- если присоединяется сразу несколько в чат (на будущее)
        // TODO: Pavel (подумать) -- если присоединяющийся пользователь бот и юбилейный (на далёкое будущее)
        // TODO: Pavel (подумать) -- юбилейные номера могут быть разные для каждого чата (на далёкое будущее)

        User user = chatJoinRequest.from();
        long chatId = chatJoinRequest.chat().id();
        int joinNumber = telegramService.getChatMembersCount(chatId);
        int anniversaryNumber = chatSettings.getAnniversaryJoinNumber(chatId, joinNumber);
        if (anniversaryNumber == 0 || !chatUserService.isChatUser(chatId)) {
            return false;
        }

        UserJoining userJoining = new UserJoining();
        userJoining.setChatId(chatId);
        userJoining.setUserId(user.id());
        userJoining.setNumber(joinNumber);
        userJoining.setJoinTime(LocalDateTime.from(Instant.ofEpochSecond(chatJoinRequest.date())));
        userJoining = userJoiningService.save(userJoining);

        if (!userJoiningService.existCongratulatedUser(chatId, anniversaryNumber)) {
            notificationService.notifyModeratorsAboutUserJoining(userJoining);
        }
        return true;
    }
}