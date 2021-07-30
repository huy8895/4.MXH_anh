package com.socialnetwork.service;

import com.socialnetwork.model.AppUser;
import com.socialnetwork.model.Post;
import com.socialnetwork.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPostService {
    Page<Post> getAllPost(Pageable pageable);
    Post getPostById(long id);
    Post save(Post post);
    void remove(Post post);
    Page<Post> getAllPostByUser(AppUser user, Pageable pageable);
    Page<Post> getAllPostByStatus(Status status,Pageable pageable);
    void setStatusForPost(Status status,Long postID);

    Page<Post> findAllPostByUserLiked(Long appUserId, Pageable pageable);
    List<Long> getAllPostIdByUserLiked(Long userId);
}
