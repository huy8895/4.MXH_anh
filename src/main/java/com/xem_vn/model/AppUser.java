package com.xem_vn.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Pattern(regexp="^[A-Za-z]*$")
    private long id;

    private String username;

    private String password;

    @ManyToOne
    private AppRole role;

    @Transient
    private MultipartFile avatarFile;

    private String avatarFileName;

}
