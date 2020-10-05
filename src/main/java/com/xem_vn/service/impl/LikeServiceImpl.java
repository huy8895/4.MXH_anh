package com.xem_vn.service.impl;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Like;
import com.xem_vn.model.Post;
import com.xem_vn.repository.ILikeRepository;
import com.xem_vn.service.ILikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements ILikeService {
    @Autowired
    ILikeRepository likeRepository;
    @Override
    public Iterable<Like> findAllByPost(Post post) {
        return likeRepository.findAllByPost(post);
    }

    @Override
    public Iterable<Like> findAllByUser(AppUser user) {
        return likeRepository.findAllByAppUser(user);
    }

    @Override
    public void save(Like like) {
        likeRepository.save(like);
    }

    @Override
    public void remove(Like like) {
        likeRepository.delete(like);
    }

    @Override
    public boolean existsByAppUserAndAndPost(AppUser user, Post post) {
        return likeRepository.existsByAppUserAndAndPost(user,post);
    }
}
