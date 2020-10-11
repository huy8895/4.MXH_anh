package com.xem_vn.service;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPostService {
    Page<Post> getAllPost(Pageable pageable);
    Post getPostById(long id);
    Post save(Post post);
    void remove(Post post);
    Page<Post> getAllPostByUser(AppUser user,Pageable pageable);
    Page<Post> getAllPostByStatus(Status status,Pageable pageable);
    void setStatusForPost(Long statusID,Long postID);

    Page<Post> findAllPostByUserLiked(Long appUserId, Pageable pageable);
    List<Long> getAllPostIdByUserLiked(Long userId);
}
