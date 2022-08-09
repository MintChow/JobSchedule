package com.example.jobschedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.jobschedule.entity.MeterEntity;
import com.example.jobschedule.entity.MinTableEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author MintChow
 * @create 2022-08-08-0:08
 */

@Mapper
public interface MinTableMapper extends BaseMapper<MinTableEntity> {
}
