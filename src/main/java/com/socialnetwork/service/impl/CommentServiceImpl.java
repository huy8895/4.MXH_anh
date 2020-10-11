package com.socialnetwork.service.impl;

import com.socialnetwork.model.Comment;
import com.socialnetwork.model.Post;
import com.socialnetwork.repository.ICommentRepository;
import com.socialnetwork.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (comment != null)
            commentRepository.delete(comment);
    }

    @Override
    public Page<Comment> getAllCommentByPost(Post post, Pageable pageable) {
        return commentRepository.getAllByPost(post, pageable);
    }

    @Override
    public Long countAllByPost(Post post) {
        return commentRepository.countAllByPost(post);
    }

    @Override
    public List<Long> getAllCommentIdByUserLoved(Long commentId) {
        return commentRepository.getAllCommentIdByUserLoved(commentId);
    }


}
