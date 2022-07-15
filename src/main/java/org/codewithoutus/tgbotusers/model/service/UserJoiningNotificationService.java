package org.codewithoutus.tgbotusers.model.service;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.entity.UserJoiningNotification;
import org.codewithoutus.tgbotusers.model.repository.UserJoiningNotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserJoiningNotificationService {

    private final UserJoiningNotificationRepository userJoiningNotificationRepository;

    public UserJoiningNotification save(UserJoiningNotification entity) {
        return userJoiningNotificationRepository.save(entity);
    }

    public List<UserJoiningNotification> findByChatIdAndAnniversaryNumber(Long chatId, Integer anniversaryNumber) {
        return userJoiningNotificationRepository.findByUserJoining_ChatIdAndUserJoining_AnniversaryNumber(chatId, anniversaryNumber);
    }

    public List<UserJoiningNotification> findByUserIdAndChatIdAndAnniversaryNumber(Long userId, Long chatId, Integer anniversaryNumber) {
        return userJoiningNotificationRepository.findByUserJoining_UserIdAndUserJoining_ChatIdAndUserJoining_AnniversaryNumber(userId, chatId, anniversaryNumber);
    }
}