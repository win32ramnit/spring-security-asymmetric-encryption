package com.manish.app.user.scheduler;

import com.manish.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeletionScheduler {

    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserDeletionScheduler.class);

    @Scheduled(cron = "0 0 * * * *") // every hour
    public void deleteMarkedUsers() {
        log.info("Running scheduled deletion task");
        userService.processScheduledDeletions();
    }
}
