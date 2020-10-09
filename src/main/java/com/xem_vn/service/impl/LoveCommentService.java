package com.xem_vn.service.impl;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Comment;
import com.xem_vn.model.LoveComment;
import com.xem_vn.repository.ILoveCommentRepository;
import com.xem_vn.service.ILoveCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoveCommentService implements ILoveCommentService {
    @Autowired
    ILoveCommentRepository loveCommentRepository;
    @Override
    public Iterable<LoveComment> findAllByComment(Comment comment) {
        return loveCommentRepository.findAllByComment(comment);
    }

    @Override
    public Iterable<LoveComment> findAllByAppUser(AppUser user) {
        return loveCommentRepository.findAllByAppUser(user);
    }

    @Override
    public boolean existsByAppUserAndComment(AppUser appUser, Comment comment) {
        return loveCommentRepository.existsByAppUserAndComment(appUser,comment);
    }

    @Override
    public Long countAllByComment(Comment comment) {
        return loveCommentRepository.countAllByComment(comment);
    }

    @Override
    public LoveComment getByAppUserAndComment(AppUser user, Comment comment) {
        return loveCommentRepository.getByAppUserAndComment(user,comment);
    }

    @Override
    public void save(LoveComment loveComment) {
        loveCommentRepository.save(loveComment);
    }

    @Override
    public void remove(LoveComment loveComment) {
        loveCommentRepository.delete(loveComment);
    }
}
