package com.socialnetwork.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskSchedule {
    @Scheduled(fixedDelay = 60000)
    public void scheduleFixedDelayTask() {
        log.info("TaskSchedule fixedDelay = 60000");
    }
}
