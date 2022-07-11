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
@ConfigurationProperties(prefix = "bot-setting")
public class BotConfig {

    public Map<Group,Group> listModerGroups;
    public Map<User,Group> listUserGroups;

}
