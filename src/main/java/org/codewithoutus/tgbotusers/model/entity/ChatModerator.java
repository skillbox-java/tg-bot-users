package org.codewithoutus.tgbotusers.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ChatModerator {

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
    private List<ChatUser> chatUsers;

    public void addChatUser(ChatUser chatUser) {
        if (chatUsers == null) {
            chatUsers = new ArrayList<>();
        }
        chatUsers.add(chatUser);
    }
}