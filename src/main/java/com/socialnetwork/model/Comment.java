package com.socialnetwork.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String content;
    private Long loveCount;

    @ManyToOne
    private AppUser appUser;

    @ManyToOne
    private Post post;

    private Date timeComment;
}
