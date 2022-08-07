package com.example.jobschedule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.example.jobschedule.mapper")
public class JobScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobScheduleApplication.class, args);
    }


}
