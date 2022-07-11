package org.codewithoutus.tgbotusers.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class ModeratorChat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NaturalId
    @Column(nullable = false, unique = true)
    private Long chatId;
    
    @ManyToMany
    @JoinTable(name = "moderators2users",
            joinColumns = @JoinColumn(name = "moderator_chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_chat_id"))
    private List<UserChat> userChats;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ModeratorChat that = (ModeratorChat) o;
    
        return chatId.equals(that.chatId);
    }
    
    @Override
    public int hashCode() {
        return chatId.hashCode();
    }
}
