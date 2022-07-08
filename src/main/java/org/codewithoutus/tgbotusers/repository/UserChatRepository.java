package org.codewithoutus.tgbotusers.repository;

import org.codewithoutus.tgbotusers.model.UserChat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRepository extends CrudRepository<UserChat, Integer> {
}
