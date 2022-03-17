package com.example.renyi.service.impl;

import com.example.renyi.mapper.orderMapper;
import com.example.renyi.service.BasicService;
import com.example.renyi.utils.HttpClient;
import com.example.renyi.utils.MapToJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BasicServiceImpl implements BasicService {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.renyi.service.impl.BasicServiceImpl.class);

    @Autowired
    private orderMapper orderMapper;

    @Override
    public String createXM(Map<String, String> params) {
        String result = "";
        try {
            String json = MapToJson.getXMStrByMap(params);//这个参数只能 单独来 设置 生成。
            result = HttpClient.HttpPost("/tplus/api/v2/Project/Create",json,params.get("AppKey"),params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public String getXM(Map<String, String> params) {
        String result = "";
        try {
            String json = MapToJson.getXMStrByMap(params);//这个参数只能 单独来 设置 生成。
            result = HttpClient.HttpPost("/tplus/api/v2/Project/Query2",json,params.get("AppKey"),params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }




}
