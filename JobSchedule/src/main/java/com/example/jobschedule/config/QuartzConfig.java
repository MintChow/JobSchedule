package com.example.jobschedule.config;

import com.example.jobschedule.job.MinDailyJob;
import com.example.jobschedule.job.XqDailyJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author MintChow
 * @create 2022-07-31-14:10
 */

@Configuration
public class QuartzConfig {

    private static String JOB_GROUP_NAME = "DAILY_JOBGROUP";
    private static String TRIGGER_GROUP_NAME = "DAILY_TRIGGERGROUP";


    /*
    *
    * 供西樵日报任务（任务详情）
    * */
    @Bean
    public JobDetail xqDailyJobDetail() {
        JobDetail jobDetail = JobBuilder.newJob(XqDailyJob.class)
                .withIdentity("xqDailyJobDetail", JOB_GROUP_NAME)
                .storeDurably() //即使没有Trigger关联时，也不需要删除该JobDetail
                .build();
        return jobDetail;
    }

    @Bean
    public Trigger xqDailyTrigger(){

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("00 30 08 * * ?");

        Trigger trigger= TriggerBuilder.newTrigger()
                .forJob(xqDailyJobDetail())
                .withIdentity("xqDailyTrigger",TRIGGER_GROUP_NAME)
                .withSchedule(cronScheduleBuilder)
                .build();

        return trigger;
    }

    @Bean
    public JobDetail minDailyJobDetail() {
        JobDetail jobDetail = JobBuilder.newJob(MinDailyJob.class)
                .withIdentity("minDailyJobDetail", JOB_GROUP_NAME)
                .storeDurably() //即使没有Trigger关联时，也不需要删除该JobDetail
                .build();
        return jobDetail;
    }

    @Bean
    public Trigger minDailyTrigger(){

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("00 30 13 * * ?");

        Trigger trigger= TriggerBuilder.newTrigger()
                .forJob(minDailyJobDetail())
                .withIdentity("minDailyTrigger",TRIGGER_GROUP_NAME)
                .withSchedule(cronScheduleBuilder)
                .build();

        return trigger;
    }
}
