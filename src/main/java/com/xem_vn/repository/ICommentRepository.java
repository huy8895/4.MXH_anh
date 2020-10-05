package com.xem_vn.repository;

import com.xem_vn.model.Comment;
import com.xem_vn.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface ICommentRepository extends CrudRepository<Comment,Long> {
    Iterable<Comment> getAllByPost(Post post);
    Long countAllByPost (Post post);
}
