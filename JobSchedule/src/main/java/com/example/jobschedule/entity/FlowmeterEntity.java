package com.example.jobschedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("流量计统计表")
public class FlowmeterEntity {

    @TableId(value = "number",type = IdType.ID_WORKER)
    private Integer id;
    private Integer number;
    private String address;
    private String platform;
    private String area;
    private BigDecimal springValue;
    private BigDecimal reference;
    private BigDecimal referenceRatio;
    private String fmAddress;
    private String pressureId;
    private String insAegId;
    private String insNegId;
    private String accAegId;
    private BigDecimal filterValue;
    private String remarks;

}
