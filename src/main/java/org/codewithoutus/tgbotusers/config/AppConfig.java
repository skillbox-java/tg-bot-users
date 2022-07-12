package org.codewithoutus.tgbotusers.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.Bot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final BotSettings botSettings;

    @Bean
    public Bot telegramBot() {
        return new Bot(botSettings.getBotToken(), botSettings.getLongPollingTimeout());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}