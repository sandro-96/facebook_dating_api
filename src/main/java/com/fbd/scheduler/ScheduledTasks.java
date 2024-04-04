package com.fbd.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    // This will run every day at 12 PM
    @Scheduled(cron = "0 0 12 * * ?")
    public void runEverydayAt12PM() {
    }
}
