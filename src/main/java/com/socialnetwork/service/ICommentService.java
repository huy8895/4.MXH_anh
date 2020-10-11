package com.socialnetwork.service;

import com.socialnetwork.model.Comment;
import com.socialnetwork.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {
    Iterable<Comment> getAllComment();
    Comment getCommentById(long id);
    Comment save(Comment comment);
    void remove(Comment comment);
    Page<Comment> getAllCommentByPost(Post post, Pageable pageable);
    Long countAllByPost (Post post);
    List<Long> getAllCommentIdByUserLoved(Long commentId);
}
