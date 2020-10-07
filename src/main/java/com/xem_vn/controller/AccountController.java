package com.xem_vn.controller;

import com.xem_vn.model.AppRole;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
            appUser = userService.getUserByUserName(((UserDetails) principal).getUsername()).orElse(null);
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

    @GetMapping("/uploader")
    public ModelAndView showUploaderPage(@PageableDefault(value = 10, page = 0)
                                         @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        AppUser user = getPrincipal();
        postService.getAllPostByUser(user, pageable);
        Page<Post> posts = postService.getAllPostByUser(user, pageable);
        ModelAndView modelAndView = new ModelAndView("/account/uploader");
        modelAndView.addObject("posts", posts);
        return modelAndView;
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
    public String showFavoritePage() {
        return "/account/favorite";
    }
}
