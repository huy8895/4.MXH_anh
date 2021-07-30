package com.socialnetwork.repository;

import com.socialnetwork.model.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IAppUserRepository extends CrudRepository<AppUser,Long> {
    AppUser findAppUserByUsername(String userName);
    boolean existsAppUserByUsername(String userName);
    AppUser findTopByOrderByIdDesc();
}