package com.xem_vn.controller;

import com.xem_vn.config.facebook.FacebookConnectionSignup;
import com.xem_vn.model.AppRole;
import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Status;
import com.xem_vn.repository.ILikeRepository;
import com.xem_vn.service.*;
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
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    IAppRoleService roleService;

    @Autowired
    IPostService postService;

    @Value("${upload.path}")
    private String upload_path;
    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IAppUserService userService;

    @Autowired
    IStatusService statusService;
    @Autowired
    ILikeService likeService;
    @Autowired
    private FacebookConnectionSignup facebookConnectionSignup;

    @ModelAttribute("user")
    private AppUser getPrincipal() {
        AppUser appUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            appUser = userService.getUserByUserName(((UserDetails) principal).getUsername());
        }
        return appUser;
    }
    @ModelAttribute("likedUserIds")
    private List<Long> getListLikedUserIds(){
        List<Long> list = likeService.getListLikedUserIds();
        return list;
    }

    @GetMapping({"/", "/home"})
    public ModelAndView showApprovalPage(@PageableDefault(value = 10, page = 0)
                                         @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("welcome");
        Status status = statusService.findByName("approve").get();
        Page<Post> postPage =  postService.getAllPostByStatus(status, pageable);
        modelAndView.addObject("posts", postPage);
        modelAndView.addObject("currentTime", System.currentTimeMillis());
        modelAndView.addObject("post", new Post());
        return modelAndView;
    }
    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView("login");
        AppUser checkFbUser = appUserService.findTopByOrderByIdDesc();
        if(checkFbUser.getUsername().length()==15 && checkFbUser.getRole().getId()==2)
        {
            System.out.println("true check");
            modelAndView.addObject("messageLogin","Successful !!");
            modelAndView.addObject("fbUser",checkFbUser);
        }
        return modelAndView;
    }

    @GetMapping("/Access_Denied")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal().getUsername());
        return "accessDenied";
    }

    @GetMapping("/create-account")
    public ModelAndView showCreateUserForm(){
        ModelAndView modelAndView = new ModelAndView("account/create");
        modelAndView.addObject("newUser",new AppUser());
        return modelAndView;
    }

    @PostMapping("/create-account")
    public ModelAndView createUser(@ModelAttribute("newUser") AppUser user){
        System.out.println("post create : " + user);
        System.out.println(user.getUsername());
        AppRole role = roleService.getRoleByName("ROLE_USER");
        user.setRole(role);
        MultipartFile avatar = user.getAvatarFile();
        String avatarFileName = avatar.getOriginalFilename();
        if(avatarFileName!=null) {
            user.setAvatarFileName(avatarFileName);
            try {
                FileCopyUtils.copy(avatar.getBytes(), new File(upload_path + avatarFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            user.setAvatarFileName("default_avatar.jpg");
        userService.save(user);
        return  new ModelAndView("/account/create");
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
        return modelAndView;
    }

    @GetMapping("/vote")
    public ModelAndView showVotePage(@PageableDefault(value = 10, page = 0)
                                         @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/vote");
        Status status = statusService.findByName("pending").get();
        Page<Post> postPage =  postService.getAllPostByStatus(status, pageable);
        modelAndView.addObject("posts", postPage);
        modelAndView.addObject("currentTime", System.currentTimeMillis());
        return modelAndView;
    }
}
