package com.socialnetwork.service.impl;

import com.socialnetwork.repository.IPostRepository;
import com.socialnetwork.model.AppUser;
import com.socialnetwork.model.Post;
import com.socialnetwork.model.Status;
import com.socialnetwork.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    public void setStatusForPost(Status status, Long postID) {
        postRepository.setStatusForPost(status,postID);
    }

    @Override
    public Page<Post> findAllPostByUserLiked(Long appUserId, Pageable pageable) {
        return postRepository.findAllPostByUserLiked(appUserId,pageable);
    }

    @Override
    public List<Long> getAllPostIdByUserLiked(Long userId) {
        return postRepository.getAllPostIdByUserLiked(userId);
    }
}
