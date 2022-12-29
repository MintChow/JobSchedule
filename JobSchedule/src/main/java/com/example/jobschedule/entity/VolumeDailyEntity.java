package com.example.jobschedule.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("站点用水量表")
public class VolumeDailyEntity {

    @TableId
    private Integer id;
    private Integer number;
    private String meterId;
    private String address;
    private String platform;
    private String date;
    private BigDecimal value;

}
