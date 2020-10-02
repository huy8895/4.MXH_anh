package com.xem_vn.repository;

import com.xem_vn.model.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface IAppUserRepository extends CrudRepository<AppUser,Long> {
    AppUser findByUsername(String userName);
}
