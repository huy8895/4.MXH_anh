package com.xem_vn.service.impl;

import com.xem_vn.model.Status;
import com.xem_vn.repository.IStatusRepository;
import com.xem_vn.service.IStatusService;
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
