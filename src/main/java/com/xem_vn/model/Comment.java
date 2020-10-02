package com.xem_vn.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private AppUser appUser;

    @ManyToOne
    private Post post;
}
