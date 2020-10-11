package com.socialnetwork.service.impl;

import com.socialnetwork.repository.ILoveCommentRepository;
import com.socialnetwork.service.ILoveCommentService;
import com.socialnetwork.model.AppUser;
import com.socialnetwork.model.Comment;
import com.socialnetwork.model.LoveComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public boolean existsByAppUserId(Long appUserId) {
        return loveCommentRepository.existsByAppUserId(appUserId);
    }

    @Override
    public List<Long> getListUserIds() {
        return loveCommentRepository.getListUserIds();
    }

    @Override
    public List<LoveComment> getListLoveComment(Long userId) {
        return loveCommentRepository.getListLoveComment(userId);
    }

    @Override
    public LoveComment getByCommentId(Long commentId) {
        return loveCommentRepository.getByCommentId(commentId);
    }
}
