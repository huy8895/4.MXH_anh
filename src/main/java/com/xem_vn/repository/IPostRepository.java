package com.xem_vn.repository;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IPostRepository extends PagingAndSortingRepository<Post,Long> {
    Page<Post> getAllByAppUser(AppUser appUser, Pageable pageable);

    Page<Post> getAllByStatus(Status status, Pageable pageable);

    @Query(value = "update post p set p.status_id = ?1 where p.id = ?2",nativeQuery = true)
    void setStatusForPost(Long statusID,Long postID);

    Page<Post> getAllByAppUserLike(AppUser appUser, Pageable pageable); //tra ve cac post da like

//    @Query("select * from Post and ")
//    Page<>



//    @Query(value = "UPDATE post SET status WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
//    void updatePostStatus()
}
