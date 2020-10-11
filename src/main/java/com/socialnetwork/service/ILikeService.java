package com.socialnetwork.service;

import com.socialnetwork.model.AppUser;
import com.socialnetwork.model.Like;
import com.socialnetwork.model.Post;

public interface ILikeService {
    Iterable<Like> findAllByPost(Post post);
    Iterable<Like> findAllByUser(AppUser user);
    void save(Like like);
    void remove(Like like);
    boolean existsByAppUserAndPost(AppUser user,Post post);
    Long countAllByPost (Post post);
    Like getByAppUserAndPost (AppUser user,Post post);
}
