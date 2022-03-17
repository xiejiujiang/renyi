package com.example.renyi.service.impl;

import com.example.renyi.mapper.orderMapper;
import com.example.renyi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private orderMapper orderMapper;

    @Override
    public String getTnameByCode(String code) {
        return "";
    }
}
