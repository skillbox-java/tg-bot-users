package org.codewithoutus.tgbotusers.model.service;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.repository.ChatModeratorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatModeratorService {

    private final ChatModeratorRepository chatModeratorRepository;

    public void deleteAll() {
        chatModeratorRepository.deleteAll();
    }

    public ChatModerator save(ChatModerator entity) {
        return chatModeratorRepository.save(entity);
    }

    public List<ChatModerator> findAll() {
        List<ChatModerator> result = new ArrayList<>();
        chatModeratorRepository.findAll().forEach(result::add);
        return result;
    }

    public List<ChatModerator> findByChatUsersId(Long chatId) {
        return chatModeratorRepository.findByChatUsers_ChatId(chatId);
    }

    public ChatModeratorRepository getChatModeratorRepository() {
        return chatModeratorRepository;
    }
    public boolean findByIDModerChatInDatabase(Long chatId){
        return chatModeratorRepository.existsByChatId(chatId);
    }


}