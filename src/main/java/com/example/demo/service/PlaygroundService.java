package com.example.demo.service;

import com.example.demo.DailyJob;
import com.example.demo.info.TimerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaygroundService {
    private final SchedulerService scheduler;

    @Autowired
    public PlaygroundService(final SchedulerService scheduler) {
        this.scheduler = scheduler;
    }

    public void runSchedulerJob() {
        final TimerInfo info = new TimerInfo();
        info.setRunForever(true);
        info.setRepeatIntervalMs(1000*60*60*24);
        info.setInitialOffsetMs(1000);
        scheduler.schedule(DailyJob.class, info);
    }
}