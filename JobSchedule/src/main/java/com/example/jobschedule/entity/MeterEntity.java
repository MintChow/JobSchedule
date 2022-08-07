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
    @TableField("number")
    private Integer number;


    @TableField("address")
    private String address;

    @TableField("platform")
    private String platform;

    @TableField("area")
    private String area;

    @TableField("spring_value")
    private BigDecimal springValue;

    @TableField("reference")
    private BigDecimal reference;

    @TableField("remarks")
    private String remarks;

    @TableField("fmAddress")
    private String fmAddress;
}
