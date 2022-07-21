package org.codewithoutus.tgbotusers.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class UserJoiningNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer sentMessageId;

    @Column(nullable = false)
    private Long sentMessageChatId;

    @Column
    private boolean hasKeyboard;

    @ManyToOne
    private UserJoining userJoining;
}