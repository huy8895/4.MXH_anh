package com.xem_vn.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Post post;

    @ManyToOne
    private AppUser appUser;

    private Long value;
}

