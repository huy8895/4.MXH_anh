package com.xem_vn.service;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Status;

public interface IPostService {
    Iterable<Post> getAllPost();
    Post getPostById(long id);
    Post save(Post post);
    void remove(Post post);
    Iterable<Post> getAllPostByUser(AppUser user);
    Iterable<Post> getAllPostByStatus(Status status);
}
