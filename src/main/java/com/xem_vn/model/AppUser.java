package com.xem_vn.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true,columnDefinition = "varchar(50)")
    private String username;

    private String fullName;

    private String password;

    @ManyToOne
    private AppRole role;

    @Transient
    private MultipartFile avatarFile;

    private String avatarFileName;

}
