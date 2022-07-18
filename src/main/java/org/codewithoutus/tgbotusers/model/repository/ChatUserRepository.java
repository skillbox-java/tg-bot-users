package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatUserRepository extends CrudRepository<ChatUser, Integer> {
    
//    boolean existsByName(String name);

    Optional<ChatUser> findByChatId(long chatId);
    
//    Optional<ChatUser> findByName(String name);

    List<ChatUser> findByChatModeratorsNotEmpty();
}