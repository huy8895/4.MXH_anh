package com.xem_vn.controller;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.service.IAppRoleService;
import com.xem_vn.service.IAppUserService;
import com.xem_vn.service.IPostService;
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
import java.util.Objects;

@Controller
@RequestMapping("account")
@CrossOrigin("*")
public class AccountController {
    @Autowired
    IAppUserService userService;
    @Autowired
    IAppRoleService roleService;

    @Autowired
    IPostService postService;

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
            try {
                FileCopyUtils.copy(user.getAvatarFile().getBytes(), new File(upload_path + avatarFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.save(user);
        return "/account/edit";
    }


    @GetMapping("/password")
    public String showPassWordForm() {
        return "/account/password";
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
        return "/account/notification";
    }

    @GetMapping("/favorite")
    public ModelAndView showFavoritePage(@PageableDefault(value = 10, page = 0)
                                   @SortDefault(sort = "date_Upload", direction = Sort.Direction.DESC)
                                           Pageable pageable) {
        AppUser currentUser = getPrincipal();
        ModelAndView modelAndView = new ModelAndView("/account/favorite");
        Page<Post> posts = postService.findAllPostByUserLiked(currentUser.getId(), pageable);
        modelAndView.addObject("posts", posts);
        return modelAndView;
    }

}
