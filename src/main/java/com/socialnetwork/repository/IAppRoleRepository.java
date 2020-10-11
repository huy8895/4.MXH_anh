package com.socialnetwork.repository;

import com.socialnetwork.model.AppRole;
import org.springframework.data.repository.CrudRepository;

public interface IAppRoleRepository extends CrudRepository<AppRole,Long> {
    AppRole getAppRoleByName (String roleName);
}
