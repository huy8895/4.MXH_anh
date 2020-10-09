package com.xem_vn.repository;

import com.xem_vn.model.*;
import org.springframework.data.repository.CrudRepository;

public interface ILoveCommentRepository extends CrudRepository<LoveComment,Long> {
    Iterable<LoveComment> findAllByComment(Comment comment);
    Iterable<LoveComment> findAllByAppUser(AppUser user);
    boolean existsByAppUserAndComment(AppUser appUser, Comment comment);
    Long countAllByComment (Comment comment);
    LoveComment getByAppUserAndComment (AppUser user,Comment comment);
}
