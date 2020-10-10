package com.xem_vn.repository;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Like;
import com.xem_vn.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ILikeRepository extends CrudRepository<Like,Long> {
    Iterable<Like> findAllByPost(Post post);
    Iterable<Like> findAllByAppUser(AppUser user);
    boolean existsByAppUserAndPost(AppUser user,Post post);
    Long countAllByPost (Post post);
    Like getByAppUserAndPost (AppUser user,Post post);
    @Query(value = "select app_user_id from likes",nativeQuery = true)
    List<Long> getListLikedUserIds();
}
