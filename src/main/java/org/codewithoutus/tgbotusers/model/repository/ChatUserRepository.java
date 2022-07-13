package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatUserRepository extends CrudRepository<ChatUser, Integer> {

    ChatUser findByChatId(long chatId);

    List<ChatUser> findByChatModeratorsNotEmpty();
}