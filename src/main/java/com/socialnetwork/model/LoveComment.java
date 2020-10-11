package com.socialnetwork.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "lovescomment")
public class LoveComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    private Comment comment;
    @ManyToOne
    private AppUser appUser;



}
