package org.codewithoutus.tgbotusers.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class ModeratorChat extends Chat {
    
    @ManyToMany
    @JoinTable(name = "moderators2users",
            joinColumns = @JoinColumn(name = "moderator_chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_chat_id"))
    private List<UserChat> userGroups;
}
