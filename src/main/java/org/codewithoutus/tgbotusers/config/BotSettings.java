package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "bot-settings")
public class BotSettings {

    public int longPollingTimeout;
    public String botToken;
    public String botUserName;
}