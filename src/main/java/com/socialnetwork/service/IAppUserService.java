package com.socialnetwork.service;

import com.socialnetwork.model.AppUser;

public interface IAppUserService {
    Iterable<AppUser> getAllUser();
    AppUser getUserById(Long id);
    AppUser save(AppUser user);
    void remove(AppUser user);
    AppUser getUserByUserName(String userName);
    boolean existsAppUserByUsername(String userName);
    AppUser findTopByOrderByIdDesc();
}
