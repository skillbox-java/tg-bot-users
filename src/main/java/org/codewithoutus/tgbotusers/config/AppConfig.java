package org.codewithoutus.tgbotusers.config;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final BotSettings botSettings;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(botSettings.getBotToken());
    }
}