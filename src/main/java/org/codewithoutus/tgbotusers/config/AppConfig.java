package org.codewithoutus.tgbotusers.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegram")
public class AppConfig {
    
    private int longPollingTimeout;
    private int multiplicity;
    private int incrementalSaves;
    private String botUserName;
    private String botToken;
    
}
