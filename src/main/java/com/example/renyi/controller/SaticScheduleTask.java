package com.example.renyi.controller;

import com.example.renyi.mapper.orderMapper;
import com.example.renyi.service.TokenService;
import com.example.renyi.utils.HttpClient;
import com.example.renyi.utils.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
@Controller
public class SaticScheduleTask {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private orderMapper orderMapper;

    //每天凌晨6点执行
    //@Scheduled(cron = "0 0 6 * * ?")

    //每天 早上8点，中午12点 和晚上 10点执行
    //@Scheduled(cron = "0 0 8,12,22 * * ?")

    @Scheduled(cron = "0 0 6 * * ?")
    private void configureTasks() {
        System.err.println("-------------------- 执行静态定时任务开始: " + LocalDateTime.now() + "--------------------");
        try{
            tokenService.refreshToken();
            String access_token = orderMapper.getTokenByAppKey("djrUbeB2");//appKey
            //增加了 专门处理中德的红旗单据 消息订阅 未收到的问题
            List<Map<String,String>> list = orderMapper.getUnuploadList();
            if(list != null && list.size() != 0){
                System.err.println("-------------------- 今天要处理的单据长度: " + list.size() + "--------------------");
                for(Map<String,String> map : list){
                    String code = map.get("code");
                    String auditjson = "{\n" +
                            "  \"param\": {\n" +
                            "    \"voucherCode\": \""+code+"\"\n" +
                            "  }\n" +
                            "}";
                    try{
                        //先弃审
                        String unauditResult = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/UnAudit",auditjson,
                                "djrUbeB2",
                                "F707B3834D9448B2A81856DE4E42357A",
                                access_token);
                        System.err.println("-------------------- 单号: " + code + " 的弃审结果是：" + unauditResult);
                        //再审核
                        String auditResult = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Audit",auditjson,
                                "djrUbeB2",
                                "F707B3834D9448B2A81856DE4E42357A",
                                access_token);
                        System.err.println("-------------------- 单号: " + code + " 的审核结果是：" + auditResult);
                    }catch (Exception e){
                        System.err.println("-------------------- 单号: " + code + " 的弃审审核中出现异常:");
                        e.printStackTrace();
                    }
                }
            }

            // 仁肄 的 定时更新 数据库中的 价格本触发器反写失败的问题
            //tokenService.updateRenyiOrder();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.err.println("-------------------- 执行静态定时任务结束: " + LocalDateTime.now() + "--------------------");
    }
}