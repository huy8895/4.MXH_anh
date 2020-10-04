package com.xem_vn.controller;

import com.xem_vn.model.AppUser;
import com.xem_vn.model.Post;
import com.xem_vn.model.Status;
import com.xem_vn.service.IAppUserService;
import com.xem_vn.service.IPostService;
import com.xem_vn.service.IStatusService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IPostService postService;

    @Autowired
    IStatusService statusService;

    private String getPrincipal(){
        String appUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            appUser = ((UserDetails) principal).getUsername();

        }
        else appUser = principal.toString();
        return appUser;
    }


    @GetMapping({"/", "/home"})
    public ModelAndView showApprovalPage(@PageableDefault(value = 5, page = 0)
//                                         @SortDefault(sort = "username", direction = Sort.Direction.DESC)
                                                 Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/welcome");
        Status status = statusService.findByName("approve").get();
        Page<Post> postPage ;
        try {
            postPage =  postService.getAllPostByStatus(status, pageable);
        } catch (Exception e){
//            e.printStackTrace();
            postPage = null;
        }
        modelAndView.addObject("posts", postPage);
        return modelAndView;
    }

    @GetMapping("/Access_Denied")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "accessDenied";
    }



}
