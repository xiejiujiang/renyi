package com.example.renyi;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.example.renyi.controller.ExcelListener;
import com.example.renyi.controller.Utils;
import com.example.renyi.entity.Ptt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class RenyiApplication {

    public static void main(String[] args) {
        // 启动！
        SpringApplication.run(RenyiApplication.class, args);
    }

}
