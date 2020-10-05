package com.xem_vn.service;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Like;
import com.xem_vn.model.Post;

public interface ILikeService {
    Iterable<Like> findAllByPost(Post post);
    Iterable<Like> findAllByUser(AppUser user);
    void save(Like like);
    void remove(Like like);

}
