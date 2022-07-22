package org.codewithoutus.tgbotusers.model.service;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.codewithoutus.tgbotusers.model.repository.UserJoiningRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserJoiningService {

    private final UserJoiningRepository userJoiningRepository;

    public UserJoining save(UserJoining userJoining) {
        return userJoiningRepository.save(userJoining);
    }

    public Optional<UserJoining> findById(int id) {
        return userJoiningRepository.findById(id);
    }

    public List<UserJoining> findByChatIds(List<Long> chatIds) {
        return userJoiningRepository.findDistinctByChatIdInOrderByChatIdAscNumberAsc(chatIds);
    }

    public List<UserJoining> findNotCongratulatedByChatIds(List<Long> chatIds) {
        return userJoiningRepository.findByChatIdAndNotStatus(chatIds, CongratulateStatus.CONGRATULATE);
    }

    public boolean existCongratulatedUser(long chatId, int anniversaryNumber) {
        return userJoiningRepository.existsByChatIdAndAnniversaryNumberAndStatus(chatId, anniversaryNumber, CongratulateStatus.CONGRATULATE);
    }

    public boolean userWasAlreadyJoinedToChat(Long chatId, Long userId) {
        return userJoiningRepository.existsByChatIdAndUserId(chatId, userId);
    }
}
