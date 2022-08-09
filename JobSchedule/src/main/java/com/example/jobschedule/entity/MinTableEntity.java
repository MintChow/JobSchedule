package com.example.jobschedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

/**
 * @author MintChow
 * @create 2022-08-10-0:00
 */

@Data
@TableName("夜间最小流量监控表")
public class MinTableEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer number;
    private String address;
    private String platform;
    private String area;
    private BigDecimal springValue;
    private BigDecimal reference;
    private String date;
    private BigDecimal value;
    private String remarks;


}
