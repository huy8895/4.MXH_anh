package com.xem_vn.controller;

import com.xem_vn.model.AppRole;
import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Status;
import com.xem_vn.service.IAppRoleService;
import com.xem_vn.service.IAppUserService;
import com.xem_vn.service.IPostService;
import com.xem_vn.service.IStatusService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    IAppUserService userService;
    @Autowired
    IAppRoleService roleService;

    @Autowired
    IPostService postService;
    @Autowired
    IStatusService statusService;

    @GetMapping
    public String adminPage() {
        return "admin";
    }

    private AppUser getPrincipal() {
        AppUser appUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            appUser = userService.getUserByUserName(((UserDetails) principal).getUsername()).orElse(null);

        }
        return appUser;
    }

    @GetMapping("/create")
    public ModelAndView showCreateUserForm() {
        ModelAndView modelAndView = new ModelAndView("account/create");
        modelAndView.addObject("user", new AppUser());
        return modelAndView;
    }

    @PostMapping("/create")
    public String createUser(AppUser user, Model model) {
        System.out.println("post create : " + user);
        AppRole role = roleService.getRoleByName("ROLE_ADMIN");
        user.setRole(role);
        userService.save(user);
        model.addAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public ModelAndView showEditUserForm() {
        ModelAndView modelAndView = new ModelAndView("account/edit");
        modelAndView.addObject("user", getPrincipal());
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editUser(AppUser user, Model model) {
        AppRole role = roleService.getRoleByName("ROLE_USER");
        user.setRole(role);
        userService.save(user);
        model.addAttribute("user", user);
        return "redirect:/admin/user-manager";
    }

    @GetMapping("/user-manager")
    public ModelAndView showListUser() {
        ModelAndView modelAndView = new ModelAndView("/account/list");
        Iterable<AppUser> userList = userService.getAllUser();
        modelAndView.addObject("userList", userList);
        return modelAndView;
    }

    @GetMapping("/user-manager/edit/{id}")
    public ModelAndView showDetail(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("/account/edit");
        AppUser appUser = userService.getUserById(id);
        modelAndView.addObject("user", appUser);
        return modelAndView;
    }

    @GetMapping("/user-manager/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("/account/list");
        userService.remove(userService.getUserById(id));
        Iterable<AppUser> userList = userService.getAllUser();
        modelAndView.addObject("userList", userList);
        return modelAndView;
    }

    @GetMapping("/post/approval")
    public ModelAndView showApprovalPage(@PageableDefault(value = 5, page = 0)
                                         @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/post/approval");
        Status status = statusService.findByName("pending").get();
        Page<Post> postPage = postService.getAllPostByStatus(status, pageable);
        modelAndView.addObject("posts", postPage);
        return modelAndView;
    }

    @GetMapping("/post/approval/accept/{id}")
    public ModelAndView setStatusApprove(@PathVariable("id") Long postId,
                                         @PageableDefault(value = 10, page = 0)
                                         @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/post/approval");
        postService.setStatusForPost(statusService.findByName("approve").get().getId(),postId);
        Status status = statusService.findByName("pending").get();
        Page<Post> postPage = postService.getAllPostByStatus(status, pageable);
        modelAndView.addObject("posts", postPage);
        return modelAndView;
    }

    @GetMapping("/post/approval/deny/{id}")
    public ModelAndView setStatusDeny(@PathVariable("id") Long postId,
                                         @PageableDefault(value = 10, page = 0)
                                         @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/post/approval");
        postService.setStatusForPost(statusService.findByName("deny").get().getId(),postId);
        Status status = statusService.findByName("pending").get();
        Page<Post> postPage = postService.getAllPostByStatus(status, pageable);
        modelAndView.addObject("posts", postPage);
        return modelAndView;
    }

//    @GetMapping("/user-manager/delete/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id){
////        ModelAndView modelAndView = new ModelAndView("/account/detail");
//        userService.remove(userService.getUserById(id));
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
