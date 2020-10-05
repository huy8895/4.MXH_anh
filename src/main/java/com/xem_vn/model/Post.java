package com.xem_vn.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String photoName;

    @Transient
    private MultipartFile photo;

    private long viewCount;

    @ManyToOne
    private AppUser appUser;

    @ManyToOne
    private Status status;

    private Date dateUpload;
}
