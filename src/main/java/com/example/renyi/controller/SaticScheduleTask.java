package com.example.renyi.controller;

import com.example.renyi.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@Controller
public class SaticScheduleTask {

    @Autowired
    private TokenService tokenService;
    //@Scheduled(cron = "0 */2 * * * ?")

    //每天凌晨3点执行
    @Scheduled(cron = "0 0 3 * * ?")
    private void configureTasks() {
        System.err.println("-------------------- 执行静态定时任务开始: " + LocalDateTime.now() + "--------------------");
        try{
            //更新 仁肄 数据库里的 单据信息
            //tokenService.updateRenyiOrder();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            tokenService.refreshToken();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.err.println("-------------------- 执行静态定时任务结束: " + LocalDateTime.now() + "--------------------");
    }
}