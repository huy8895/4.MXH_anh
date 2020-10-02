package com.xem_vn.controller;

import com.xem_vn.model.AppRole;
import com.xem_vn.model.AppUser;
import com.xem_vn.service.IAppRoleService;
import com.xem_vn.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    IAppUserService userService;
    @Autowired
    IAppRoleService roleService;

    @GetMapping("/create")
    public ModelAndView showCreateUserForm(){
        ModelAndView modelAndView = new ModelAndView("account/create");
        modelAndView.addObject("user",new AppUser());
        return modelAndView;
    }

    @PostMapping("/create")
    public String createUser(AppUser user, Model model){
        System.out.println("post create");
        AppRole role = roleService.getRoleByName("ROLE_USER");
        user.setRole(role);
        userService.save(user);
        model.addAttribute("user",user);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String showEditUserForm(){
        return "account/edit";
    }

//    @PostMapping("/edit")
//    public String editUser(AppUser user, Model model){
//        AppRole role = roleService.getRoleByName("ROLE_USER");
//        user.setRole(role);
//        userService.save(user);
//        model.addAttribute("user",user);
//        return "redirect:/edit";
//    }
}
