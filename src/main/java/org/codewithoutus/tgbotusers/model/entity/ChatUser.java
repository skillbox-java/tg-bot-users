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
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NaturalId
    @Column(nullable = false, unique = true)
    private Long chatId;

    @ManyToMany(mappedBy = "chatUsers", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<ChatModerator> chatModerators = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUser that = (ChatUser) o;
        return chatId.equals(that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}