package com.example.renyi.service.impl;

import com.example.renyi.mapper.orderMapper;
import com.example.renyi.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.renyi.service.impl.TokenServiceImpl.class);

    @Autowired
    private orderMapper orderMapper;

    public void testService(){
        LOGGER.error("-------------------------------service调用成功！-------------------------------");
    }

}
