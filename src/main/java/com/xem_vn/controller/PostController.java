package com.xem_vn.controller;

import com.xem_vn.model.*;
import com.xem_vn.repository.IPostRepository;
import com.xem_vn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

    @Autowired
    ICommentService commentService;

    @ModelAttribute("user")
    private AppUser getPrincipal() {
        AppUser appUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            appUser = userService.getUserByUserName(((UserDetails) principal).getUsername());

        }
        return appUser;
    }

    private Date getCurrentDate(){
        return new Date(System.currentTimeMillis());
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/post/create");
        modelAndView.addObject("post", new Post());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createPost(Post post) {
        post.setDateUpload(getCurrentDate());
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
    public ResponseEntity<Post> like(@RequestBody Like like) {
        Post currentPost = postService.getPostById(like.getPost().getId());
        AppUser currentUser = getPrincipal();
        if (currentUser!=null && !likeService.existsByAppUserAndAndPost(currentUser, currentPost)){
            likeService.save(like);
        } else {
            Like currentLike = likeService.getByAppUserAndAndPost(currentUser,currentPost);
            likeService.remove(currentLike);
        }
        Long countLike = likeService.countAllByPost(currentPost);
        currentPost.setLikeCount(countLike);
        postService.save(currentPost);
        return new ResponseEntity<>(currentPost,HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ModelAndView showPostDetail(@PathVariable("id") Long id,
                                       @PageableDefault(value = 5, page = 0)
                                       @SortDefault(sort = "timeComment", direction = Sort.Direction.DESC)
                                               Pageable pageable) {
        Post currentPost = postService.getPostById(id);
        Page<Comment> commentPage = commentService.getAllCommentByPost(currentPost,pageable);
        ModelAndView modelAndView = new ModelAndView("/post/detail");
        modelAndView.addObject("post", currentPost);
        modelAndView.addObject("commentPage", commentPage);
        modelAndView.addObject("newComment", new Comment());
        modelAndView.addObject("currentTime", System.currentTimeMillis());
        return modelAndView;
    }

    @PostMapping("/detail/{id}")
    public ResponseEntity<Post> saveComment(@RequestBody Comment comment,
                                            @PathVariable("id") Long id){
        comment.setTimeComment(getCurrentDate());
        commentService.save(comment);
        System.out.println(comment);
        Post currentPost = postService.getPostById(id);
        currentPost.setCommentCount(commentService.countAllByPost(currentPost));
        postService.save(currentPost);
        return new ResponseEntity<>(currentPost,HttpStatus.OK);
    }


}
