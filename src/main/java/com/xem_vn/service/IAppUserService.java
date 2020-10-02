package com.xem_vn.service;

import com.xem_vn.model.AppUser;

public interface IAppUserService {
    Iterable<AppUser> getAllUser();
    AppUser getUserById(Long id);
    AppUser save(AppUser role);
    void remove(AppUser role);
}
