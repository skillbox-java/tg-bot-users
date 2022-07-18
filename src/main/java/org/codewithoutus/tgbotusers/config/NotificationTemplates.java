package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "message-templates")
public class NotificationTemplates {

    private Map<String, String> variables;
    private Map<String, String> plugs;
    private DateTimeFormatter dateTimeFormatter;

    private String joinCongratulation;
    private String joinAlert;
    private String joinUserInfo;

    @PostConstruct
    private void postConstruct() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(plugs.get("join-date-format"));
    }
}