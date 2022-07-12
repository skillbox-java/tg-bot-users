package org.codewithoutus.tgbotusers.repository;

import org.codewithoutus.tgbotusers.model.UserJoining;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJoiningRepository extends CrudRepository<UserJoining, Integer> {
    
    UserJoining findByUserIdAndChatId(Long userId, Long chatId);
    List<UserJoining> findByChatId(Long chatId);
}

