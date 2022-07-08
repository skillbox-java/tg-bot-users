package org.codewithoutus.tgbotusers.repository;

import org.codewithoutus.tgbotusers.model.ModeratorChat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeratorChatRepository extends CrudRepository<ModeratorChat, Integer> {
}
