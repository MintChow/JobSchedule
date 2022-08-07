package com.example.jobschedule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.jobschedule.entity.MeterEntity;
import com.example.jobschedule.mapper.MeterMapper;
import com.example.jobschedule.service.MeterService;
import org.springframework.stereotype.Service;

/**
 * @author MintChow
 * @create 2022-08-08-0:13
 */
@Service
public class MeterServiceImpl extends ServiceImpl<MeterMapper, MeterEntity> implements MeterService {
}
