package org.codewithoutus.tgbotusers.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.service.BotService;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppListener {

    private final BotService botService;

    @EventListener
    public void handleContextStartedEvent(ContextStartedEvent ctxStartEvt) {
        botService.start();
    }
}
