package org.codewithoutus.tgbotusers.config;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BotConfig {
    
    private final AppConfig appConfig;
    
    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(appConfig.getBotToken());
    }
}
