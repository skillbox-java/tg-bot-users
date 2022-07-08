package org.codewithoutus.tgbotusers.service;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateService {
    
    private final ChatJoinRequestService chatJoinRequestService;
    private final CallbackQueryService callbackQueryService;
    private final MessageService messageService;
    
    
    public void process(Update update) {
        chatJoinRequestService.process(update);
        callbackQueryService.process(update);
        messageService.process(update);
    }
    
}
