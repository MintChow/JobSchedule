package com.example.jobschedule.job;

import com.example.jobschedule.entity.MeterEntity;
import com.example.jobschedule.entity.MinTableEntity;
import com.example.jobschedule.service.MeterService;
import com.example.jobschedule.service.MinTableService;
import com.example.jobschedule.util.HttpClientUtil;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
    protected void executeInternal(JobExecutionContext context) {
        //获取需查询的列表

        String queryTime=dayFormat.format(new Date());
        List<MeterEntity> meterEntityList=meterService.list();
        for (MeterEntity o:meterEntityList) {
            if (o.getFmAddress()!=null&&!"".equals(o.getFmAddress())&&!"null".equals(o.getFmAddress())){
//                String queryTime="2022-08-09";//dayFormat.format(new Date());
                try {
                    minTableEntity.setAddress(o.getAddress());
                    minTableEntity.setPlatform(o.getPlatform());
                    minTableEntity.setArea(o.getArea());
                    minTableEntity.setSpringValue(o.getSpringValue());
                    minTableEntity.setReference(o.getReference());
                    minTableEntity.setRemarks(o.getRemarks());
                    minTableEntity.setDate(queryTime);
                    minTableEntity.setValue(new BigDecimal(getMinDaily(o.getFmAddress(),queryTime)));
                    minTableEntity.setRemarks(o.getRemarks());
                    minTableService.save(minTableEntity);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (o.getObjectIds()!=null&&!"".equals(o.getObjectIds())&&!"null".equals(o.getObjectIds())){
                try {
                    minTableEntity.setAddress(o.getAddress());
                    minTableEntity.setPlatform(o.getPlatform());
                    minTableEntity.setArea(o.getArea());
                    minTableEntity.setSpringValue(o.getSpringValue());
                    minTableEntity.setReference(o.getReference());
                    minTableEntity.setRemarks(o.getRemarks());
                    minTableEntity.setDate(queryTime);
                    minTableEntity.setValue(new BigDecimal(getMinDailyBySCADA(o.getObjectIds(),queryTime)));
                    minTableEntity.setRemarks(o.getRemarks());
                    minTableService.save(minTableEntity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

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
        String arr0;
        String arr1;
        arrayList1.clear();
        String min;
        for (int i=0;i<json.length();i++){
            arr0=json.getJSONArray(i).getString(0);
            Long timestamp=Long.parseLong(arr0);
            arr1=json.getJSONArray(i).getString(1);
            if (timestamp>=today2h&&timestamp<=today5h){
                arrayList1.add(arr1);
            }
        }
        if(arrayList1.size()!=0){
            min= (String) Collections.min(arrayList1);
            return  min;
        }else {
            return "";
        }

    }

//    {"ObjectIds":[3918],"StartTime":"2022-08-15 00:00","EndTime":"2022-08-16 00:00","Step":1}
    public String getMinDailyBySCADA(String objectIds, String queryTime) throws JSONException {
        String jsonsub="{\"ObjectIds\":["+objectIds+"],\"StartTime\":\""+queryTime +" 02:00:00\",\"EndTime\":\""+queryTime+ " 05:00:00\",\"Step\":1}";
        String url="http://10.8.5.119:48803/Api/wsmp/v1/scadaQuery/lines";
        String result=HttpClientUtil.doPostJson(url,jsonsub);
        JSONObject json =new JSONObject(result);
        JSONArray jsonArray=json.getJSONArray("Data");
        jsonArray=jsonArray.getJSONArray(0);
        arrayList2.clear();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonData=jsonArray.getJSONObject(i);
            String value=jsonData.getJSONArray("Values").getString(0);
            arrayList2.add(new BigDecimal(value));
        }
        if(arrayList2.size()!=0){
            BigDecimal mind= (BigDecimal) Collections.min(arrayList2);
            String min =mind.toPlainString();
            return  min;
        }else {
            return "";
        }
    }

}

