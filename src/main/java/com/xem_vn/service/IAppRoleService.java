package com.xem_vn.service;

import com.xem_vn.model.AppRole;

public interface IAppRoleService {
    Iterable<AppRole> getAllRole();
    AppRole getRoleById(long id);
    AppRole save(AppRole role);
    void remove(AppRole role);
}
