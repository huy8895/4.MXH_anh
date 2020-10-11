package com.socialnetwork.repository;

import com.socialnetwork.model.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface IAppUserRepository extends CrudRepository<AppUser,Long> {
    AppUser findAppUserByUsername(String userName);
    boolean existsAppUserByUsername(String userName);
    AppUser findTopByOrderByIdDesc();
}