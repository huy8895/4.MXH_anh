package com.socialnetwork.config.facebook;

import com.socialnetwork.service.impl.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Collections;

@Service
public class FacebookSignInAdapter implements SignInAdapter {
    @Autowired
    AppUserServiceImpl appUserService;

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        System.out.println(" ====== Sign In adapter");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(localUserId, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
        return "/login";
    }
}
