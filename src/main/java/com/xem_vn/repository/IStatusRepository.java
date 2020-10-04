package com.xem_vn.repository;

import com.xem_vn.model.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IStatusRepository extends CrudRepository<Status,Long> {
    Optional<Status> findByName (String statusName);
}
