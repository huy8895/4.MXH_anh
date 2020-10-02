package com.xem_vn.service.impl;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.repository.IPostRepository;
import com.xem_vn.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    IPostRepository postRepository;
    @Override
    public Iterable<Post> getAllPost() {
        return postRepository.findAll();
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
    public Iterable<Post> getAllPostByUser(AppUser user) {
        return postRepository.getAllByAppUser(user);
    }
}
