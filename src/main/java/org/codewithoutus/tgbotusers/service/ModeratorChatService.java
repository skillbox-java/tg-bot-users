package org.codewithoutus.tgbotusers.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.ModeratorChat;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ModeratorChatService {
    
    private Map<Long, ModeratorChat> moderatorChats;
    
    
}
