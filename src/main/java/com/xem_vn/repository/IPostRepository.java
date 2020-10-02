package com.xem_vn.repository;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IPostRepository extends CrudRepository<Post,Long> {
    Iterable<Post> getAllByAppUser(AppUser user);

    Iterable<Post> getAllByStatus(Status status);



//    @Query(value = "UPDATE post SET status WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
//    void updatePostStatus()
}
