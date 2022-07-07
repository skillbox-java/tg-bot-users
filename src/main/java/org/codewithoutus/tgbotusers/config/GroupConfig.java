package org.codewithoutus.tgbotusers.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegram.groups")
public class  GroupConfig {
    private List<ModeratorGroup> groups;
    
    
    @Getter
    @Setter
    public class ModeratorGroup {
        private int id;
        private List<UserGroup> userGroups;
    }
    
    @Getter
    @Setter
    public class UserGroup {
        private int id;
    }
}
