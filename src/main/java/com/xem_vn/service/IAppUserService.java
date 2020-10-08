package com.xem_vn.service;

import com.xem_vn.model.AppUser;

import java.util.Optional;

public interface IAppUserService {
    Iterable<AppUser> getAllUser();
    AppUser getUserById(Long id);
    AppUser save(AppUser user);
    void remove(AppUser user);
    AppUser getUserByUserName(String userName);
    boolean existsAppUserByUsername(String userName);
    AppUser findTopByOrderByIdDesc();
}
