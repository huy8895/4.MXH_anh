package com.xem_vn.service;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Like;
import com.xem_vn.model.Post;
import com.xem_vn.model.Vote;

public interface IVoteService {
    Iterable<Vote> findAllByPost(Post post);
    Iterable<Vote> findAllByUser(AppUser user);
    void save(Vote vote);
    void remove(Vote vote);
    boolean existsByAppUserAndAndPost(AppUser user,Post post);
    Long sumOfValues (Post post);
    Vote getByAppUserAndAndPost (AppUser user,Post post);
}
