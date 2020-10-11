package com.socialnetwork.service;

import com.socialnetwork.model.AppUser;
import com.socialnetwork.model.Post;
import com.socialnetwork.model.Vote;

public interface IVoteService {
    Iterable<Vote> findAllByPost(Post post);
    Iterable<Vote> findAllByUser(AppUser user);
    void save(Vote vote);
    void remove(Vote vote);
    boolean existsByAppUserAndAndPost(AppUser user,Post post);
    Long sumOfValues (Post post);
    Vote getByAppUserAndAndPost (AppUser user,Post post);
}
