package org.codewithoutus.tgbotusers.service;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.JoinedUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    
    
    public void notifyModerators(JoinedUser joinedUser) {
        createNotification(joinedUser);
        sendNotification();
    }
    
    private void createNotification(JoinedUser joinedUser) {
    
    }
    
    private void sendNotification() {
    
    }
}
