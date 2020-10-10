package com.xem_vn.controller;

import com.xem_vn.model.*;
import com.xem_vn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
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
import java.util.List;

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

    @Autowired
    IVoteService voteService;
    @Autowired
    ILoveCommentService loveCommentService;

    @ModelAttribute("user")
    private AppUser getPrincipal() {
        AppUser appUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            appUser = userService.getUserByUserName(((UserDetails) principal).getUsername());
        }
        return appUser;
    }
    private List<Long> getListHeartUserId(){
        List<Long> list = loveCommentService.getListUserIds();
        return list;
    }

    private Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("post/create");
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
        ModelAndView modelAndView = new ModelAndView("post/create");
        postService.save(post);
        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView showEditForm() {
        ModelAndView modelAndView = new ModelAndView("post/edit");
        modelAndView.addObject("post", new Post());
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView edit(Post post) {
        ModelAndView modelAndView = new ModelAndView("post/edit");
        MultipartFile photo = post.getPhoto();
        post.setPhotoName(photo.getOriginalFilename());
        postService.save(post);
        return modelAndView;
    }

    @PostMapping(value = "/like")
    public ResponseEntity<Post> like(@RequestBody Like like) {
        Post currentPost = postService.getPostById(like.getPost().getId());
        AppUser currentUser = getPrincipal();
        if (currentUser != null && !likeService.existsByAppUserAndPost(currentUser, currentPost)) {
            likeService.save(like);
        } else {
            Like currentLike = likeService.getByAppUserAndPost(currentUser, currentPost);
            likeService.remove(currentLike);
        }
        Long countLike = likeService.countAllByPost(currentPost);
        currentPost.setLikeCount(countLike);
        postService.save(currentPost);
        return new ResponseEntity<>(currentPost, HttpStatus.OK);
    }
    @PostMapping("/loveComment")
    public ResponseEntity<Comment> loveComment(@RequestBody LoveComment loveComment) {
        Comment currentComment = commentService.getCommentById(loveComment.getComment().getId());
        System.out.println("id comment" + currentComment.getId());
        AppUser currentUser = getPrincipal();
        if (currentUser != null && !loveCommentService.existsByAppUserAndComment(currentUser, currentComment)) {
            loveCommentService.save(loveComment);
        } else {
            LoveComment heart = loveCommentService.getByAppUserAndComment(currentUser, currentComment);
            loveCommentService.remove(heart);
        }
        Long countLoveComment = loveCommentService.countAllByComment(currentComment);
        currentComment.setLoveCount(countLoveComment);
        commentService.save(currentComment);
        return new ResponseEntity<>(currentComment, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ModelAndView showPostDetail(@PathVariable("id") Long id,
                                       @PageableDefault(value = 5, page = 0)
                                       @SortDefault(sort = "timeComment", direction = Sort.Direction.DESC)
                                               Pageable pageable) {
        Post currentPost = postService.getPostById(id);
        Page<Comment> commentPage = commentService.getAllCommentByPost(currentPost, pageable);
        ModelAndView modelAndView = new ModelAndView("post/detail");
        modelAndView.addObject("post", currentPost);
        modelAndView.addObject("commentPage", commentPage);
        modelAndView.addObject("newComment", new Comment());
        modelAndView.addObject("currentTime", System.currentTimeMillis());
        modelAndView.addObject("listHeartUserIds", getListHeartUserId());
        return modelAndView;
    }

    @PostMapping("/detail/{id}")
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment,
                                               @PathVariable("id") Long id) {
        comment.setTimeComment(getCurrentDate());
        commentService.save(comment);
        Post currentPost = postService.getPostById(id);
        currentPost.setCommentCount(commentService.countAllByPost(currentPost));
        postService.save(currentPost);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PutMapping("/detail/{id}")
    public ResponseEntity<Comment> editComment(@RequestBody Comment commentEdited,
                                               @PathVariable("id") Long id) {
        Comment currentComment = commentService.getCommentById(commentEdited.getId());
        currentComment.setContent(commentEdited.getContent());
        commentService.save(currentComment);
        Post currentPost = postService.getPostById(id);
        currentPost.setCommentCount(commentService.countAllByPost(currentPost));
        postService.save(currentPost);
        return new ResponseEntity<>(currentComment, HttpStatus.OK);
    }

    @DeleteMapping("/detail/{id}")
    public ResponseEntity<Post> removeComment(@RequestBody Comment comment,
                                              @PathVariable("id") Long id) {
        Comment currentComment = commentService.getCommentById(comment.getId());
        commentService.remove(currentComment);

        Post currentPost = postService.getPostById(id);
        currentPost.setCommentCount(commentService.countAllByPost(currentPost));

        postService.save(currentPost);
        return new ResponseEntity<>(currentPost, HttpStatus.OK);

    }

    @PostMapping("/upVote")
    public ResponseEntity<Post> upVotePost(@RequestBody Vote vote) {

        Post currentPost = postService.getPostById(vote.getPost().getId());
        AppUser currentUser = getPrincipal();
        if (currentUser != null && !voteService.existsByAppUserAndAndPost(currentUser, currentPost)) {
            vote.setValue(1L);
            voteService.save(vote);
        } else {
            Vote currentVote = voteService.getByAppUserAndAndPost(currentUser, currentPost);
            if (currentVote.getValue() == -1L) {
                currentVote.setValue(1L);
                voteService.save(currentVote);
            } else {
                voteService.remove(currentVote);
            }
        }
        Long countVote = voteService.sumOfValues(currentPost);
        System.out.println("upVote_countVote = " + countVote);
        if (countVote == null) {
            currentPost.setVoteCount(0);
        } else {
            currentPost.setVoteCount(countVote);
        }

        postService.save(currentPost);
        return new ResponseEntity<>(currentPost, HttpStatus.OK);
    }

    @PostMapping("/downVote")
    public ResponseEntity<Post> downVotePost(@RequestBody Vote vote) {
        Post currentPost = postService.getPostById(vote.getPost().getId());
        AppUser currentUser = getPrincipal();
        if (currentUser != null && !voteService.existsByAppUserAndAndPost(currentUser, currentPost)) {
            vote.setValue(-1L);
            voteService.save(vote);
        } else {
            Vote currentVote = voteService.getByAppUserAndAndPost(currentUser, currentPost);
            if (currentVote.getValue() == 1L) {
                currentVote.setValue(-1L);
                voteService.save(currentVote);
            } else {
                voteService.remove(currentVote);
            }
        }
        Long countVote = voteService.sumOfValues(currentPost);
        if (countVote == null) {
            currentPost.setVoteCount(0);
        } else {
            currentPost.setVoteCount(countVote);
        }
        System.out.println("downVote_countVote = " + countVote);
        postService.save(currentPost);
        return new ResponseEntity<>(currentPost, HttpStatus.OK);
    }


}
