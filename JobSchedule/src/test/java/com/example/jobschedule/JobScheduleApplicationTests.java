package com.example.jobschedule;

import com.example.jobschedule.entity.MeterEntity;
import com.example.jobschedule.entity.MinTableEntity;
import com.example.jobschedule.entity.XqDailyEntity;
import com.example.jobschedule.job.MinDailyJob;
import com.example.jobschedule.job.XqDailyJob;
import com.example.jobschedule.service.MeterService;
import com.example.jobschedule.service.MinTableService;
import com.example.jobschedule.service.XqDailyService;
import com.example.jobschedule.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.sql.DataSource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@SpringBootTest
class JobScheduleApplicationTests {

    @Autowired
    MeterService meterService;
    @Autowired
    XqDailyService xqDailyService;

    @Autowired
    XqDailyEntity xqDailyEntity;

    ArrayList arrayList1=new ArrayList<>();
    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    DataSource dataSource;

    XqDailyJob b=new XqDailyJob();

    @Autowired
    MinTableService minTableService;


    MinTableEntity minTableEntity=new MinTableEntity();

    MinDailyJob a=new MinDailyJob();


    ArrayList arrayList=new ArrayList<>();




    @Test
    void test6(){
        Date queryDate=new Date(122,7,16);
        String queryTime=dayFormat.format(queryDate);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(queryDate);
        calendar.add(calendar.DATE,1);
        String tomorrow=dayFormat.format(calendar.getTime());
        calendar.add(calendar.DATE,-2);
        String lastDay=dayFormat.format(calendar.getTime());
        System.out.println(queryTime+"\n"+tomorrow+"\n"+lastDay);
    }





}
