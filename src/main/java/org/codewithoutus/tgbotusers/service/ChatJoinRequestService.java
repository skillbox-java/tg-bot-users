package org.codewithoutus.tgbotusers.service;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.handler.ChatJoinRequestHandler;
import org.codewithoutus.tgbotusers.model.JoinedUser;
import org.codewithoutus.tgbotusers.repository.JoinedUserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatJoinRequestService implements TelegramEntityService {
    
    private final ChatJoinRequestHandler chatJoinRequestHandler;
    private final JoinedUserService joinedUserService;
    private final NotificationService notificationService;
    

    @Override
    public void process(Update update) {
        Map<String, ?> userData = chatJoinRequestHandler.handle(update);
        if (userData.isEmpty()) {
            return;
        }
        
        JoinedUser joinedUser = buildJoinedUser(userData);
        joinedUserService.save(joinedUser);
    
        notificationService.notifyModerators(joinedUser);
    }
    
    private JoinedUser buildJoinedUser(Map<String, ?> userData) {
        
        JoinedUser joinedUser = new JoinedUser();
        
        joinedUser.setUserId((Long) userData.get("userId"));
        joinedUser.setChatId((Long) userData.get("chatId"));
        joinedUser.setNumber((Integer) userData.get("count"));
        Instant time = Instant.ofEpochSecond((Long) userData.get("data"));
        joinedUser.setJoinTime(LocalDateTime.from(time));
        
        return joinedUser;
    }
    
    
}
