package com.example.renyi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.renyi.mapper.orderMapper;
import com.example.renyi.mapper.renyiMapper;
import com.example.renyi.service.TokenService;
import com.example.renyi.utils.HttpClient;
import com.example.renyi.utils.MapToJson;
import com.example.renyi.utils.Md5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.renyi.service.impl.TokenServiceImpl.class);

    @Autowired
    private orderMapper orderMapper;

    @Autowired
    private renyiMapper renyiMapper;


    public String refreshToken(){
        try {
            List<Map<String,String>> orgList = orderMapper.getDBAllOrgList();
            if(orgList != null && orgList.size() != 0){
                for(Map<String,String> org : orgList){
                    Map<String,String> parma = new HashMap<String,String>();
                    parma.put("grantType","refresh_token");
                    parma.put("appKey",org.get("AppKey"));
                    parma.put("refreshToken",org.get("refresh_token"));
                    String result = HttpClient.doGeturlparams("https://openapi.chanjet.com/auth/refreshToken", parma);
                    //将返回的 result 解析出来，写回数据库！,并一定更新 最后的 更新时间 ,其实 只有 refresh_token,token,和 更新时间会变。
                    JSONObject jso = JSONObject.parseObject(result);
                    if("200".equals(jso.get("code").toString())){//调用成功，更新数据库！
                        JSONObject detail = JSONObject.parseObject(jso.get("result").toString());
                        String access_token = detail.get("access_token").toString();
                        String refresh_token = detail.get("refresh_token").toString();
                        String org_id = detail.get("org_id").toString();
                        Map<String,String> updateMap = new HashMap<String,String>();
                        updateMap.put("org_id",org_id);
                        updateMap.put("refresh_token",refresh_token);
                        updateMap.put("access_token",access_token);
                        orderMapper.updateOrgToken(updateMap);
                    }else{
                        LOGGER.error("----------------更新失败，检擦！！！---------------------- " + org.get("org_id").toString());
                    }
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            //如果出异常，就再来一次试试
            try{
                List<Map<String,String>> orgList = orderMapper.getDBAllOrgList();
                if(orgList != null && orgList.size() != 0){
                    for(Map<String,String> org : orgList){
                        Map<String,String> parma = new HashMap<String,String>();
                        parma.put("grantType","refresh_token");
                        parma.put("appKey",org.get("AppKey"));
                        parma.put("refreshToken",org.get("refresh_token"));
                        String result = HttpClient.doGeturlparams("https://openapi.chanjet.com/auth/refreshToken", parma);
                        //将返回的 result 解析出来，写回数据库！,并一定更新 最后的 更新时间 ,其实 只有 refresh_token,token,和 更新时间会变。
                        JSONObject jso = JSONObject.parseObject(result);
                        if("200".equals(jso.get("code").toString())){//调用成功，更新数据库！
                            JSONObject detail = JSONObject.parseObject(jso.get("result").toString());
                            String access_token = detail.get("access_token").toString();
                            String refresh_token = detail.get("refresh_token").toString();
                            String org_id = detail.get("org_id").toString();
                            Map<String,String> updateMap = new HashMap<String,String>();
                            updateMap.put("org_id",org_id);
                            updateMap.put("refresh_token",refresh_token);
                            updateMap.put("access_token",access_token);
                            orderMapper.updateOrgToken(updateMap);
                        }else{
                            LOGGER.error("----------------更新失败，检擦！！！---------------------- " + org.get("org_id").toString());
                        }
                    }
                }
            }catch (Exception ex){
                LOGGER.error("---------------- 再来一次都TM失败了，检查一下看看！ ---------------------- " );
            }
        }
        return "success";
    }


    //处理仁肄的  零售单和销货单上 考核成本没有反写成功的情况
    public void updateRenyiOrder(){
        //先更新零售单据
        renyiMapper.updateRetailDetails();
        //再更新销货单
        //renyimapper.updateSadetails();
    }


    //根据零售单 单号  获取 详细信息 ，然后再创建销货单，并审核。
    public String getRetailToSaleResult(List<String> codelist){
        String resultstr;
        resultstr = "本次一共同步："+codelist.size()+"单";
        int successCodes = 0;
        for(String code : codelist){
            List<Map<String,Object>> dataList = renyiMapper.getRetailDataListByCode(code);//零售单的DATA
            List<Map<String,Object>> settleList = renyiMapper.getRetailSettleListByCode(code);//零售单的结算明细
            //解析出这个 list 的具体数据，做销货单创建准备。
            if(dataList != null && dataList.size() != 0
                    && !"1".equals(dataList.get(0).get("priuserdefnvc6").toString())){//否则 说明 已经传成功了，就不要重复传了。
                try{
                    String sajson = MapToJson.getSAJsonByRetailData(dataList,settleList);
                    String access_token = orderMapper.getTokenByAppKey("CfeqWq1g");//appKey
                    String result = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Create",sajson,
                            "CfeqWq1g",
                            "7CF60DFC31365CFB0A1BDA01071FA399",
                            access_token);
                    LOGGER.info("-------------- 调用T+ 创建 销货单的接口后返回：" + result + " --------------");
                    JSONObject jon = JSONObject.parseObject(result);
                    if("0".equals(jon.getString("code"))){//如果 销货单 创建 成功！ 再 调用  审核 功能
                        //如果创建成功，则先把 原账套里面的 A账同步字段 更新成 1,这样下次再传重复的单子 就不会再 重复创建了。
                        renyiMapper.updateReretailAStateByCode(code);
                        successCodes = successCodes + 1;
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
                                "CfeqWq1g",
                                "7CF60DFC31365CFB0A1BDA01071FA399",
                                access_token);
                    }else{//如果 创建 T+ 销货单 失败！  就再创建 审核 一次!
                        String result2 = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Create",sajson,
                                "CfeqWq1g",
                                "7CF60DFC31365CFB0A1BDA01071FA399",
                                access_token);
                        JSONObject jon2 = JSONObject.parseObject(result2);
                        if("0".equals(jon2.getString("code"))) {//如果 销货单 创建 成功！ 再调用  审核 功能
                            //如果创建成功，则先把 原账套里面的 A账同步字段 更新成 1,这样下次再传重复的单子 就不会再 重复创建了。
                            renyiMapper.updateReretailAStateByCode(code);
                            successCodes = successCodes + 1;
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
                                    "CfeqWq1g",
                                    "7CF60DFC31365CFB0A1BDA01071FA399",
                                    access_token);
                        }
                    }
                }catch (Exception e){
                    LOGGER.info("------------------ 零售单：" + code + " 生成A账销货单报错，请注意检查 ------------------");
                    e.printStackTrace();
                }
            }
        }
        resultstr = resultstr + ",其中 成功了：" + successCodes + "单。";
        return resultstr;
    }
}
