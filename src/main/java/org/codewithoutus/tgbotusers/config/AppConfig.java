package org.codewithoutus.tgbotusers.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.Bot;
import org.codewithoutus.tgbotusers.bot.BotService;
import org.codewithoutus.tgbotusers.handler.CallbackQueryHandler;
import org.codewithoutus.tgbotusers.handler.ChatJoinRequestHandler;
import org.codewithoutus.tgbotusers.handler.Handler;
import org.codewithoutus.tgbotusers.handler.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final BotSettings botSettings;
    private final BotService botService;

    private final CallbackQueryHandler callbackQueryHandler;
    private final ChatJoinRequestHandler chatJoinRequestHandler;
    private final MessageHandler messageHandler;

    @Bean
    public Bot telegramBot() {
        return new Bot(botSettings.getBotToken(), botService);
    }

    @Bean
    public List<Handler> telegramBotUpdateHandlers() {
        // order FROM more frequent & less resource-intensive -> TO less frequent & more resource-intensive
        return List.of(
                messageHandler,
                chatJoinRequestHandler,
                callbackQueryHandler);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}