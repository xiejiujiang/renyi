package com.example.renyi;

import ch.qos.logback.core.util.FileUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.chanjet.openapi.sdk.java.DefaultChanjetClient;
import com.chanjet.openapi.sdk.java.domain.CreateTenantContent;
import com.chanjet.openapi.sdk.java.exception.ChanjetApiException;
import com.chanjet.openapi.sdk.java.request.CreateTenantRequest;
import com.chanjet.openapi.sdk.java.response.CreateTenantResponse;
import com.example.renyi.entity.TData;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class RenyiApplicationTests {

    @Test
    void contextLoads() {
        try {
            //创建client
            DefaultChanjetClient defaultChanjetClient = new DefaultChanjetClient("https://openapi.chanjet.com");

            //创建请求对象
            CreateTenantRequest createTenantRequest = new CreateTenantRequest();

            //设置开放平台公共请求参数
            createTenantRequest.setAppKey("zMqBZ...");
            createTenantRequest.setAppSecret("5DB7FB61... ...");
            createTenantRequest.setRequestUri("/financial/orgAndUser/createTenant");

            //设置header参数,接口如无appKey、appSecret、openToken、Content-Type四个参数之外的请求头，则不需要传
            createTenantRequest.addHeader("key", "value");

            //设置query参数,接口无query参数则不需要传
            createTenantRequest.addQueryParam("key", "value");

            //设置业务参数对象
            CreateTenantContent createTenantContent = new CreateTenantContent();
            createTenantContent.setTenantId("tenant_987... ...");
            createTenantRequest.setBizContent(createTenantContent);
            //发起请求并响应
            CreateTenantResponse createTenantResponse = defaultChanjetClient.execute(createTenantRequest);
            if (createTenantResponse.getResult()) {
                System.out.println("调用成功。");
            } else {
                System.out.println("调用失败，原因：" + createTenantResponse.getError().getMsg());
            }
        } catch (ChanjetApiException e) {
            //做异常处理
            System.out.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Test
    void gg(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        System.out.println("today == " + today);
    }
}
