package com.xem_vn.config.facebook;


import com.xem_vn.model.AppRole;
import com.xem_vn.model.AppUser;
import com.xem_vn.service.IAppRoleService;
import com.xem_vn.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp {

    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private IAppRoleService roleService;

    @Override
    public String execute(Connection<?> connection) {
        System.out.println("signup === ");
        AppUser user = new AppUser();
        System.out.println("key: " +connection.getKey());
        if(!appUserService.existsAppUserByUsername(""+connection.getKey())) {
            user.setFullName(connection.getDisplayName());
            user.setPassword(randomAlphabetic(8));
            user.setAvatarFileName("default_avatar.jpg");
            String userName = connection.getKey().toString().substring(9);
            user.setUsername(userName);
            AppRole role = roleService.getRoleByName("ROLE_USER");
            user.setRole(role);
            appUserService.save(user);
            return user.getUsername();
        }else{
            user = appUserService.getUserByUserName(""+connection.getKey());
            return user.getUsername();
        }
// tao random username
//        Iterable<AppUser> users = appUserService.getAllUser();
//        user = appUserService.findTopByOrderByIdDesc();
//        String setName = "fb_user"+user.getId();
//        if(appUserService.existsAppUserByUsername(setName)){
//            setName+=randomAlphabetic(4);
//        }
//        user.setUsername(setName);            e
    }

}
