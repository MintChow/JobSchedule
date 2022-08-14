package com.example.jobschedule.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author MintChow
 * @create 2022-08-07-23:55
 */

@Data
@TableName("夜间最小流量监控表统计表")
public class MeterEntity {

    @TableId

    private Integer number;



    private String address;


    private String platform;


    private String area;


    private BigDecimal springValue;


    private BigDecimal reference;


    private String remarks;


    private String fmAddress;


    private String objectIds;
}
