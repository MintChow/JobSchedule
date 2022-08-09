package com.example.jobschedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.jobschedule.entity.MeterEntity;
import com.example.jobschedule.entity.MinTableEntity;
import com.example.jobschedule.job.MinDailyJob;
import com.example.jobschedule.mapper.MeterMapper;
import com.example.jobschedule.service.MeterService;
import com.example.jobschedule.service.MinTableService;
import com.example.jobschedule.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;


import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@SpringBootTest
class JobScheduleApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Autowired
    MeterService meterService;

    @Autowired
    MinTableService minTableService;


    MinTableEntity minTableEntity=new MinTableEntity();

    MinDailyJob a=new MinDailyJob();


    ArrayList arrayList=new ArrayList<>();


    @Test
    void contextLoads() throws JSONException, ParseException {
//        List<String> fmAddressList = jdbcTemplate.queryForList("SELECT fmAddress FROM `夜间最小流量监控表统计表` WHERE NOT ISNULL(fmAddress)", String.class);
//        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
//        long todayZero = dayFormat.parse(dayFormat.format(new Date())).getTime();
//        for (String a:fmAddressList){
//            System.out.println(a);
//        }


//        System.out.println(todayZero);
//        System.out.println(dayFormat.format(new Date()));
//        Map<String, String> param=new HashMap<>();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String queryTime=formatter.format(LocalDateTime.now());
//        param.put("fmAddress","1912216245");
//        param.put("startTime",queryTime);
//        param.put("endTime",queryTime);
//        param.put("interval","1");
//        param.put("analytype","instant");
//        param.put("panel","analysisMeters");
//        String url="http://zsgs.dmas.cn/getData/getFMReadData.ashx";
//        String result=HttpClientUtil.doPost(url,param);
//        JSONArray json =new JSONArray(result);
//        json=json.getJSONArray(0);
//        String arr0;
//        String arr1;
//        ArrayList arrayList = new ArrayList();
//        String min;
//        for (int i=0;i<json.length();i++){
//            arr0=json.getJSONArray(i).getString(0);
//            arr1=json.getJSONArray(i).getString(1);
//
//            arrayList.add(arr1);
//
//        }
//        if(arrayList.size()!=0){
//        min= (String) Collections.min(arrayList);
//        System.out.println(min);
//        }else {
//            System.out.println("null");
//        }



        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<MeterEntity> meterEntityList=meterService.list();
        for (MeterEntity o:meterEntityList) {
            if (o.getFmAddress()!=null&&o.getFmAddress()!=""&&o.getFmAddress()!="null"){
                String queryTime="2022-08-09";//dayFormat.format(new Date());
                    try {
                        minTableEntity.setAddress(o.getAddress());
                        minTableEntity.setPlatform(o.getPlatform());
                        minTableEntity.setArea(o.getArea());
                        minTableEntity.setSpringValue(o.getSpringValue());
                        minTableEntity.setReference(o.getReference());
                        minTableEntity.setRemarks(o.getRemarks());
                        minTableEntity.setDate(queryTime);

                        minTableEntity.setValue(new BigDecimal(a.getMinDaily(o.getFmAddress(),queryTime)));
                        minTableEntity.setRemarks(o.getRemarks());
                        minTableService.save(minTableEntity);
//                        System.out.print(o.getAddress()+"             ");
//                        System.out.println(a.getMinDaily(o.getFmAddress(),queryTime));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }


//        Long aLong = jdbcTemplate.queryForObject("select count(*) from `供水`", Long.class);
//        log.info("记录总数：{}",aLong);
//        log.info("数据源:{}",dataSource.getClass());
//        System.out.println(json);


    }

}
