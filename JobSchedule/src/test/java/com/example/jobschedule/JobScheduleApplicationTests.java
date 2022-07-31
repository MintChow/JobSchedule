package com.example.jobschedule;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
class JobScheduleApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;


    @Test
    void contextLoads() {
        Long aLong = jdbcTemplate.queryForObject("select count(*) from `供水`", Long.class);
        log.info("记录总数：{}",aLong);
        log.info("数据源:{}",dataSource.getClass());


    }

}
