package com.example.jobschedule.job;

import com.example.jobschedule.entity.MeterEntity;
import com.example.jobschedule.entity.MinTableEntity;
import com.example.jobschedule.service.MeterService;
import com.example.jobschedule.service.MinTableService;
import com.example.jobschedule.util.HttpClientUtil;
import org.quartz.JobExecutionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MintChow
 * @create 2022-08-07-1:44
 */

public class MinDailyJob extends QuartzJobBean {

    ArrayList arrayList1=new ArrayList<>();
    ArrayList arrayList2=new ArrayList<>();


    @Autowired
    MeterService meterService;

    @Autowired
    MinTableService minTableService;

    MinTableEntity minTableEntity=new MinTableEntity();

    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected synchronized void executeInternal(JobExecutionContext context) {
        //获取需查询的列表
        List<MeterEntity> meterEntityList=meterService.list();


        Calendar calendar=Calendar.getInstance();
//        for (int j=0;j<3;j++) {
//            calendar.setTime(new Date());
//            calendar.add(calendar.DATE, -j);
//            String queryTime = dayFormat.format(calendar.getTime());
        String queryTime=dayFormat.format(new Date());
            for (MeterEntity o : meterEntityList) {
                if (o.getFmAddress() != null && !"".equals(o.getFmAddress()) && !"null".equals(o.getFmAddress())) {
                    try {
                        minTableEntity.setNumber(o.getNumber());
                        minTableEntity.setAddress(o.getAddress());
                        minTableEntity.setPlatform(o.getPlatform());
                        minTableEntity.setArea(o.getArea());
                        minTableEntity.setSpringValue(o.getSpringValue());
                        minTableEntity.setReference(o.getReference());
                        minTableEntity.setRemarks(o.getRemarks());
                        minTableEntity.setDate(queryTime);
                        String min = getMinDaily(o.getFmAddress(), queryTime);
                        if ("" != min && min != null) {
                            minTableEntity.setValue(new BigDecimal(min));
                        } else {
                            minTableEntity.setValue(null);
                        }
                        minTableEntity.setRemarks(o.getRemarks());
                        minTableService.save(minTableEntity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (o.getObjectIds() != null && !"".equals(o.getObjectIds()) && !"null".equals(o.getObjectIds())) {
//                if ("3918".equals(o.getObjectIds()) ) {
                    if (o.getNumber() < 2000) {
                        try {
                            minTableEntity.setNumber(o.getNumber());
                            minTableEntity.setAddress(o.getAddress());
                            minTableEntity.setPlatform(o.getPlatform());
                            minTableEntity.setArea(o.getArea());
                            minTableEntity.setSpringValue(o.getSpringValue());
                            minTableEntity.setReference(o.getReference());
                            minTableEntity.setRemarks(o.getRemarks());
                            minTableEntity.setDate(queryTime);
                            String min;
                            if (o.getFilterValue() == null || "".equals(o.getFilterValue())) {
                                min = getMinDailyBySCADA(o.getObjectIds(), queryTime);
                            } else {
                                min = getMinDailyBySCADA(o.getObjectIds(), queryTime, o.getFilterValue());
                            }
                            if ("".equals(min) || min == null) {
                                minTableEntity.setValue(null);
                            } else {
                                minTableEntity.setValue(new BigDecimal(min));
                            }
                            minTableEntity.setRemarks(o.getRemarks());
                            minTableService.save(minTableEntity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
//        }

    }

    //获取当日的该表的02:00-05:00最小流量值，仅拓安信可用
    public String getMinDaily(String fmAddress, String queryTime) throws JSONException, ParseException {

        Long todayZero = dayFormat.parse(queryTime).getTime();
        Long today2h=todayZero+7200000;
        Long today5h=todayZero+18000000;
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
        if(json.length()==0){
            return null;
        }
        Long arr0;
        BigDecimal arr1;
        arrayList1.clear();
        String min;
        for (int i=0;i<json.length();i++){
            arr0=json.getJSONArray(i).getLong(0);
            Long timestamp=arr0;
            arr1=json.getJSONArray(i).getBigDecimal(1);
            if (timestamp>=today2h&&timestamp<=today5h){
                arrayList1.add(arr1);
            }
        }
        if(arrayList1.size()!=0){
            min= Collections.min(arrayList1).toString();
            return  min;
        }else {
            return null;
        }

    }

//    {"ObjectIds":[3918],"StartTime":"2022-08-15 00:00","EndTime":"2022-08-16 00:00","Step":1}
    public String getMinDailyBySCADA(String objectIds, String queryTime) throws JSONException {
        String jsonsub="{\"ObjectIds\":["+objectIds+"],\"StartTime\":\""+queryTime +" 02:00:00\",\"EndTime\":\""+queryTime+ " 05:00:00\",\"Step\":1}";
        String url="http://10.8.5.119:48803/Api/wsmp/v1/scadaQuery/lines";
        String result=HttpClientUtil.doPostJson(url,jsonsub);
        JSONObject json =new JSONObject(result);
        JSONArray jsonArray=json.getJSONArray("Data");
        if (jsonArray.length()==0){
            return null;
        }
        jsonArray=jsonArray.getJSONArray(0);
        arrayList2.clear();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonData=jsonArray.getJSONObject(i);
            BigDecimal value=jsonData.getJSONArray("Values").getBigDecimal(0);
            arrayList2.add(value);
        }
        if(arrayList2.size()!=0){
            BigDecimal mind= (BigDecimal) Collections.min(arrayList2);
            String min =mind.toPlainString();
            return  min;
        }else {
            return null;
        }
    }

    public String getMinDailyBySCADA(String objectIds, String queryTime,BigDecimal filterValue) throws JSONException {
        String jsonsub="{\"ObjectIds\":["+objectIds+"],\"StartTime\":\""+queryTime +" 02:00:00\",\"EndTime\":\""+queryTime+ " 05:00:00\",\"Step\":1}";
        String url="http://10.8.5.119:48803/Api/wsmp/v1/scadaQuery/lines";
        String result=HttpClientUtil.doPostJson(url,jsonsub);
        JSONObject json =new JSONObject(result);
        JSONArray jsonArray=json.getJSONArray("Data");
        if (jsonArray.length()==0){
            return null;
        }
        jsonArray=jsonArray.getJSONArray(0);
        arrayList2.clear();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonData=jsonArray.getJSONObject(i);
            BigDecimal value=jsonData.getJSONArray("Values").getBigDecimal(0);
            if (value.compareTo(filterValue)>=0){
                arrayList2.add(value);
            }
        }
        if(arrayList2.size()!=0){
            BigDecimal mind= (BigDecimal) Collections.min(arrayList2);
            String min =mind.toPlainString();
            return  min;
        }else {
            return null;
        }
    }
}

