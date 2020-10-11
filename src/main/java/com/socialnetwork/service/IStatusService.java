package com.socialnetwork.service;

import com.socialnetwork.model.Status;

import java.util.Optional;

public interface IStatusService {
    Optional<Status> findByName (String statusName);
}
