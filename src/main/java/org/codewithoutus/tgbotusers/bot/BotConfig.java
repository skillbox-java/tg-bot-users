package org.codewithoutus.tgbotusers.bot;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.Bot;
import org.codewithoutus.tgbotusers.handler.CallbackQueryHandler;
import org.codewithoutus.tgbotusers.handler.ChatJoinRequestHandler;
import org.codewithoutus.tgbotusers.handler.Handler;
import org.codewithoutus.tgbotusers.handler.PrivateMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BotConfig {

    private final Bot bot;

    private final CallbackQueryHandler callbackQueryHandler;
    private final ChatJoinRequestHandler chatJoinRequestHandler;
    private final PrivateMessageHandler privateMessageHandler;

    @PostConstruct
    private void botStart() {
        List<Handler> updateHandlers = List.of(
                privateMessageHandler,
                chatJoinRequestHandler,
                callbackQueryHandler);

        bot.setUpdateHandlers(updateHandlers);
        bot.start();
    }
}