package com.socialnetwork.controller;

import com.socialnetwork.config.amazon.AmazonClient;
import com.socialnetwork.model.AppUser;
import com.socialnetwork.model.Post;
import com.socialnetwork.service.IAppRoleService;
import com.socialnetwork.service.IAppUserService;
import com.socialnetwork.service.IPostService;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("account")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AccountController {
    private final IAppUserService userService;
    private final IAppRoleService roleService;
    private final IPostService postService;
    private final AmazonClient amazonClient;

    @Value("${upload.path}")
    private String upload_path;

    private List<Long> getAllPostIdByUserLiked(Long userId){
        List<Long> list = postService.getAllPostIdByUserLiked(userId);
        return list;
    }

    @ModelAttribute("user")
    private AppUser getPrincipal() {
        AppUser appUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            appUser = userService.getUserByUserName(((UserDetails) principal).getUsername());
        }
        return appUser;
    }


    @GetMapping("/edit")
    public String showEditUserForm() {
        return "account/edit";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") AppUser user) {
        String avatarFileName ;
        if (!Objects.equals(user.getAvatarFile().getOriginalFilename(), "")) {
            avatarFileName = "avatar_" + user.getId() + user.getAvatarFile().getOriginalFilename();
            user.setAvatarFileName(avatarFileName);
            final String fileUrl = amazonClient.uploadFile(user.getAvatarFile());
            user.setAvatarUrl(fileUrl);
        }
        userService.save(user);
        return "/account/edit";
    }


    @GetMapping("/password")
    public String showPassWordForm() {
        return "account/password";
    }

    @PostMapping("/password")
    public String showPassWordForm(@RequestParam("newPass") String newPass) {
        AppUser currentUser = getPrincipal();
        if (currentUser != null) {
            currentUser.setPassword(newPass);
            userService.save(currentUser);
        }
        return "/account/password";
    }

    @GetMapping("/notification")
    public String showNotificationPage() {
        return "account/notification";
    }

    @GetMapping("/favorite")
    public ModelAndView showFavoritePage(@PageableDefault(value = 10, page = 0)
                                   @SortDefault(sort = "date_Upload", direction = Sort.Direction.DESC)
                                           Pageable pageable) {
        AppUser currentUser = getPrincipal();
        ModelAndView modelAndView = new ModelAndView("account/favorite");
        Page<Post> posts = postService.findAllPostByUserLiked(currentUser.getId(), pageable);
        modelAndView.addObject("posts", posts);
        modelAndView.addObject("currentTime", System.currentTimeMillis());
        modelAndView.addObject("post", new Post());
        if(getPrincipal()!=null) {
            modelAndView.addObject("listPostLiked", getAllPostIdByUserLiked(getPrincipal().getId()));
        }
        return modelAndView;
    }

}
