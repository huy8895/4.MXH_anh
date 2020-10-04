package com.xem_vn.service;

import com.xem_vn.model.Status;

import java.util.Optional;

public interface IStatusService {
    Optional<Status> findByName (String statusName);
}
