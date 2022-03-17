package com.example.renyi.service;

import java.util.Map;

public interface BasicService {

    //这个里面 封装了 所有  T+ 基础类的 接口API 调用方法

    //创建项目
    public String createXM(Map<String,String> params);
}
