package org.codewithoutus.tgbotusers.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.ModeratorGroup;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "groups-settings")
public class GroupSettings {

    private Boolean rewriteDatabaseSettingsOnStartup;
    private List<Long> anniversaryNumbers;

    @Getter(AccessLevel.NONE)
    private Map<Long, List<Long>> moderatorGroupsData; // only used for loading from application-settings file

    @JsonIgnore
    private List<ModeratorGroup> moderatorGroups; // real group-settings, mapped to DB Entities

    @PostConstruct
    private void synchronizeDataBaseSettings() {
    }
}