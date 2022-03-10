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
import com.example.renyi.utils.Des;
import com.example.renyi.utils.HttpClients;
import com.example.renyi.utils.Md5;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class RenyiApplicationTests {

    /*@Test
    void test(){
        try {
            String result = HttpClients.HttpPost("/tplus/api/v2/warehouse/Query","{\n" +
                    "  \"param\": {\n" +
                    "    \"Code\": \"0101010102\"\n" +
                    "  }\n" +
                    "}","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiNjAwMTM0NTc3ODgiLCJvcmdJZCI6IjkwMDE1OTk5MTMyIiwiYWNjZXNzX3Rva2VuIjoiMjFmOTI3NGMtOTIzNi00ODljLTliOTctZDJiOTAwYTA1YjBjIiwiYXVkIjoiaXN2IiwibmJmIjoxNjQ2NjQ4NDc2LCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiMjNhOTUzMjgtNjI3Mi00NTgwLWI5ZGYtOGY3YzE5YTAwZTY2IiwiZXhwIjoxNjQ3MTY2ODc2LCJpYXQiOjE2NDY2NDg0NzYsIm9yZ0FjY291bnQiOiJ1OTYxaTVnZmVkbTQifQ.FcLytiJeEy1_PP-9COuQsSQUB6t3a4OwJu4CjT_P2n8");

            System.out.println("result : " + result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/


    @Test
    void test1(){
        /*String ss = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiNjAwMTM0NTc3ODgiLCJvcmdJZCI6IjkwMDE1OTk5MTMyIiwiYWNjZXNzX3Rva2VuIjoiMjFmOTI3NGMtOTIzNi00ODljLTliOTctZDJiOTAwYTA1YjBjIiwiYXVkIjoiaXN2IiwibmJmIjoxNjQ2NjQ4NDc2LCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiMjNhOTUzMjgtNjI3Mi00NTgwLWI5ZGYtOGY3YzE5YTAwZTY2IiwiZXhwIjoxNjQ3MTY2ODc2LCJpYXQiOjE2NDY2NDg0NzYsIm9yZ0FjY291bnQiOiJ1OTYxaTVnZmVkbTQifQ.FcLytiJeEy1_PP-9COuQsSQUB6t3a4OwJu4CjT_P2n8";
        System.out.println("ss.length == " + ss.length());

        String xm = "华为体验店供货平台";
        String s1 = "四川省 成都市 锦江区 四川省成都市锦江区锦华万达广场仁肄华为授权体验店（样机专用）——四川省成都市锦江区锦华路一段68号1幢1层1011-1";
        if(s1.contains("成都市锦江区锦华万达") && (xm.contains("MSC供货") || s1.contains("样机"))){
            System.out.println("s1.length == " + s1.length());
        }*/

        String param = "{\"VoucherId\":\"1708\",\"MenuCode\":\"SA04\"}";
        JSONObject jsonObject = JSONObject.parseObject(param);
        System.out.println("VoucherId === " + jsonObject.getString("VoucherId"));

    }


    @Test
    public void getTokenByCode() throws Exception{
        /*String result = "";
        String json = "";
        URL realUrl = new URL("https://openapi.chanjet.com/auth/getToken");
        URLConnection conn = realUrl.openConnection();
        conn.setRequestProperty("user-agent","Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("redirectUri", "http://mexjj.natapp1.cc/renyi/token/recode");
        conn.setRequestProperty("code", "c-cf72d37f715b4565b105113e9f76dae6");
        conn.setRequestProperty("appKey", "A9A9WH1i");
        conn.setRequestProperty("grantType", "authorization_code");
        //conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        //PrintWriter out = new PrintWriter(conn.getOutputStream());
        PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
        out.print(json);
        out.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        out.close();
        in.close();
        System.out.println("result == " + result);*/
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
