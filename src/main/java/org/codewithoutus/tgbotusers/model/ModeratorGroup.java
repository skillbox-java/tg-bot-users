package org.codewithoutus.tgbotusers.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "user_groups")
@Getter
@Setter
public class ModeratorGroup extends Group {
    
    @ManyToMany
    @JoinTable(name = "moderators2users",
            joinColumns = {@JoinColumn(name = "moderator_group_id", referencedColumnName = "id", columnDefinition="INT NOT NULL")},
            inverseJoinColumns = {@JoinColumn(name = "user_group_id", referencedColumnName = "id", columnDefinition="INT NOT NULL")})
    private Set<UserGroup> userGroups;
}
