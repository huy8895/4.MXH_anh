package com.xem_vn.repository;

import com.xem_vn.model.AppRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IAppRoleRepository extends CrudRepository<AppRole,Long> {
    AppRole getAppRoleByName (String roleName);
}
