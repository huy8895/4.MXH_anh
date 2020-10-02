package com.xem_vn.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "status")
@Data
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long name;
}
