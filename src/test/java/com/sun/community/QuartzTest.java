package com.sun.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class QuartzTest {
    @Autowired
    private Scheduler scheduler;

    @Test
    public void deleteJob(){
        try {
            boolean res=scheduler.deleteJob(new JobKey("alphaJob","alphaJobGroup"));
            System.out.println(res);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
