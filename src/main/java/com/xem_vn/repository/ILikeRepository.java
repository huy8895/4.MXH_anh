package com.xem_vn.repository;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Like;
import com.xem_vn.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface ILikeRepository extends CrudRepository<Like,Long> {
    Iterable<Like> findAllByPost(Post post);
    Iterable<Like> findAllByAppUser(AppUser user);
    boolean existsByAppUserAndAndPost(AppUser user,Post post);
    Long countAllByPost (Post post);
}
