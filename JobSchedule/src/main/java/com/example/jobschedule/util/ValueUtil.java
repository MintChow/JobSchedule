package com.example.jobschedule.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ValueUtil {

    static ArrayList arrayList1=new ArrayList<>();

    static ArrayList arrayList2=new ArrayList<>();

    static ArrayList arrayList3=new ArrayList<>();

    static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

    //拓安信获取夜间最小流量值
    public static String getMinDaily(String fmAddress, String queryTime) throws JSONException, ParseException {
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
    //SCADA获取夜间最小流量带无过滤
    public static String getMinDailyBySCADA(String objectIds, String queryTime) throws JSONException {
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


    //SCADA获取夜间最小流量带过滤
    public static String getMinDailyBySCADA(String objectIds, String queryTime,BigDecimal filterValue) throws JSONException {
        String jsonsub="{\"ObjectIds\":["+objectIds+"],\"StartTime\":\""+queryTime +" 02:00:00\",\"EndTime\":\""+queryTime+ " 05:00:00\",\"Step\":1}";
        String url="http://10.8.5.119:48803/Api/wsmp/v1/scadaQuery/lines";
        String result=HttpClientUtil.doPostJson(url,jsonsub);
        JSONObject json =new JSONObject(result);
        JSONArray jsonArray=json.getJSONArray("Data");
        if (jsonArray.length()==0){
            return null;
        }
        jsonArray=jsonArray.getJSONArray(0);
        arrayList3.clear();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonData=jsonArray.getJSONObject(i);
            BigDecimal value=jsonData.getJSONArray("Values").getBigDecimal(0);
            if (value.compareTo(filterValue)>=0){
                arrayList3.add(value);
            }
        }
        if(arrayList3.size()!=0){
            BigDecimal mind= (BigDecimal) Collections.min(arrayList3);
            String min =mind.toPlainString();
            return  min;
        }else {
            return null;
        }
    }

    //SCADA取流量值
    public static String getValueByScada(String objectIds, String queryTime) throws JSONException, ParseException {
        Date queryDate=new SimpleDateFormat("yyyy-MM-dd").parse(queryTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(queryDate);
        calendar.add(calendar.DATE,1);
        String tomorrow=dayFormat.format(calendar.getTime());
        calendar.add(calendar.DATE,1);
        String nextTwoDay=dayFormat.format(calendar.getTime());
        //获取查询日的值
        String jsonsub="{\"ObjectIds\":["+objectIds+"],\"StartTime\":\""+queryTime +" 00:00:00\",\"EndTime\":\""+tomorrow+ " 00:00:00\",\"Step\":1}";
        String url="http://10.8.5.119:48803/Api/wsmp/v1/scadaQuery/lines";
        String result= HttpClientUtil.doPostJson(url,jsonsub);
        JSONObject json =new JSONObject(result);
        JSONArray jsonArray=json.getJSONArray("Data");
        if (jsonArray.length()==0){
            return null;
        }
        jsonArray=jsonArray.getJSONArray(0);
        JSONObject jsonData1=jsonArray.getJSONObject(0);
        Double value1=jsonData1.getJSONArray("Values").getDouble(0);
        //获取查询日后一日值
        jsonsub="{\"ObjectIds\":["+objectIds+"],\"StartTime\":\""+tomorrow +" 00:00:00\",\"EndTime\":\""+nextTwoDay+ " 00:00:00\",\"Step\":1}";
        result= HttpClientUtil.doPostJson(url,jsonsub);
        json =new JSONObject(result);
        jsonArray=json.getJSONArray("Data");
        if (jsonArray.length()==0){
            return null;
        }
        jsonArray=jsonArray.getJSONArray(0);
        JSONObject jsonData2=jsonArray.getJSONObject(0);
        Double value2=jsonData2.getJSONArray("Values").getDouble(0);
        return Double.toString(value2-value1);
    }

    public static String getValue (String objectIds, String queryTime){
        return null;
    }
}
