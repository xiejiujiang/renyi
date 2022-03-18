package com.example.renyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@MapperScan(value = "com.example.renyi.mapper")
public class RenyiApplication{

    public static void main(String[] args) {
        // 启动！
        SpringApplication.run(RenyiApplication.class, args);
    }

    /*protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(RenyiApplication.class);
    }*/
}