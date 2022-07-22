package org.codewithoutus.tgbotusers.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ChatModerator { // TODO: Pavel -- переименовать сущность и сопутствующие переменные (после задачи Макса)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NaturalId
    @Column(nullable = false, unique = true)
    private Long chatId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "moderators2users",
            joinColumns = @JoinColumn(name = "moderator_chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_chat_id"))
    private List<ChatUser> chatUsers = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatModerator that = (ChatModerator) o;
        return chatId.equals(that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}