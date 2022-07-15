package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.UserJoiningNotification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJoiningNotificationRepository extends CrudRepository<UserJoiningNotification, Integer> {

    List<UserJoiningNotification> findByUserJoining_ChatIdAndUserJoining_AnniversaryNumber(Long chatId, Integer anniversaryNumber);

    List<UserJoiningNotification> findByUserJoining_UserIdAndUserJoining_ChatIdAndUserJoining_AnniversaryNumber(Long userId, Long chatId, Integer anniversaryNumber);
}