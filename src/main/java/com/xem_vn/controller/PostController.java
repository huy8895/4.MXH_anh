package com.xem_vn.controller;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Like;
import com.xem_vn.model.Post;
import com.xem_vn.repository.IPostRepository;
import com.xem_vn.service.IAppUserService;
import com.xem_vn.service.ILikeService;
import com.xem_vn.service.IPostService;
import com.xem_vn.service.IStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("post")
@CrossOrigin("*")
public class PostController {
    @Autowired
    IPostService postService;

    @Autowired
    IStatusService statusService;

    @Value("${upload.path}")
    private String upload_path;

    @Autowired
    IAppUserService userService;

    @Autowired
    ILikeService likeService;

    @ModelAttribute("user")
    private AppUser getPrincipal() {
        AppUser appUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            appUser = userService.getUserByUserName(((UserDetails) principal).getUsername()).orElse(null);

        }
        return appUser;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/post/create");
        modelAndView.addObject("post", new Post());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createPost(Post post) {
        long currentTime = System.currentTimeMillis();
        post.setDateUpload(new Date(currentTime));
        MultipartFile photo = post.getPhoto();
        String photoName = "post_" + photo.getOriginalFilename();
        post.setPhotoName(photoName);
        post.setStatus(statusService.findByName("pending").get());
        post.setAppUser(getPrincipal());
        try {
            FileCopyUtils.copy(photo.getBytes(), new File(upload_path + photoName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        postService.save(post);
        ModelAndView modelAndView = new ModelAndView("/post/create");
        postService.save(post);
        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView showEditForm() {
        ModelAndView modelAndView = new ModelAndView("/post/edit");
        modelAndView.addObject("post", new Post());
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView edit(Post post) {
        ModelAndView modelAndView = new ModelAndView("/post/edit");
        MultipartFile photo = post.getPhoto();
        post.setPhotoName(photo.getOriginalFilename());
        postService.save(post);
        return modelAndView;
    }

    @PostMapping(value = "/like")
    public ResponseEntity<Like> like(@RequestBody Like like) {
        if (!likeService.existsByAppUserAndAndPost(like.getAppUser(), like.getPost())){
            likeService.save(like);
            Post currentPost = postService.getPostById(like.getPost().getId());
            currentPost.setLikeCount(likeService.countAllByPost(currentPost));
        }
        return new ResponseEntity<>(like,HttpStatus.OK);
    }
}
