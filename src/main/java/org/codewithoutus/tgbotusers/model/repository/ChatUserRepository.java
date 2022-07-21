package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatUserRepository extends CrudRepository<ChatUser, Integer> {

    Optional<ChatUser> findByChatId(long chatId);

    boolean existsByChatId(Long chatId);
}