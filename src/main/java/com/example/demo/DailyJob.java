package com.example.demo;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DailyJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(DailyJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        final String uri = "http://localhost:8080/addTodayExangeRates";
        RestTemplate restTemplate = new RestTemplate();
        LOG.info(restTemplate.getForObject(uri,String.class));
    }
}
