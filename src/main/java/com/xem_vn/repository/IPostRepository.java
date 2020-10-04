package com.xem_vn.repository;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IPostRepository extends PagingAndSortingRepository<Post,Long> {
    Page<Post> getAllByAppUser(AppUser appUser, Pageable pageable);

    Page<Post> getAllByStatus(Status status, Pageable pageable);



//    @Query(value = "UPDATE post SET status WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
//    void updatePostStatus()
}
