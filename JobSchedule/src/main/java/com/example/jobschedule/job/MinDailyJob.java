package com.example.jobschedule.job;

import com.example.jobschedule.util.HttpClientUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author MintChow
 * @create 2022-08-07-1:44
 */
public class MinDailyJob extends QuartzJobBean {
    @Autowired
    ArrayList arrayList;

    @Autowired
    JdbcTemplate jdbcTemplate;

    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //获取需查询的列表
        List<String> fmAddressList = jdbcTemplate.queryForList("SELECT fmAddress FROM `夜间最小流量监控表统计表` WHERE NOT ISNULL(fmAddress)", String.class);
        String queryTime=dayFormat.format(new Date());
        for (String fmAddress:fmAddressList){
            try {
                getMinDaily(fmAddress,queryTime);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

    //获取当日的该表的02:00-05:00最小流量值，仅拓安信可用
    private String getMinDaily(String fmAddress,String queryTime) throws JSONException, ParseException {
        long todayZero = dayFormat.parse(queryTime).getTime();
        Map<String, String> param=new HashMap<>();
        param.put("fmAddress",fmAddress);
        param.put("startTime",queryTime);
        param.put("endTime",queryTime);
        param.put("interval","1");
        param.put("analytype","instant");
        param.put("panel","analysisMeters");
        String url="http://zsgs.dmas.cn/getData/getFMReadData.ashx";
        String result= HttpClientUtil.doPost(url,param);
        JSONArray json =new JSONArray(result);
        json=json.getJSONArray(0);
        String arr0;
        String arr1;
        arrayList.clear();
        String min;
        for (int i=0;i<json.length();i++){
            arr0=json.getJSONArray(i).getString(0);
            arr1=json.getJSONArray(i).getString(1);
            arrayList.add(arr1);
        }
        if(arrayList.size()!=0){
            min= (String) Collections.min(arrayList);
            return  min;
        }else {
            return "null";
        }

    }
}

