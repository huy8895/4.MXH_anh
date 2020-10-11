package com.socialnetwork.repository;

import com.socialnetwork.model.AppUser;
import com.socialnetwork.model.Like;
import com.socialnetwork.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface ILikeRepository extends CrudRepository<Like,Long> {
    Iterable<Like> findAllByPost(Post post);
    Iterable<Like> findAllByAppUser(AppUser user);
    boolean existsByAppUserAndPost(AppUser user,Post post);
    Long countAllByPost (Post post);
    Like getByAppUserAndPost (AppUser user,Post post);
}
