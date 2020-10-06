package com.xem_vn.repository;

import com.xem_vn.model.Comment;
import com.xem_vn.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ICommentRepository extends CrudRepository<Comment,Long> {
    Page<Comment> getAllByPost(Post post, Pageable pageable);
    Long countAllByPost (Post post);
}
