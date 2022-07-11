package org.codewithoutus.tgbotusers.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.UserChat;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class UserChatService {
    
    private Map<Long, UserChat> userChats;
}
