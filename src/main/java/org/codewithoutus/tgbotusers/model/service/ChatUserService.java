package org.codewithoutus.tgbotusers.model.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.codewithoutus.tgbotusers.model.repository.ChatUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final ChatUserRepository chatUserRepository;

    public void deleteAll() {
        chatUserRepository.deleteAll();
    }
    
    public boolean existsByName(String name) {
        return chatUserRepository.existsByName(name);
    }

    public ChatUser save(ChatUser entity) {
        return chatUserRepository.save(entity);
    }

    public ChatUser findByChatId(long chatId) {
        return chatUserRepository.findByChatId(chatId);
    }

    public Optional<ChatUser> findByName(String name) {
        return chatUserRepository.findByName(name);
    }
    
    public boolean isChatUser(long id) {
        return chatUserRepository.findByChatModeratorsNotEmpty()
                .stream()
                .anyMatch(chatUser -> chatUser.getChatId().equals(id));
    }
}