package com.xem_vn.controller;

import com.xem_vn.model.AppRole;
import com.xem_vn.model.AppUser;
import com.xem_vn.service.IAppRoleService;
import com.xem_vn.service.IAppUserService;
import com.xem_vn.service.impl.AppUserServiceImpl;
import org.omg.IOP.ServiceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    IAppUserService userService;
    @Autowired
    IAppRoleService roleService;

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
    public String createUser(AppUser user,Model model){
        System.out.println("post create : " + user);
        AppRole role = roleService.getRoleByName("ROLE_USER");
        user.setRole(role);
        userService.save(user);
        model.addAttribute("user",user);
        return "redirect:/";
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
        user.setRole(role);
        userService.save(user);
        model.addAttribute("user",user);
        return "redirect:/edit";
    }
}
