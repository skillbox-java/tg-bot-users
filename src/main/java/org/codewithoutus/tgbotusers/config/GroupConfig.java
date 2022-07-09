package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.ModeratorChat;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegram")
public class  GroupConfig {
    
    private List<ModeratorChat> moderatorChats;

}
