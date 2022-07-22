package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot-settings")
public class BotSettings {

    private int longPollingTimeout;
    private String botToken;
    private String botUserName;
}