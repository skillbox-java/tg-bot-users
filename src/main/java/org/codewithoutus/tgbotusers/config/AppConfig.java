package org.codewithoutus.tgbotusers.config;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "telegram")
public class AppConfig {
    
    private int longPollingTimeout;
    private int multiplicity;
    private int incrementalSaves;
    private String botUserName;
    private String botToken;
    
    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(botToken);
    }
    
}
