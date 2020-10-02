package com.xem_vn.service.impl;

import com.xem_vn.model.Comment;
import com.xem_vn.model.Post;
import com.xem_vn.repository.ICommentRepository;
import com.xem_vn.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    ICommentRepository commentRepository;
    @Override
    public Iterable<Comment> getAllComment() {
        return commentRepository.findAll();
    }

    @Override
    public Comment getCommentById(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void remove(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public Iterable<Comment> getAllCommentByPost(Post post) {
        return commentRepository.getAllByPost(post);
    }
}
