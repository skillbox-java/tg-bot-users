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
public class ChatUser { // TODO: Pavel -- переименовать сущность и сопутствующие переменные (после задачи Макса)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NaturalId
    @Column(nullable = false, unique = true)
    private Long chatId;

    @ManyToMany(mappedBy = "chatUsers", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<ChatModerator> chatModerators;

    public void addChatModerator(ChatModerator chatModerator) {
        if (chatModerators == null) {
            chatModerators = new ArrayList<>();
        }
        chatModerators.add(chatModerator);
    }

    public ChatUser(Long chatId, List<ChatModerator> chatModerators) {
        this.chatId = chatId;
        this.chatModerators = chatModerators;
    }

    public ChatUser() {
    }


}