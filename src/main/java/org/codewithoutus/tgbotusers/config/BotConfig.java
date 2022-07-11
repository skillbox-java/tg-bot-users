package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.Group;
import org.codewithoutus.tgbotusers.model.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "bot-setting.bot-setting")
public class BotConfig {

    public int longPollingTimeout;
    public String botToken;
    public String botName;

}
