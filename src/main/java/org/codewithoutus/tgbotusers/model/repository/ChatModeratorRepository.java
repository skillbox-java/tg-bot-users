package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatModeratorRepository extends CrudRepository<ChatModerator, Integer> {

    List<ChatModerator> findByChatUsers_ChatId(Long chatId);

    @Override
    Optional<ChatModerator> findById(Integer integer);


    boolean existsByChatId(Long chatId);

    Optional<ChatModerator>findByChatId(Long chatId);
}