package com.xem_vn.service.impl;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Status;
import com.xem_vn.repository.IPostRepository;
import com.xem_vn.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    IPostRepository postRepository;
    @Override
    public Page<Post> getAllPost(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Post getPostById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void remove(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Page<Post> getAllPostByUser(AppUser user, Pageable pageable) {
        return postRepository.getAllByAppUser(user,pageable);
    }

    @Override
    public Page<Post> getAllPostByStatus(Status status, Pageable pageable) {
        return postRepository.getAllByStatus(status,pageable);
    }

    @Override
    public void setStatusForPost(Long statusID, Long postID) {
        postRepository.setStatusForPost(statusID,postID);
    }

    @Override
    public Page<Post> getAllByAppUserLike(AppUser appUser, Pageable pageable) {
        return postRepository.getAllByAppUserLike(appUser,pageable);
    }
}
