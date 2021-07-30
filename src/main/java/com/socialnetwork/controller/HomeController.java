package com.socialnetwork.controller;

import com.socialnetwork.config.amazon.AmazonClient;
import com.socialnetwork.config.facebook.FacebookConnectionSignup;
import com.socialnetwork.model.AppRole;
import com.socialnetwork.model.AppUser;
import com.socialnetwork.model.Post;
import com.socialnetwork.model.Status;
import com.socialnetwork.service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final IAppRoleService roleService;
    private final IPostService postService;
    private final IAppUserService appUserService;
    private final IAppUserService userService;
    private final AmazonClient amazonClient;
    private final IStatusService statusService;
    private final ILikeService likeService;
    private final FacebookConnectionSignup facebookConnectionSignup;

    @Value("${upload.path}")
    private String upload_path;

    @ModelAttribute("user")
    private AppUser getPrincipal() {
        AppUser appUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            appUser = userService.getUserByUserName(((UserDetails) principal).getUsername());
        }
        return appUser;
    }


    private List<Long> getAllPostIdByUserLiked(Long userId) {
        List<Long> list = postService.getAllPostIdByUserLiked(userId);
        return list;
    }

    @GetMapping({"/", "/home"})
    public ModelAndView showApprovalPage(@PageableDefault(value = 10, page = 0)
                                         @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("welcome");
        Status status = statusService.findByName("approve").get();
        Page<Post> postPage = postService.getAllPostByStatus(status, pageable);
        modelAndView.addObject("posts", postPage);
        modelAndView.addObject("currentTime", System.currentTimeMillis());
        modelAndView.addObject("post", new Post());
        if (getPrincipal() != null) {
            modelAndView.addObject("listPostLiked", getAllPostIdByUserLiked(getPrincipal().getId()));
        }
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        AppUser checkFbUser = appUserService.findTopByOrderByIdDesc();
        if (checkFbUser != null) {
            if (checkFbUser.getUsername().length() == 15 && checkFbUser.getRole().getId() == 2) {
                System.out.println("true check");
                modelAndView.addObject("messageLogin", "Successful !!");
                modelAndView.addObject("fbUser", checkFbUser);
            }
        }
        return modelAndView;
    }

    @GetMapping("/Access_Denied")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal().getUsername());
        return "accessDenied";
    }

    @GetMapping("/create-account")
    public ModelAndView showCreateUserForm() {
        ModelAndView modelAndView = new ModelAndView("account/create");
        modelAndView.addObject("newUser", new AppUser());
        return modelAndView;
    }

    @PostMapping("/create-account")
    public ModelAndView createUser(@ModelAttribute("newUser") AppUser user) {
        System.out.println("post create : " + user);
        System.out.println(user.getUsername());
        AppRole role = roleService.getRoleByName("ROLE_USER");
        user.setRole(role);
        MultipartFile avatar = user.getAvatarFile();
        String avatarFileName = avatar.getOriginalFilename();
        user.setAvatarFileName(avatarFileName);
        final String fileUrl = amazonClient.uploadFile(user.getAvatarFile());
        user.setAvatarUrl(fileUrl);
        userService.save(user);
        return new ModelAndView("/account/create");
    }

    @GetMapping("/uploader/{id}")
    public ModelAndView showUploaderPage(@PathVariable("id") Long userId,
                                         @PageableDefault(value = 10, page = 0)
                                         @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        AppUser user = userService.getUserById(userId);
        Page<Post> posts = postService.getAllPostByUser(user, pageable);
        ModelAndView modelAndView = new ModelAndView("/account/uploader");
        modelAndView.addObject("posts", posts);
        if (getPrincipal() != null) {
            modelAndView.addObject("listPostLiked", getAllPostIdByUserLiked(getPrincipal().getId()));
        }
        return modelAndView;
    }

    @GetMapping("/vote")
    public ModelAndView showVotePage(@PageableDefault(value = 10, page = 0)
                                     @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                             Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/vote");
        Status status = statusService.findByName("pending").get();
        Page<Post> postPage = postService.getAllPostByStatus(status, pageable);
        modelAndView.addObject("posts", postPage);
        modelAndView.addObject("currentTime", System.currentTimeMillis());
        return modelAndView;
    }
}
