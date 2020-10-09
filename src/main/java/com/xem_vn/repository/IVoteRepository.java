package com.xem_vn.repository;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Vote;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IVoteRepository extends CrudRepository<Vote,Long> {
    Iterable<Vote> findAllByPost(Post post);
    Iterable<Vote> findAllByAppUser(AppUser user);
    boolean existsByAppUserAndAndPost(AppUser user,Post post);

    @Query(value = "select sum(value) from votes where post_id = ?1", nativeQuery = true)
    Long sumOfValues (Long postId);

    Vote getByAppUserAndAndPost (AppUser user,Post post);

//    @Value(value = "selec")
//    @Query(value = "select * from post where post.id in (select likes.post_id from likes where likes.app_user_id = ?1)", nativeQuery = true)
//    Page<Post> findAllPostByUserLiked(Long appUserId, Pageable pageable);
}
