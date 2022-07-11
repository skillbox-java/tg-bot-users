package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "message-templates")
public class NotificationTemplates {

    private String joinCongratulation;
    private String joinAlert;
    private String joinUserInfo;
}