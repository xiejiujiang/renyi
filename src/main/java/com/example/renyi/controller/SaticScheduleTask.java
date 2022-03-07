package com.example.renyi.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    //@Scheduled(cron = "0 0 1 * * ?")//3.添加定时任务

    @Scheduled(cron = "0 */2 * * * ?")//每隔 2 分钟
    private void configureTasks() {
        System.err.println("-------------------- 执行静态定时任务时间: " + LocalDateTime.now() + "--------------------");
    }
}
