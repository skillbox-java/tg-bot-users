package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@ToString
@Getter
@Setter
@Component
//@Configuration
@ConfigurationProperties(prefix = "groups-settings")
public class GroupConfig {

    public List<Long> anniversaryNumbers;
    private Map<Long,List<Long>> moderatorGroups;
    private Map<Long,List<Long>> userGroups;


//    private List<ModeratorGroup> groups;    // 24-28 строки были тут
//    public record ModeratorGroup(int id, List<UserGroup> userGroups) {
//    }
//    public record UserGroup(int id) {
//    }


}

