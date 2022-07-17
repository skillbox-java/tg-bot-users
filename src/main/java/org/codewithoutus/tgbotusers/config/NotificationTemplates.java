package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import org.codewithoutus.tgbotusers.bot.enums.TemplatePattern;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "message-templates")
public class NotificationTemplates {

    private String joinCongratulation;
    private String joinAlert;
    private String joinUserInfo;
    
}