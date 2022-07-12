package org.codewithoutus.tgbotusers.bot.service;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.service.UserJoiningService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatJoinRequestService {
    
    private final UserJoiningService userJoiningService;
    private final NotificationService notificationService;

//    public void process(Map<String, ?> userData) {
//        UserJoining userJoining = buildUserJoining(userData);
//        userJoiningService.save(userJoining);
//        notificationService.notifyModerators(userJoining);
//    }

//    private UserJoining buildUserJoining(Map<String, ?> userData) {
//
//        UserJoining userJoining = new UserJoining();
//
//        userJoining.setUserId((Long) userData.get("userId"));
//        userJoining.setChatId((Long) userData.get("chatId"));
//        userJoining.setNumber((Integer) userData.get("count"));
//        Instant time = Instant.ofEpochSecond((Long) userData.get("data"));
//        userJoining.setJoinTime(LocalDateTime.from(time));
//        userJoining.setStatus(CongratulateStatus.WAIT);
//
//        return userJoining;
//    }
}