package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "message-templates")
public class NotificationTemplates {

    private String joinCongratulation;
    private String joinAlert;
    private String joinUserInfo;

    @PostConstruct
    private void formatTemplates() {
        // TODO: Алекс -- сделать шаблоны
//        joinCongratulation = joinCongratulation.replaceAll("{\*}", "%s");
    }
}