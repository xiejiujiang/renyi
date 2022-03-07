package com.example.renyi;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.example.renyi.controller.ExcelListener;
import com.example.renyi.controller.Utils;
import com.example.renyi.entity.Ptt;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@MapperScan(value = "com.example.renyi.mapper")
public class RenyiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        // 启动！
        SpringApplication.run(RenyiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(RenyiApplication.class);
    }
}
