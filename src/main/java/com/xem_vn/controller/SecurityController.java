package com.xem_vn.controller;

import com.xem_vn.model.AppUser;
import com.xem_vn.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecurityController {
    @Autowired
    private IAppUserService appUserService;

//    @Autowired
//    private IA

    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    @GetMapping(value = {"/", "/home"})
    public String Homepage(Model model){
        model.addAttribute("user", getPrincipal());
        return "welcome";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "admin";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String userPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "user";
    }

    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "accessDenied";
    }

    @GetMapping("/signup")
    public String signupPage(Model model){
        model.addAttribute("user",new AppUser());

        return "signup";
    }

    @PostMapping
    public String signup(AppUser appUser,Model model){
        //lay role the name o bang role

        if (appUserService.save(appUser)!=null){
            model.addAttribute("message","tao thanh cong");
        } else {
            model.addAttribute("message","tao khong thanh cong");
        }
        model.addAttribute("user",new AppUser());
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
