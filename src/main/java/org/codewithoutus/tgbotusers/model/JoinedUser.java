package org.codewithoutus.tgbotusers.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class JoinedUser extends User {
    
    @Column(nullable = false)
    private Long chatId;
    
    @Column(nullable = false)
    private Integer number;
    
    @Column(nullable = false)
    private LocalDateTime joinTime;
}