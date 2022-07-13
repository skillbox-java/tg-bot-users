package org.codewithoutus.tgbotusers.model.service;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.repository.UserJoiningRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserJoiningService {

    private final UserJoiningRepository userJoiningRepository;

    public UserJoining save(UserJoining userJoining) {
        return userJoiningRepository.save(userJoining);
    }

    public UserJoining findByUserIdAndChatId(Long userId, Long chatId) {
        return userJoiningRepository.findByUserIdAndChatId(userId, chatId);
    }

    public List<UserJoining> findByChatId(Long chatId) {
        return userJoiningRepository.findByChatId(chatId);
    }
}