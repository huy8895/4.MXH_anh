package com.xem_vn.service;

import com.xem_vn.model.Comment;
import com.xem_vn.model.Post;

public interface ICommentService {
    Iterable<Comment> getAllComment();
    Comment getCommentById(long id);
    Comment save(Comment comment);
    void remove(Comment comment);
    Iterable<Comment> getAllCommentByPost(Post post);
    Long countAllByPost (Post post);
}
