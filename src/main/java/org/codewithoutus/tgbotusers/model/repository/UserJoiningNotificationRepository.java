package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.UserJoiningNotification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJoiningNotificationRepository extends CrudRepository<UserJoiningNotification, Integer> {

    List<UserJoiningNotification> findByUserJoining_ChatIdAndUserJoining_AnniversaryNumberAndHasKeyboard(Long chatId, Integer anniversaryNumber, boolean hasKeyboard);

    List<UserJoiningNotification> findByUserJoining_ChatIdAndUserJoining_UserIdAndHasKeyboard(Long chatId, Long userId, boolean hasKeyboard);
}