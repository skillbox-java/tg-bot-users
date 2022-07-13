package org.codewithoutus.tgbotusers.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserJoining {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long chatId;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private LocalDateTime joinTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CongratulateStatus status;
}