package com.socialnetwork.service;

import com.socialnetwork.model.AppUser;
import com.socialnetwork.model.Comment;
import com.socialnetwork.model.LoveComment;

import java.util.List;

public interface ILoveCommentService {
    Iterable<LoveComment> findAllByComment(Comment comment);
    Iterable<LoveComment> findAllByAppUser(AppUser user);
    boolean existsByAppUserAndComment(AppUser appUser, Comment comment);
    Long countAllByComment (Comment comment);
    LoveComment getByAppUserAndComment (AppUser user,Comment comment);
    void save(LoveComment loveComment);
    void remove(LoveComment loveComment);
    boolean existsByAppUserId(Long appUserId);
    List<Long> getListUserIds();
    List<LoveComment> getListLoveComment(Long userId);
    LoveComment getByCommentId(Long commentId);

}
