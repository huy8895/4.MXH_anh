package com.socialnetwork.service.impl;

import com.socialnetwork.repository.IStatusRepository;
import com.socialnetwork.service.IStatusService;
import com.socialnetwork.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class StatusServiceImpl implements IStatusService {
    @Autowired
    IStatusRepository statusRepository;
    @Override
    public Optional<Status> findByName(String statusName) {
        return statusRepository.findByName(statusName);
    }
}
