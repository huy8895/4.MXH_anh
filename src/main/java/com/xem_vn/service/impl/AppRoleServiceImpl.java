package com.xem_vn.service.impl;

import com.xem_vn.model.AppRole;
import com.xem_vn.repository.IAppRoleRepository;
import com.xem_vn.service.IAppRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppRoleServiceImpl implements IAppRoleService {
    @Autowired
    private IAppRoleRepository roleRepository;
    @Override
    public Iterable<AppRole> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public AppRole getRoleById(long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public AppRole save(AppRole role) {
        return roleRepository.save(role);
    }

    @Override
    public void remove(AppRole role) {
        roleRepository.delete(role);
    }
}
