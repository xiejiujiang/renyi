package com.example.renyi.controller;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.example.renyi.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ExcelListener extends AnalysisEventListener<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.renyi.controller.ExcelListener.class);

    private User user;

    public ExcelListener(User user){
        this.user = user;
    }

    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        System.out.println("invoke方法被调用");
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(user));
        doSomething(user);
    }

    private void doSomething(User user) {
        //1、入库调用接口
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // datas.clear();//解析结束销毁不用的资源
    }
}