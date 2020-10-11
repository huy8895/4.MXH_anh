package com.xem_vn.service;

import com.xem_vn.model.Comment;
import com.xem_vn.model.LoveComment;
import com.xem_vn.model.Post;
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
