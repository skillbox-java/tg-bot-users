package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codewithoutus.tgbotusers.model.Group;
import org.codewithoutus.tgbotusers.model.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@ToString
@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "bot-settings")
public class BotConfig {

    public int longPollingTimeout;
    public String botToken;
    public String botName;

}
