package com.socialnetwork.service;

import com.socialnetwork.model.AppUser;

import java.util.List;

public interface IAppUserService {
    List<AppUser> getAllUser();
    AppUser getUserById(Long id);
    AppUser save(AppUser user);
    void remove(AppUser user);
    AppUser getUserByUserName(String userName);
    boolean existsAppUserByUsername(String userName);
    AppUser findTopByOrderByIdDesc();
}
