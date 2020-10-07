package com.xem_vn.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String photoName;

    @Transient
    private MultipartFile photo;

    private long likeCount;
    private long commentCount;

    @ManyToOne
    private AppUser appUser;

    @ManyToOne
    private Status status;

    @Column(name = "date_Upload")
    private Date dateUpload;
}
