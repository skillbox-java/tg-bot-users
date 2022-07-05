package org.codewithoutus.tgbotusers.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "user_groups")
@Getter
@Setter
public class UserGroup extends Group {
    
    @ManyToMany(mappedBy = "userGroups")
    private Set<ModeratorGroup> moderatorsGroups;
}
