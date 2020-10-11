package com.socialnetwork.service;

import com.socialnetwork.model.AppRole;

public interface IAppRoleService {
    Iterable<AppRole> getAllRole();
    AppRole getRoleById(Long id);
    AppRole save(AppRole role);
    void remove(AppRole role);
    AppRole getRoleByName(String roleName);
}
