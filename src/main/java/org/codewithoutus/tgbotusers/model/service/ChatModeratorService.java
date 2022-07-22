package org.codewithoutus.tgbotusers.model.service;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.repository.ChatModeratorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatModeratorService {

    private final ChatModeratorRepository chatModeratorRepository;

    public void deleteAll() {
        chatModeratorRepository.deleteAll();
    }

    public void deleteById(Integer id) {
        chatModeratorRepository.deleteById(id);
    }

    public ChatModerator save(ChatModerator entity) {
        return chatModeratorRepository.save(entity);
    }

    public List<ChatModerator> findAll() {
        List<ChatModerator> result = new ArrayList<>();
        chatModeratorRepository.findAll().forEach(result::add);
        return result;
    }

    public Optional<ChatModerator> findByChatId(Long chatId) {
        return chatModeratorRepository.findByChatId(chatId);
    }

    public List<ChatModerator> findByChatUsersId(Long chatId) {
        return chatModeratorRepository.findByChatUsers_ChatId(chatId);
    }

    public boolean existsByChatId(Long chatId) {
        return chatModeratorRepository.existsByChatId(chatId);
    }
}