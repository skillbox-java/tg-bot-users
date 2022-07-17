package org.codewithoutus.tgbotusers.model.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
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


    public Optional<ChatUser> findByChatId(long chatId) {
        return chatUserRepository.findByChatId(chatId);
    }


    public boolean isChatUser(long id) {
        return chatUserRepository.findByChatModeratorsNotEmpty()
                .stream()
                .anyMatch(chatUser -> chatUser.getChatId().equals(id));
    }
}