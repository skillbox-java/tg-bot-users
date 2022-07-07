package org.codewithoutus.tgbotusers.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
public class Congratulation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "joined_user_id", nullable = false, unique = true)
    private JoinedUser joinedUser;

}