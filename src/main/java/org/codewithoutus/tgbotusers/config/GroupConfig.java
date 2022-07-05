package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "telegram.groups")
public class GroupConfig {
    private List<ModeratorGroup> groups;
    
    
    public record ModeratorGroup(int id, List<UserGroup> userGroups) {
    }
    
    public record UserGroup(int id) {
    }
}
