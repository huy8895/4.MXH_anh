package com.socialnetwork.repository;

import com.socialnetwork.model.Comment;
import com.socialnetwork.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICommentRepository extends CrudRepository<Comment,Long> {
    Page<Comment> getAllByPost(Post post, Pageable pageable);
    Long countAllByPost (Post post);
    @Query(value = "select comment.id from comment where comment.id  in (select lovescomment.comment_id from lovescomment where lovescomment.app_user_id = ?1)", nativeQuery = true)
    List<Long> getAllCommentIdByUserLoved(Long userId);
}
