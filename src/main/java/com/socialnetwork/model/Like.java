package com.socialnetwork.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Post post;

    @ManyToOne
    private AppUser appUser;
}
