package org.codewithoutus.tgbotusers.service;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatJoinRequestService {
    
    private final UserJoiningService userJoiningService;
    private final NotificationService notificationService;
    
    
    public void process(Map<String, ?> userData) {
        UserJoining userJoining = buildUserJoining(userData);
        
        userJoiningService.save(userJoining);
    
        notificationService.notifyModerators(userJoining);
    }
    
    private UserJoining buildUserJoining(Map<String, ?> userData) {
    
        UserJoining userJoining = new UserJoining();
    
        userJoining.setUserId((Long) userData.get("userId"));
        userJoining.setChatId((Long) userData.get("chatId"));
        userJoining.setNumber((Integer) userData.get("count"));
        Instant time = Instant.ofEpochSecond((Long) userData.get("data"));
        userJoining.setJoinTime(LocalDateTime.from(time));
        userJoining.setStatus(CongratulateStatus.WAIT);
        
        return userJoining;
    }
    
    
}
