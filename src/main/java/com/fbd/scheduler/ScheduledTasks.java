package com.fbd.scheduler;

import com.fbd.service.MatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private MatchServiceImpl matchService;

    // This will run every day at 12 PM
    @Scheduled(cron = "0 0 0 * * ?")
    public void runEverydayAt12PM() {
        // delete all match from match collection
        matchService.deleteAllMatches();
    }
}
