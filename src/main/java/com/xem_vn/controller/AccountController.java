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
import org.springframework.data.web.PageableDefault;
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

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    IAppUserService userService;
    @Autowired
    IAppRoleService roleService;

    @Autowired
    IPostService postService;

    @Value("${upload.path}")
    private String upload_path;

    @ModelAttribute
    private AppUser getPrincipal(){
        AppUser appUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            appUser = userService.getUserByUserName(((UserDetails)principal).getUsername()).orElse(null);
        }
        return appUser;
    }

    @GetMapping("/create")
    public ModelAndView showCreateUserForm(){
        ModelAndView modelAndView = new ModelAndView("account/create");
        modelAndView.addObject("user",new AppUser());
        return modelAndView;
    }

    @PostMapping("/create")
    public ResponseEntity<AppUser> createUser(AppUser user){
        System.out.println("post create : " + user);
        AppRole role = roleService.getRoleByName("ROLE_USER");
        user.setRole(role);
        MultipartFile avatar = user.getAvatarFile();
        String avatarFileName = avatar.getOriginalFilename();
        user.setAvatarFileName(avatarFileName);
        userService.save(user);
        try {
            FileCopyUtils.copy(avatar.getBytes(), new File(upload_path + avatarFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/edit")
    public ModelAndView showEditUserForm(){
        ModelAndView modelAndView = new ModelAndView("account/edit");
        modelAndView.addObject("user",getPrincipal());
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editUser(AppUser user, Model model){
        AppRole role = roleService.getRoleByName("ROLE_USER");
        String avatarFileName = "avatar_"+user.getAvatarFile().getOriginalFilename();
        try {
            FileCopyUtils.copy(user.getAvatarFile().getBytes(), new File(upload_path + avatarFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setRole(role);
        userService.save(user);
        model.addAttribute("user",user);
        return "/account/edit";
    }

    @GetMapping("/uploader")
    public ModelAndView showUploaderPage(@PageableDefault(value = 5, page = 0)
//                                         @SortDefault(sort = "username", direction = Sort.Direction.DESC)
                                               Pageable pageable){
        AppUser user = getPrincipal();
        postService.getAllPostByUser(user,pageable);
        Page<Post> posts = postService.getAllPostByUser(user,pageable);
        ModelAndView modelAndView = new ModelAndView("/account/uploader");
        modelAndView.addObject("posts",posts);
        return modelAndView;
    }

    @GetMapping("/password")
    public String showPassWordForm(){
        return "/account/password";
    }

    @GetMapping("/notification")
    public String showNotificationPage(){
        return "/account/notification";
    }

    @GetMapping("/favorite")
    public String showFavoritePage(){
        return "/account/favorite";
    }
}
