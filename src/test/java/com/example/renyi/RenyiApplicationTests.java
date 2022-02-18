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
import com.example.renyi.utils.HttpClients;
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
    void test(){
        try {
            String result = HttpClients.HttpPost("https://openapi.chanjet.com/tplus/api/v2/warehouse/Query","{\n" +
                    "  \"param\": {\n" +
                    "    \"Code\": \"0101010102\"\n" +
                    "  }\n" +
                    "}","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiNjAwMTM0NTc3ODgiLCJvcmdJZCI6IjkwMDE1MzM5NDA4IiwiYWNjZXNzX3Rva2VuIjoiMjFmOTI3NGMtOTIzNi00ODljLTliOTctZDJiOTAwYTA1YjBjIiwiYXVkIjoiaXN2IiwibmJmIjoxNjQ0ODA0NDM3LCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiZGM5NjhmNDMtOTQ5My00ZTNkLWEwNmMtOGY5ZjdkMjVjODA5IiwiZXhwIjoxNjQ1MzIyODM3LCJpYXQiOjE2NDQ4MDQ0MzcsIm9yZ0FjY291bnQiOiJ1cXk2aG5wcnIyYWoifQ.2HsiaSdoj2sUXrnybZQHPqa4tON2pupphkvYLibJ28k");

            System.out.println("result : " + result);

            //String rr = HttpClients.HttpGet("https://openapi.chanjet.com/accounting/cia/api/v1/user","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiNjAwMTM0NTc3ODgiLCJvcmdJZCI6IjkwMDE1MzM5NDA4IiwiYWNjZXNzX3Rva2VuIjoiMjFmOTI3NGMtOTIzNi00ODljLTliOTctZDJiOTAwYTA1YjBjIiwiYXVkIjoiaXN2IiwibmJmIjoxNjQ0ODA0NDM3LCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiZGM5NjhmNDMtOTQ5My00ZTNkLWEwNmMtOGY5ZjdkMjVjODA5IiwiZXhwIjoxNjQ1MzIyODM3LCJpYXQiOjE2NDQ4MDQ0MzcsIm9yZ0FjY291bnQiOiJ1cXk2aG5wcnIyYWoifQ.2HsiaSdoj2sUXrnybZQHPqa4tON2pupphkvYLibJ28k");
            //System.out.println("rr : " + rr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    void test1(){
        String ss = "123_32432424";
        System.out.println(ss.substring(ss.indexOf("_")+1 ,ss.length()));
    }
}
