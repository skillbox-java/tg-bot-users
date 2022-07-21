package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatModeratorRepository extends CrudRepository<ChatModerator, Integer> {

    Optional<ChatModerator>findByChatId(Long chatId);

    List<ChatModerator> findByChatUsers_ChatId(Long chatId);

    boolean existsByChatId(Long chatId);
}