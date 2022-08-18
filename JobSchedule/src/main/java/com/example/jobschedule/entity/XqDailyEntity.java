package com.example.jobschedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author MintChow
 * @create 2022-08-10-0:00
 */

@Data
@TableName("供西樵水量日报表")
@Component
public class XqDailyEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "站点ID")
    private String addressId;
    @TableField(value = "站点名称")
    private String address;
    @TableField(value = "日期")
    private String date;
    @TableField(value = "当日水量")
    private BigDecimal value;
    @TableField(value = "检测量编号")
    private String objectIds;


}
