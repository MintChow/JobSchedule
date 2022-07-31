package com.example.jobschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JobScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobScheduleApplication.class, args);
    }


}
