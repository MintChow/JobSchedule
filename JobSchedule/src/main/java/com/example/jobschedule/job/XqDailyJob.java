package com.example.jobschedule.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.sql.DataSource;

/**
 * @author MintChow
 * @create 2022-07-31-14:11
 */
public class XqDailyJob extends QuartzJobBean {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Long aLong = jdbcTemplate.queryForObject("select count(*) from `供水`", Long.class);
        System.out.println(aLong);
        System.out.println(dataSource.getClass());
    }
}
