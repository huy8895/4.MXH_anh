package com.xem_vn.repository;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPostRepository extends PagingAndSortingRepository<Post, Long> {
    Page<Post> getAllByAppUser(AppUser appUser, Pageable pageable);

    Page<Post> getAllByStatus(Status status, Pageable pageable);

    @Query(value = "update post p set p.status_id = ?1 where p.id = ?2", nativeQuery = true)
    void setStatusForPost(Long statusID, Long postID);

    @Query(value = "select * from post where post.id in (select likes.post_id from likes where likes.app_user_id = ?1)", nativeQuery = true)
    Page<Post> findAllPostByUserLiked(Long appUserId, Pageable pageable);

    @Query(value = "select post.id from post where post.id in (select likes.post_id from likes where likes.app_user_id = ?1)", nativeQuery = true)
    List<Long> getAllPostIdByUserLiked(Long userId);

//    @Query("select * from Post and ")
//    Page<>

//    @Query("select * from person where first_name=:firstName")
//    List<Person> findByFirstName(@Param("firstName") String firstName);

//    @Query(value = "UPDATE post SET status WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
//    void updatePostStatus()
}
