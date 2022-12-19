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
    void test() throws  org.json.JSONException {
        List<MeterEntity> meterEntityList=meterService.list();
        for (MeterEntity o:
        meterEntityList) {
            if (o.getObjectIds()!=null&&!"".equals(o.getObjectIds())&&!"null".equals(o.getObjectIds())){
                System.out.print(o.getAddress()+"   ");
                System.out.println(a.getMinDailyBySCADA(o.getObjectIds(),"2022-08-14"));
            }
        }
    }
    @Test
    void test2()throws JSONException{
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar=Calendar.getInstance();
        for (int j=1;j<10;j++){
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,-j);
        String queryTime=dayFormat.format(calendar.getTime());
        calendar.add(calendar.DATE,-1);
        String lastDay=dayFormat.format(calendar.getTime());
        calendar.add(calendar.DATE,-1);
        String lastTowDay=dayFormat.format(calendar.getTime());
        List<MeterEntity> meterEntityList=meterService.list();

        for (MeterEntity o:meterEntityList) {
            if (o.getObjectIds()!=null&&!"".equals(o.getObjectIds())&&!"null".equals(o.getObjectIds())){
                if (o.getNumber()>=2000) {
                    xqDailyEntity.setAddress(o.getAddress());
                    xqDailyEntity.setDate(queryTime);
                    xqDailyEntity.setObjectIds(o.getObjectIds());
                    String todayValue;
                    String lastdayValue;
                    todayValue = b.getXqDailyValue(o.getObjectIds(), queryTime, lastDay);
                    lastdayValue = b.getXqDailyValue(o.getObjectIds(), lastDay, lastTowDay);

                    if ("".equals(todayValue) || todayValue == null || "".equals(lastdayValue) || lastdayValue == null) {
                        xqDailyEntity.setValue(null);
                    } else {
                        BigDecimal num1=new BigDecimal(todayValue);
                        BigDecimal num2=new BigDecimal(lastdayValue);
                        xqDailyEntity.setValue(num1.subtract(num2));
                    }
                    xqDailyEntity.setDate(lastDay);
                    xqDailyService.save(xqDailyEntity);
                }
            }
        }
        }
    }
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

    @Test
    void test4(){
        arrayList.add("9.31");
        arrayList.add("9.541");
        arrayList.add("100.5");
        arrayList.add("10.1");
        arrayList.add("2.13456");
        System.out.println(Collections.min(arrayList));

    }

    @Test
    void  test3() {
        Map<String, String> param=new HashMap<>();
        param.put("isSearch","true");
        param.put("gid"," id_public_grid_R00456");
        param.put("start","0");
        param.put("limit","100");
        param.put("exetype","");
        param.put("rid","R00456");
        param.put("report","");
        param.put("worktype","");
        param.put("queryparams","");
        param.put("format","");
        param.put("titles","售水结构月报表汇总");
        param.put("printtype","1");
        param.put("year","2022");
        param.put("month","05");
//        param.put("method","getReportData");
        String url="http://10.8.2.92/mis/statistic/workreport.action?method=getReportData";

        String result= HttpClientUtil.doPost(url,param);
        System.out.println(result);
    }

    @Test
    void contextLoads() throws  ParseException {
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
                minTableEntity.setAddress(o.getAddress());
                minTableEntity.setPlatform(o.getPlatform());
                minTableEntity.setArea(o.getArea());
                minTableEntity.setSpringValue(o.getSpringValue());
                minTableEntity.setReference(o.getReference());
                minTableEntity.setRemarks(o.getRemarks());
                minTableEntity.setDate(queryTime);

//                        minTableEntity.setValue(a.getMinDaily(o.getFmAddress(),queryTime));
                minTableEntity.setRemarks(o.getRemarks());
                minTableService.save(minTableEntity);
//                        System.out.print(o.getAddress()+"             ");
//                        System.out.println(a.getMinDaily(o.getFmAddress(),queryTime));
            }
            }


//        Long aLong = jdbcTemplate.queryForObject("select count(*) from `供水`", Long.class);
//        log.info("记录总数：{}",aLong);
//        log.info("数据源:{}",dataSource.getClass());
//        System.out.println(json);


    }

}
