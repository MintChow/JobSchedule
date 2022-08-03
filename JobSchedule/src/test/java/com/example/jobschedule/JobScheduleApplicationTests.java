package com.example.jobschedule;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class JobScheduleApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;


    @Test
    void contextLoads() throws JSONException {
        Map<String, String> param=new HashMap<>();
        param.put("fmAddress","1811211494");
        param.put("startTime","2022-07-29");
        param.put("endTime","2022-08-04");
        param.put("interval","1");
        param.put("analytype","instant");
        param.put("panel","analysisMeters");
        String url="http://zsgs.dmas.cn/getData/getFMReadData.ashx";
        String result=HttpClientUtil.doPost(url,param);
        JSONArray json =new JSONArray(result);


//        Long aLong = jdbcTemplate.queryForObject("select count(*) from `供水`", Long.class);
//        log.info("记录总数：{}",aLong);
//        log.info("数据源:{}",dataSource.getClass());
        System.out.println(json);


    }

}
