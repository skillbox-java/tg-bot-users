package org.codewithoutus.tgbotusers.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class UserJoining implements Comparable<UserJoining> {

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
    private Integer anniversaryNumber;

    @Column(nullable = false)
    private LocalDateTime joinTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CongratulateStatus status;

    @OneToMany(mappedBy = "userJoining")
    @ToString.Exclude
    private List<UserJoiningNotification> notifications;
    
    
    @Override
    public int compareTo(@NotNull UserJoining o) {
        int result = number.compareTo(o.number);
        if (result == 0) {
            result = joinTime.compareTo(o.joinTime);
        }
        return result;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserJoining that = (UserJoining) o;
        
        if (!userId.equals(that.userId)) return false;
        return chatId.equals(that.chatId);
    }
    
    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + chatId.hashCode();
        return result;
    }
}