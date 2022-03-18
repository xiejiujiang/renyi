package com.example.renyi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.renyi.mapper.orderMapper;
import com.example.renyi.service.BasicService;
import com.example.renyi.utils.HttpClient;
import com.example.renyi.utils.MapToJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
            result = HttpClient.HttpPost("/tplus/api/v2/Project/Create",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result; // result:"31" 成功！
    }


    @Override
    public String getXM(Map<String, String> params) {
        String result = "";
        try {
            String json = "{ param:{\"Code\":\"111\"} }";
            result = HttpClient.HttpPost("/tplus/api/v2/Project/Query2",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonArray =  JSONObject.parseArray(result);
        List<Map> xmList = jsonArray.toJavaList(Map.class);
        return result;
    }


    @Override
    public String createXMClass(Map<String, String> params) {
        String result = "";
        try {
            String json = "{dto:{\"Code\":\"09002\", \"Name\":\"销售二部\", \"Parent\":{\"Code\":\"09\"}}}";
            result = HttpClient.HttpPost("/tplus/api/v2/ProjectClass/Create",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;// result:null
    }

    @Override
    public String getXMClass(Map<String, String> params) {
        String result = "";
        try {
            String json = "{param:{Code:\"09\"}}"; //Name 也可以
            result = HttpClient.HttpPost("/tplus/api/v2/ProjectClass/Query",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;// result:null
    }

    @Override
    public String getWarehouse(Map<String, String> params) {
        String result = "";
        try {
            String json = "{param:{Code:\"09\"}}"; //Name 也可以，  {param:{}} 查所有
            result = HttpClient.HttpPost("/tplus/api/v2/warehouse/Query",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;// result:null
    }

    @Override
    public String createWarehouse(Map<String, String> params) {
        String result = "";
        try {
            //16.0 最简传参，若非 就不传 warehouseType 01：普通仓02：现场仓03：委外仓,其他参数请参照API文档
            String json = "{\"dto\": {\"Code\": \"001\",\"Name\": \"电子设备库\",\"warehouseType\": {\"Code\": \"01\"}}}";
            result = HttpClient.HttpPost("/tplus/api/v2/warehouse/Create",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;// result:"3"     其中3代表新创建的仓库ID
    }
}

