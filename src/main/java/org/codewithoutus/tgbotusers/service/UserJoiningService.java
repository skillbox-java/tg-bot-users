package org.codewithoutus.tgbotusers.service;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.UserJoining;
import org.codewithoutus.tgbotusers.repository.UserJoiningRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserJoiningService {
    
    private final UserJoiningRepository userJoiningRepository;
    
//    public UserJoining save(UserJoining userJoining) {
//        return userJoiningRepository.save(userJoining);
//    }
//
//    public UserJoining findByUserIdAndChatId(Long userId, Long chatId) {
//        return userJoiningRepository.findByUserIdAndChatId(userId, chatId);
//    }
}