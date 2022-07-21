package org.codewithoutus.tgbotusers.model.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.codewithoutus.tgbotusers.model.repository.ChatUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void deleteById(Integer id) {
        chatUserRepository.deleteById(id);
    }

    public ChatUser save(ChatUser entity) {
        return chatUserRepository.save(entity);
    }

    public List<ChatUser> findAll() {
        return (List<ChatUser>) chatUserRepository.findAll();
    }
    
    public Optional<ChatUser> findByChatId(long chatId) {
        return chatUserRepository.findByChatId(chatId);
    }

    public boolean existByChatId(long chatId) {
        return chatUserRepository.existsByChatId(chatId);
    }
}