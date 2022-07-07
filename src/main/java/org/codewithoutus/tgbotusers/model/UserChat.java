package org.codewithoutus.tgbotusers.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class UserChat extends Chat {
    
    @ManyToMany(mappedBy = "userGroups")
    private List<ModeratorChat> moderatorChats;
}
