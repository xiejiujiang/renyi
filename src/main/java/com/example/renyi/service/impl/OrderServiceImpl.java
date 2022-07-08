package com.example.renyi.service.impl;

import com.example.renyi.mapper.orderMapper;
import com.example.renyi.mapper.renyiMapper;
import com.example.renyi.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private orderMapper orderMapper;

    @Autowired
    private renyiMapper renyiMapper;

    @Override
    public String getTnameByCode(String code) {
        return "";
    }

    @Override
    public String getDistricntKC(String department,String thistotal) {
        Float thisnow = Float.valueOf(thistotal);
        //根据部门判断不同的 地区 SQL
        if(department.contains("成都")){ //除了车业务和全屋的商品。剩下的不超过最高限值 1550万
            Float now = renyiMapper.getDistricntKC("%成都%");
            LOGGER.error("--------------------- 成都 现在的库存金额是："+now+ " ---------------------------");
            if(now > 15500000f || (now+thisnow) > 15500000f) {
                return "0";//不行
            }else{
                return "1";//可以
            }
        }
        if(department.contains("绵阳")){ //除了车业务和全屋的商品。剩下的不超过最高限值 500万
            Float now = renyiMapper.getDistricntKC("%绵阳%");
            LOGGER.error("--------------------- 绵阳 现在的库存金额是："+now+ " ---------------------------");
            if(now > 5000000f || (now+thisnow) > 5000000f) {
                return "0";
            }else{
                return "1";
            }
        }
        if(department.contains("昆明")){ //除了车业务和全屋的商品。剩下的不超过最高限值 1650万
            Float now = renyiMapper.getDistricntKC("%昆明%");
            LOGGER.error("--------------------- 昆明 现在的库存金额是："+now+ " ---------------------------");
            if(now > 16500000f || (now+thisnow) > 16500000f) {
                return "0";
            }else{
                return "1";
            }
        }
        if(department.contains("锦华MSC")){ //除了车业务和全屋的商品。剩下的不超过最高限值 200万
            Float now = renyiMapper.getDistricntKC("%锦华MSC%");
            LOGGER.error("--------------------- 锦华MSC 现在的库存金额是："+now+ " ---------------------------");
            if(now > 2000000f || (now+thisnow) > 2000000f) {
                return "0";
            }else{
                return "1";
            }
        }
        if(department.contains("贵阳")){ //除了车业务和全屋的商品。剩下的不超过最高限值 300万
            Float now = renyiMapper.getDistricntKC("%贵阳%");
            LOGGER.error("---------------------贵阳 现在的库存金额是："+now+ " ---------------------------");
            if(now > 3000000f || (now+thisnow) > 3000000f) {
                return "0";
            }else{
                return "1";
            }
        }
        if(department.contains("上海")){ //除了车业务和全屋的商品。剩下的不超过最高限值 800万
            Float now = renyiMapper.getDistricntKC("%上海%");
            LOGGER.error("---------------------上海 现在的库存金额是："+now+ " ---------------------------");
            if(now > 8000000f || (now+thisnow) > 8000000f) {
                return "0";
            }else{
                return "1";
            }
        }
        if(department.contains("苏州")){ //除了车业务和全屋的商品。剩下的不超过最高限值 500万
            Float now = renyiMapper.getDistricntKC("%苏州%");
            LOGGER.error("---------------------苏州 现在的库存金额是："+now+ " ---------------------------");
            if(now > 5000000f || (now+thisnow) > 5000000f) {
                return "0";
            }else{
                return "1";
            }
        }else{
            return "1";
        }
    }

}