package com.xem_vn.service.impl;

import com.xem_vn.model.AppRole;
import com.xem_vn.model.AppUser;
import com.xem_vn.repository.IAppUserRepository;
import com.xem_vn.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements IAppUserService, UserDetailsService {
    @Autowired
    private IAppUserRepository userRepository;

    @Override
    public Iterable<AppUser> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public AppUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public AppUser save(AppUser user) {
        return userRepository.save(user);
    }

    @Override
    public void remove(AppUser user) {
        userRepository.delete(user);
    }

    @Override
    public AppUser getUserByUserName(String userName) {
        return userRepository.findAppUserByUsername(userName);
    }

    @Override
    public boolean existsAppUserByUsername(String userName) {
        return userRepository.existsAppUserByUsername(userName);
    }

    @Override
    public AppUser findTopByOrderByIdDesc() {
        return userRepository.findTopByOrderByIdDesc();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser optionalUser = userRepository.findAppUserByUsername(username);
        if(optionalUser == null)
            throw new UsernameNotFoundException(username);
        List<AppRole> roles = new ArrayList<>();

//        if (optionalUser) {
            roles.add(optionalUser.getRole());
            User user = new User(optionalUser.getUsername(),optionalUser.getPassword(),true, true, true, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            return user;
//        }
//        else {
//            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", username));
//        }
    }
}
