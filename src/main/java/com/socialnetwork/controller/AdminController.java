package com.socialnetwork.controller;

import com.socialnetwork.model.AppRole;
import com.socialnetwork.model.AppUser;
import com.socialnetwork.model.Post;
import com.socialnetwork.model.Status;
import com.socialnetwork.service.IAppRoleService;
import com.socialnetwork.service.IAppUserService;
import com.socialnetwork.service.IPostService;
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

@Controller
@RequestMapping("admin")
@CrossOrigin("*")
public class AdminController {
    @Autowired
    IAppUserService userService;
    @Autowired
    IAppRoleService roleService;

    @Autowired
    IPostService postService;

    @GetMapping
    public ModelAndView adminPage() {
        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.addObject("users",listUser());
        return modelAndView;
    }
    private Iterable<AppUser> listUser(){
        return userService.getAllUser();
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
        ModelAndView modelAndView = new ModelAndView("account/list");
        Iterable<AppUser> userList = userService.getAllUser();
        modelAndView.addObject("userList", userList);
        return modelAndView;
    }

    @GetMapping("/user-manager/edit/{id}")
    public ModelAndView showDetail(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("account/edit");
        AppUser appUser = userService.getUserById(id);
        modelAndView.addObject("user", appUser);
        return modelAndView;
    }

    @GetMapping("/user-manager/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("account/list");
        userService.remove(userService.getUserById(id));
        Iterable<AppUser> userList = userService.getAllUser();
        modelAndView.addObject("userList", userList);
        return modelAndView;
    }

//    @GetMapping
//    public ModelAndView showApprovalPage(@PageableDefault(value = 5, page = 0)
//                                         @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
//                                                 Pageable pageable) {
//        ModelAndView modelAndView = new ModelAndView("admin");
//        Status status = statusService.findByName("pending").get();
//        Page<Post> postPage = postService.getAllPostByStatus(status, pageable);
//        modelAndView.addObject("newComment", new Comment());
//        modelAndView.addObject("posts", postPage);
//        return modelAndView;
//    }

    @PostMapping("/block")
    public ResponseEntity<String> blockUser(@RequestBody AppUser user){
        AppUser blockUser = userService.getUserById(user.getId());
        AppRole appRole = roleService.getRoleByName("ROLE_BLOCKED");
        blockUser.setRole(appRole);
        userService.save(blockUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/post/approval/accept/{id}")
    public ModelAndView setStatusApprove(@PathVariable("id") Long postId,
                                         @PageableDefault(value = 10, page = 0)
                                         @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/post/approval");
        postService.setStatusForPost(Status.APPROVED,postId);
        Page<Post> postPage = postService.getAllPostByStatus(Status.PENDING, pageable);
        modelAndView.addObject("posts", postPage);
        return modelAndView;
    }

    @GetMapping("/post/approval/deny/{id}")
    public ModelAndView setStatusDeny(@PathVariable("id") Long postId,
                                      @PageableDefault(value = 10, page = 0)
                                      @SortDefault(sort = "dateUpload", direction = Sort.Direction.DESC)
                                              Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("post/approval");
        postService.setStatusForPost(Status.DENY,postId);
        Page<Post> postPage = postService.getAllPostByStatus(Status.PENDING, pageable);
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
