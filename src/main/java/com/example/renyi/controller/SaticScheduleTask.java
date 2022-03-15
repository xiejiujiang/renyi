package com.example.renyi.controller;

import com.example.renyi.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Controller
public class SaticScheduleTask {

    @Autowired
    private TokenService tokenService;

    //@Scheduled(cron = "0 0 1 * * ?")//3.添加定时任务

    @Scheduled(cron = "0 */2 * * * ?")//每隔 2 分钟
    private void configureTasks() {
        System.err.println("-------------------- 执行静态定时任务时间: " + LocalDateTime.now() + "--------------------");
        //去数据库里面  更新 token
        tokenService.testService();
    }
}