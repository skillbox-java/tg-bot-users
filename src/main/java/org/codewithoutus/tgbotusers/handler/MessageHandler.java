package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageHandler implements Handler {

    @Override
    public boolean handle(Update update) {
//        messageService.process(messageData);
        return false;
    }
}
