package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatUserRepository extends CrudRepository<ChatUser, Integer> {

    ChatUser findByChatId(long chatId);

    List<ChatUser> findByChatModeratorsNotEmpty();

    boolean existsByChatId(Long chatId);

    Optional<ChatUser>findByChatId(Long chatId);
}