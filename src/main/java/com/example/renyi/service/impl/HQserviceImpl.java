package com.example.renyi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.renyi.HQorderBack.sa.JsonRootBean;
import com.example.renyi.mapper.orderMapper;
import com.example.renyi.service.HQservice;
import com.example.renyi.utils.HttpClient;
import com.example.renyi.utils.MapToJson;
import com.example.renyi.utils.Md5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HQserviceImpl implements HQservice {

    private static final Logger LOGGER = LoggerFactory.getLogger(HQserviceImpl.class);

    @Autowired
    private orderMapper orderMapper;

    @Override
    public String createTSaOrderByHQ(JsonRootBean jrb,com.example.renyi.saentity.JsonRootBean sajrb) {
        try{
            String json = MapToJson.getSAparamsJson(jrb,sajrb);
            String access_token = orderMapper.getTokenByAppKey("djrUbeB2");//appKey
            String result = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Create",json,
                    "djrUbeB2",
                    "F707B3834D9448B2A81856DE4E42357A",
                    access_token);
            LOGGER.info("调用T+ 创建销货单的接口后返回：" + result);
            JSONObject jon = JSONObject.parseObject(result);
            if("0".equals(jon.getString("code"))){//如果 销货单 创建 成功！ 再调用  审核 功能
                String data = jon.getString("data");
                JSONObject dataJob = JSONObject.parseObject(data);
                String auditjson = "{\n" +
                        "  \"param\": {\n" +
                        "    \"externalCode\": \" " + Md5.md5(dataJob.getString("ID") + dataJob.getString("Code")) + " \",\n" +
                        "    \"voucherID\": \" "+ dataJob.getString("ID") +" \",\n" +
                        "    \"voucherCode\": \" "+ dataJob.getString("Code") +" \"\n" +
                        "  }\n" +
                        "}";

                String auditResult = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Audit",auditjson,
                        "djrUbeB2",
                        "F707B3834D9448B2A81856DE4E42357A",
                        access_token);
            }else{//如果 创建 T+ 销货单 失败！ 应该 怎么办呢？
                String json2 = MapToJson.getSAparamsJson(jrb,sajrb);
                String access_token2 = orderMapper.getTokenByAppKey("djrUbeB2");//appKey
                String result2 = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Create",json2,
                        "djrUbeB2",
                        "F707B3834D9448B2A81856DE4E42357A",
                        access_token2);
                JSONObject jon2 = JSONObject.parseObject(result2);
                if("0".equals(jon2.getString("code"))) {//如果 销货单 创建 成功！ 再调用  审核 功能
                    String data2 = jon2.getString("data");
                    JSONObject dataJob2 = JSONObject.parseObject(data2);
                    String auditjson2 = "{\n" +
                            "  \"param\": {\n" +
                            "    \"externalCode\": \" " + Md5.md5(dataJob2.getString("ID") + dataJob2.getString("Code")) + " \",\n" +
                            "    \"voucherID\": \" "+ dataJob2.getString("ID") +" \",\n" +
                            "    \"voucherCode\": \" "+ dataJob2.getString("Code") +" \"\n" +
                            "  }\n" +
                            "}";
                    HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Audit",auditjson2,
                            "djrUbeB2",
                            "F707B3834D9448B2A81856DE4E42357A",
                            access_token);
                }
                LOGGER.error(" 红旗返回差异后，T+ 根据差异数量 创建 T+销货单 失败了！ 请检查数据：" + json);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "0000";
    }
}
