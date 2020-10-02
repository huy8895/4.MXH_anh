package com.xem_vn.repository;

import com.xem_vn.model.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IAppUserRepository extends CrudRepository<AppUser,Long> {
    Optional<AppUser> findAppUserByUsername(String userName);
}