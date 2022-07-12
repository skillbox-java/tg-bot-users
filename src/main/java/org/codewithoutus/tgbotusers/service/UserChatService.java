package org.codewithoutus.tgbotusers.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.UserChat;
import org.codewithoutus.tgbotusers.repository.UserChatRepository;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class UserChatService {

    private final UserChatRepository userChatRepository;

    public UserChat findByName(String name) {
        return userChatRepository.findByName(name);
    }
}
