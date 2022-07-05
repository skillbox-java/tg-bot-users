package org.codewithoutus.tgbotusers.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "joined_users")
@Getter
@Setter
public class JoinedUser extends User {
    
    @Column(name = "chat_id", columnDefinition = "INT NOT NULL")
    private Integer chatId;
    
    @Column(name = "number", columnDefinition = "INT NOT NULL")
    private Integer number;
}