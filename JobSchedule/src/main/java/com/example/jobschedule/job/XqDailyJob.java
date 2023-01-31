package com.example.jobschedule.job;

import com.example.jobschedule.entity.MeterEntity;
import com.example.jobschedule.entity.XqDailyEntity;
import com.example.jobschedule.service.MeterService;
import com.example.jobschedule.service.XqDailyService;
import com.example.jobschedule.util.HttpClientUtil;
import org.json.*;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author MintChow
 * @create 2022-07-31-14:11
 */

public class XqDailyJob extends QuartzJobBean {

    @Autowired
    MeterService meterService;
    @Autowired
    XqDailyService xqDailyService;

    @Autowired
    XqDailyEntity xqDailyEntity;

    ArrayList arrayList1=new ArrayList<>();
    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected synchronized void executeInternal(JobExecutionContext context)  {
//        Date queryDate=new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-02");
            Date queryDate=new Date();
            Calendar calendar1=Calendar.getInstance();
//        for (int j=0;j<3;j++) {
//            calendar1.setTime(new Date());
//            calendar1.add(calendar1.DATE, -j);
//            Date queryDate = calendar1.getTime();
            String queryTime = dayFormat.format(queryDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(queryDate);
            calendar.add(calendar.DATE, 1);
            String tomorrow = dayFormat.format(calendar.getTime());
            calendar.add(calendar.DATE, -2);
            String lastDay = dayFormat.format(calendar.getTime());
//        calendar.add(calendar.DATE,-1);
//        String lastTowDay=dayFormat.format(calendar.getTime());
            List<MeterEntity> meterEntityList = meterService.list();
            for (MeterEntity o : meterEntityList) {
                if (o.getObjectIds() != null && !"".equals(o.getObjectIds()) && !"null".equals(o.getObjectIds())) {
                    if (o.getNumber() >= 2000) {
                        xqDailyEntity.setAddress(o.getAddress());
                        xqDailyEntity.setDate(queryTime);
                        xqDailyEntity.setObjectIds(o.getObjectIds());
                        String todayValue;
                        String lastdayValue;
                        try {
                            todayValue = getXqDailyValue(o.getObjectIds(), queryTime, tomorrow);
                            lastdayValue = getXqDailyValue(o.getObjectIds(), lastDay, queryTime);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if ("".equals(todayValue) || todayValue == null || "".equals(lastdayValue) || lastdayValue == null) {
                            xqDailyEntity.setValue(null);
                        } else {
                            BigDecimal num1 = new BigDecimal(todayValue);
                            BigDecimal num2 = new BigDecimal(lastdayValue);
                            xqDailyEntity.setValue(num1.subtract(num2));
                        }
                        xqDailyEntity.setDate(lastDay);
                        xqDailyService.save(xqDailyEntity);
                    }
                }
            }
//        }


    }
    public String getXqDailyValue(String objectIds, String queryTime,String nextDay) throws JSONException {
        String jsonsub="{\"ObjectIds\":["+objectIds+"],\"StartTime\":\""+queryTime +" 00:00:00\",\"EndTime\":\""+nextDay+ " 00:00:00\",\"Step\":1}";
        String url="http://10.8.5.119:48803/Api/wsmp/v1/scadaQuery/lines";
        String result= HttpClientUtil.doPostJson(url,jsonsub);
        JSONObject json =new JSONObject(result);
        JSONArray jsonArray=json.getJSONArray("Data");
        if (jsonArray.length()==0){
            return null;
        }
        jsonArray=jsonArray.getJSONArray(0);
        JSONObject jsonData=jsonArray.getJSONObject(0);
        String value=Double.toString(jsonData.getJSONArray("Values").getDouble(0));
        return value;
    }


}
