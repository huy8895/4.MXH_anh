package com.xem_vn.service.impl;

import com.xem_vn.model.AppRole;
import com.xem_vn.model.AppUser;
import com.xem_vn.repository.IAppUserRepository;
import com.xem_vn.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username);
        List<AppRole> roles = new ArrayList<>();
        roles.add(appUser.getRole());
        User user = new User(appUser.getUsername(),appUser.getPassword(),roles);
        return user;
    }
}
