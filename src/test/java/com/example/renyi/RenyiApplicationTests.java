package com.example.renyi;

import ch.qos.logback.core.util.FileUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.openapi.sdk.java.DefaultChanjetClient;
import com.chanjet.openapi.sdk.java.domain.CreateTenantContent;
import com.chanjet.openapi.sdk.java.exception.ChanjetApiException;
import com.chanjet.openapi.sdk.java.request.CreateTenantRequest;
import com.chanjet.openapi.sdk.java.response.CreateTenantResponse;
import com.example.renyi.entity.TData;
import com.example.renyi.utils.*;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.junit.jupiter.api.Test;
import org.omg.DynamicAny.NameValuePair;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriBuilder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class RenyiApplicationTests {


    /*@Test
    void test(){
        try {
            String result = HttpClients.HttpPost("/tplus/api/v2/warehouse/Query",
                    "{\n" +
                    "  \"param\": {\n" +
                    "    \"Code\": \"0101010102\"\n" +
                    "  }\n" +
                    "}","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiNjAwMTM0NTc3ODgiLCJvcmdJZCI6IjkwMDE1OTk5MTMyIiwiYWNjZXNzX3Rva2VuIjoiMjFmOTI3NGMtOTIzNi00ODljLTliOTctZDJiOTAwYTA1YjBjIiwiYXVkIjoiaXN2IiwibmJmIjoxNjQ2OTgzMTAzLCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiNjc2M2JhYTAtYTY2OS00YjNlLWJmYTEtMzBhNTY4MTNiNDIzIiwiZXhwIjoxNjQ3NTAxNTAzLCJpYXQiOjE2NDY5ODMxMDMsIm9yZ0FjY291bnQiOiJ1OTYxaTVnZmVkbTQifQ.q2LKmN8YfJHwgJabHCQaXVFfTvvHkHVeQ7Zx5jwBF7I");

            System.out.println("result : " + result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/


    @Test
    void testXHD(){ //测试创建 销货单
        try {
            String json = MapToJson.getSAparamsJson();
            String result = HttpClients.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Create",json,
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiNjAwMTM0NTc3ODgiLCJvcmdJZCI6IjkwMDE1OTk5MTMyIiwiYWNjZXNzX3Rva2VuIjoiMjFmOTI3NGMtOTIzNi00ODljLTliOTctZDJiOTAwYTA1YjBjIiwiYXVkIjoiaXN2IiwibmJmIjoxNjQ2OTgzMTAzLCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiNjc2M2JhYTAtYTY2OS00YjNlLWJmYTEtMzBhNTY4MTNiNDIzIiwiZXhwIjoxNjQ3NTAxNTAzLCJpYXQiOjE2NDY5ODMxMDMsIm9yZ0FjY291bnQiOiJ1OTYxaTVnZmVkbTQifQ.q2LKmN8YfJHwgJabHCQaXVFfTvvHkHVeQ7Zx5jwBF7I");
            System.out.println("result : " + result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void testRefreshToken() throws Exception{
        Map<String,String> params = new HashMap<String,String>();
        params.put("grantType","refresh_token");
        params.put("appKey","A9A9WH1i");
        params.put("refreshToken","84b5dccf757a4a0c82bf843874d64f39");
        String result = HttpClient.doGeturlparams("https://openapi.chanjet.com/auth/refreshToken", params);
        System.out.println("result == " + result);
    }


    @Test
    public void testHQ() throws Exception{
        /*String result = "";
        String token = Md5.md5("8aue2u3q" + "00001" + "15828574775" + "2022-03-09 12:00:00");
        String datas = "[{\n" +
                "    \"lnkshpno\":\"TEST001002003\",\n" +
                "    \"hndno\":\"0000178787878787\",\n" +
                "    \"prvid\":\"00001\",\n" +
                "    \"dptid\":\"001\",\n" +
                "    \"mkdat\":\"20220309120000\",\n" +
                "    \"sndusr\":\"15828574775\",\n" +
                "    \"snddat\":\"20220309\",\n" +
                "    \"brief\":\"备注（可为空，最长为40位）\",\n" +
                "    \"sign\":\"fjef039hf093r8ghsj98wgh8ujsioef093f8g5h\",\n" +
                "    \"items\":[{ \n" +
                "        \"gdsid\":\"001\",\n" +
                "        \"prvgdsid\":\"001001\",\n" +
                "        \"qty\":\"2\",\n" +
                "        \"prvprc\":\"11\",\n" +
                "        \"prvamt\":\"22\",\n" +
                "        \"bthno\":\"22222222\",\n" +
                "        \"vlddat\":\"365\",\n" +
                "        \"crtdat\":\"20200309\"\n" +
                "    }]\n" +
                "}]";
        String data = "{\"method\":\"uploadOrder\",\"Request_Channel\":\"WEB\",\"prvid\",\"00001\",\"tel\":\"15828574775\",\"token\":"+token+
                ",\"timestamp\":\"2022-03-09 12:00:00\",\"datas\":"+datas+"}";

        String json = Des.desEncrypt("8aue2u3q",data);
        URL realUrl = new URL("https://www.hqwg.com.cn:9993/?OAH024");
        URLConnection conn = realUrl.openConnection();
        conn.setRequestProperty("user-agent","Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        //conn.setRequestProperty("json", json);
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        //PrintWriter out = new PrintWriter(conn.getOutputStream());
        PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
        //out.print(json);
        out.print("{\"json\":"+json+"}");
        out.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        out.close();
        in.close();
        System.out.println(" result ========= " +  Des.desDecrypt("8aue2u3q",result) );*/
    }
}