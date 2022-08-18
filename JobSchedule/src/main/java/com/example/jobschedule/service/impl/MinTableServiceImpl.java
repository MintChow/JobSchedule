package com.example.jobschedule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.jobschedule.entity.MinTableEntity;
import com.example.jobschedule.mapper.MinTableMapper;
import com.example.jobschedule.service.MinTableService;
import org.springframework.stereotype.Service;

/**
 * @author MintChow
 * @create 2022-08-08-0:13
 */
@Service
public class MinTableServiceImpl extends ServiceImpl<MinTableMapper, MinTableEntity> implements MinTableService {
}
