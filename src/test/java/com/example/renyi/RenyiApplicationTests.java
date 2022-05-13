package com.example.renyi;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONObject;
import com.example.renyi.controller.HQDemo;
import com.example.renyi.saentity.JsonRootBean;
import com.example.renyi.utils.*;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class RenyiApplicationTests {

    @Test
    void testgetXHD(){ //测试 查询 销货单
        try {
            /*String json = "{\n" +
                    "  \"param\": {\n" +
                    "    \"voucherCode\": \"SA-2022-04-0009\"\n" +
                    "  }\n" +
                    "}";
            String result = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/GetVoucherDTO",json,
                    "A9A9WH1i",
                    "C69A8CD150DA721BCB2DF5176D31E97E",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiNjAwMTM0NTc3ODgiLCJvcmdJZCI6IjkwMDE1OTk5MTMyIiwiYWNjZXNzX3Rva2VuIjoiMjFmOTI3NGMtOTIzNi00ODljLTliOTctZDJiOTAwYTA1YjBjIiwiYXVkIjoiaXN2IiwibmJmIjoxNjQ5OTE0MTM5LCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiZmQ3MjI2YWYtYjgyOC00ZDQ5LTkzN2QtZjg4YmM5NDc4ZTczIiwiZXhwIjoxNjUwNDMyNTM5LCJpYXQiOjE2NDk5MTQxMzksIm9yZ0FjY291bnQiOiJ1OTYxaTVnZmVkbTQifQ.V1N3MDpzH1CuTNcjW6fYHcT_A-FqF-TucMRCIHeROH4");
            System.out.println("result : " + result);

            JSONObject job = JSONObject.parseObject(result);
            JsonRootBean jrb =  job.toJavaObject(JsonRootBean.class);
            System.out.println("jrb : " + jrb.getData().getCode());*/

            /*String ss1 = "{\n" +
                    "  \"param\": {\n" +
                    "    \"voucherCode\": \"A2022050032\"\n" +
                    "  }\n" +
                    "}";

            String voucherCode = "A2022050032";
            String auditjson = "{\"param\": { \"voucherCode\": \" " + voucherCode + " \" } }";//弃审的JSON
            String access_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiMzg5MzE5NTQ0MjcyOTIwIiwib3JnSWQiOiIxMjM0MTEyOTcwNDcxMjk5IiwiYWNjZXNzX3Rva2VuIjoiODY0MjU4ZTMtNmEwYi00YzdhLTg4YjMtM2Q2NTc4NGNiMmE5IiwiYXVkIjoiaXN2IiwibmJmIjoxNjUyMzgyMDAyLCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiOGZlYzI1YWItZmFlZi00MmJhLTkwMjktNGFhNTJlOWVlMjBiIiwiZXhwIjoxNjUyOTAwNDAyLCJpYXQiOjE2NTIzODIwMDIsIm9yZ0FjY291bnQiOiJ1aTgwNGdzb2Z6em0ifQ.9VmYVwEd6j5MK2qM3PYahj2BGxV1ALehPLZddel81ko";
            String auditResult = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/UnAudit",ss1,
                    "djrUbeB2",
                    "F707B3834D9448B2A81856DE4E42357A",
                    access_token);
            System.out.println("auditResult == " + auditResult);*/

            /*Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String ddd = sdf.format(date);
            String ts = (System.currentTimeMillis() / 1000L)+"";
            String token = HQDemo.md5(HQDemo.prvkey+HQDemo.prvid+HQDemo.tel + ts);
            String json = "{\"prvid\":\"05192\",\"tel\":\"18428394131\",\"Request_Channel\":\"WEB\",\"method\":\"uploadOrder\",\"timestamp\":\""+ts+"\",\"token\":\""+token+"\",\"datas\":[{\"sign\":\"fb7119355a7f8666fd6f1536dd0e0e1d\",\"lnkshpno\":\"05192A20220500320032\",\"hndno\":\"05192A2022050032\",\"prvid\":\"05192\",\"dptid\":\"003\",\"mkdat\":\""+ddd+"\",\"snddat\":\"20220513\",\"sndusr\":\"18980673186\",\"brief\":\"\",\"items\":[{\"gdsid\":\"18002335\",\"prvgdsid\":\"0100001\",\"qty\":\"1.0000\",\"prvprc\":\"3.75\",\"prvamt\":\"3.75\"}]}]}";


            String js1 = "{\"prvid\":\"05192\",\"tel\":\"18428394131\",\"Request_Channel\":\"WEB\",\"method\":\"uploadOrder\",\"timestamp\":\"1652421807\",\"token\":\"434685972c8a986ff07eed3e87883b07\",\"datas\":[{\"sign\":\"a1670b25acc9a124e45808d83c6d1dc8\",\"lnkshpno\":\"05192A20220500010001\",\"hndno\":\"05192A2022050001\",\"prvid\":\"05192\",\"dptid\":\"001\",\"mkdat\":\"20220513140327\",\"snddat\":\"20220513\",\"sndusr\":\"15378616426\",\"brief\":\"\",\"items\":[{\"gdsid\":\"18002335\",\"prvgdsid\":\"0100001\",\"qty\":\"1.0000\",\"prvprc\":\"6.00\",\"prvamt\":\"6.00\",\"bthno\":\"0\",\"vlddat\":\"0\",\"crtdat\":\"0\"}]}]}";
            String result = HQDemo.request("https://www.hqwg.com.cn:9993/?OAH024", "8aue2u3q", js1.toString());
            String decryptData = Des.desDecrypt("8aue2u3q", result);
            System.out.println("decryptData == " + URLDecoder.decode(decryptData,"UTF-8"));*/

            /*JSONObject resultjob = JSONObject.parseObject("{\"RetCode\":\"200\",\"RetMsg\":\"共1单，成功1单！\",\"Obj\":[],\"Objj\":{\"page\":0,\"size\":0,\"allPage\":0,\"allSize\":0,\"other\":[]}}");
            String Obj = resultjob.getString("Obj");
            System.out.println("Obj == " + Obj);*/


            /*String fileurl = "D:\\123\\tplus.jpg";
            //String img64 = ImageUtils.encodeImgageToBase64(new File("C:\\Chanjet\\TPlusStd\\WebSite\\Attachments\\30903f39-6f65-4707-bcdc-0b0a92d36231.jpg"));
            String img64 = ImageUtils.encodeImgageToBase64(new File(fileurl));
            System.out.println("img64 == " + img64);
            String encodeBase64image = URLEncoder.encode(img64, "UTF-8");
            System.out.println("encodeBase64image == " + encodeBase64image);*/

            /*String img64 = ImageUtils.encodeImgageToBase64(new File("D:\\123\\logo.jpg"));
            String base64 = URLEncoder.encode(img64,"UTF-8");
            String encodeBase64image = URLEncoder.encode(base64, "UTF-8");
            String ts = (System.currentTimeMillis() / 1000L) + "";
            String token = Md5.md5(HQDemo.prvkey + HQDemo.prvid + HQDemo.tel + ts);
            String s12 = "{\"prvid\":\"05192\",\"tel\":\"18428394131\",\"Request_Channel\":\"WEB\",\"method\":\"uploadImage\",\"timestamp\":\""+ts+"\",\"token\":\""+token+"\",\"datas\":[{\"idx\":\"05192A2022050003\",\"hndno\":\"05192A2022050003\",\"img64\":\""+encodeBase64image+"\"}]}";

            String result = HQDemo.request("https://www.hqwg.com.cn:9993/?OAH024", "8aue2u3q", s12);
            String decryptData = Des.desDecrypt("8aue2u3q", result);
            System.out.println("decryptData == " + decryptData);*/


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    void testExcel(){
        try {
            /*Pttt p1 = new Pttt("111",123.0,"d3223","23dse");
            Pttt p2 = new Pttt("222",321.0,"5tgerg","6yhtj");
            Pttt p3 = new Pttt("333",213.123,"gth6h","j776j");
            List<Pttt> ptlist = new ArrayList<Pttt>();//最终用来写入的数据
            ptlist.add(p1);
            ptlist.add(p2);
            ptlist.add(p3);

            File pttexcel = new File("D:\\renyi\\renyi\\src\\main\\resources\\excel\\123.xlsx");

            *//*if(pttexcel.exists()){
                pttexcel.delete();
            }
            pttexcel.createNewFile();*//*

            ExcelWriter excelWriter = EasyExcel.write(pttexcel).build();

            // writeSheet1 = EasyExcel.writerSheet(1,"BBB").build();
            WriteSheet writeSheet2 = EasyExcel.writerSheet(2,"AAA").build();
            //excelWriter.write(ptlist, writeSheet1);
            excelWriter.write(ptlist, writeSheet2);
            excelWriter.finish();*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }




    //测试一下 凭证
    //@Test
    /*void test(){
        try {
            Map<String,Object> dto = new HashMap<String,Object>();
            Map<String,Object> sa = new HashMap<String,Object>();
            sa.put("ExternalCode",Md5.md5("xjj"+System.currentTimeMillis()));
            Map<String,Object> DocType = new HashMap<String,Object>();
            DocType.put("Code","记");
            DocType.put("Name","记");
            sa.put("VoucherDate","2022-03-22");
            //sa.put("AttachedVoucherNum",0); //附件数量
            sa.put("Memo","凭证的备注！！！！！！！！");
            List<Map<String,Object>> Entrys = new ArrayList<Map<String,Object>>();

            Map<String,Object> en1 = new HashMap<String,Object>();//分录1
            en1.put("Summary","提现");//摘要
            Map<String,Object> Account = new HashMap<String,Object>();
            Account.put("Code","112204");//科目  只能末级 ，应收账款
            en1.put("Account",Account);
            Map<String,Object> Currency = new HashMap<String,Object>();
            Currency.put("Code","RMB");//货币编码  如果没有开启币种管理，就是默认 RMB
            en1.put("Currency",Currency);
            en1.put("AmountCr",999);//贷方
            //en1.put("AmountDr",0);//借方
            List<Map<String,Object>> AuxInfos = new ArrayList<Map<String,Object>>();
            Map<String,Object> fzx = new HashMap<String,Object>();

            Map<String,Object> AuxAccDepartmentMap = new HashMap<String,Object>();
            AuxAccDepartmentMap.put("Code","999");
            fzx.put("AuxAccDepartment",AuxAccDepartmentMap);

            Map<String,Object> AuxAccPersonMap = new HashMap<String,Object>();
            AuxAccPersonMap.put("Code","222");
            fzx.put("AuxAccPerson",AuxAccPersonMap);

            Map<String,Object> AuxAccCustomerMap = new HashMap<String,Object>();
            AuxAccCustomerMap.put("Code","0003001");
            fzx.put("AuxAccCustomer",AuxAccCustomerMap);

            AuxInfos.add(fzx);
            en1.put("AuxInfos",AuxInfos);//辅助项
            Entrys.add(en1);


            Map<String,Object> en2 = new HashMap<String,Object>();//分录2
            en2.put("Summary","提现");//摘要
            Map<String,Object> Account2 = new HashMap<String,Object>();
            Account2.put("Code","100102");//科目  只能末级 ，应收账款
            en2.put("Account",Account2);
            Map<String,Object> Currency2 = new HashMap<String,Object>();
            Currency2.put("Code","RMB");//货币编码  如果没有开启币种管理，就是默认 RMB
            en2.put("Currency",Currency2);
            //en2.put("AmountCr",0);//贷方
            en2.put("AmountDr",999);//借方
            List<Map<String,Object>> AuxInfos2 = new ArrayList<Map<String,Object>>();
            Map<String,Object> AuxAccDepartment2 = new HashMap<String,Object>();//部门辅助项
            Map<String,Object> map4 = new HashMap<String,Object>();
            map4.put("Code","999");//部门辅助项
            AuxAccDepartment2.put("AuxAccDepartment",map4);//部门辅助项
            AuxInfos2.add(AuxAccDepartment2);//部门辅助项
            en2.put("AuxInfos",AuxInfos2);//辅助项
            Entrys.add(en2);

            sa.put("Entrys",Entrys);
            sa.put("DocType",DocType);
            dto.put("dto",sa);
            String js = JSONObject.toJSONString(dto);
            System.out.println(js);
            String result = HttpClient.HttpPost("/tplus/api/v2/doc/ReturnCodeCreate",js,
                    "A9A9WH1i",
                    "C69A8CD150DA721BCB2DF5176D31E97E",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiNjAwMTM0NTc3ODgiLCJvcmdJZCI6IjkwMDE1OTk5MTMyIiwiYWNjZXNzX3Rva2VuIjoiMjFmOTI3NGMtOTIzNi00ODljLTliOTctZDJiOTAwYTA1YjBjIiwiYXVkIjoiaXN2IiwibmJmIjoxNjQ3OTI5NjIyLCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiN2Y3NDJkZWQtMTgzOC00NjMzLWJiZjctMTUwNjU2MjM3ZmZiIiwiZXhwIjoxNjQ4NDQ4MDIyLCJpYXQiOjE2NDc5Mjk2MjIsIm9yZ0FjY291bnQiOiJ1OTYxaTVnZmVkbTQifQ.BVm54DbAhxFDhI3WYN63ECsX3S7M9p93BNB4LGBlgss");
            System.out.println("result : " + result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/


    @Test
    void testXHD(){ //测试创建 销货单
        /*try {
            String json = MapToJson.getSAparamsJson();
            String result = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Create",json,
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiNjAwMTM0NTc3ODgiLCJvcmdJZCI6IjkwMDE1OTk5MTMyIiwiYWNjZXNzX3Rva2VuIjoiMjFmOTI3NGMtOTIzNi00ODljLTliOTctZDJiOTAwYTA1YjBjIiwiYXVkIjoiaXN2IiwibmJmIjoxNjQ2OTgzMTAzLCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiNjc2M2JhYTAtYTY2OS00YjNlLWJmYTEtMzBhNTY4MTNiNDIzIiwiZXhwIjoxNjQ3NTAxNTAzLCJpYXQiOjE2NDY5ODMxMDMsIm9yZ0FjY291bnQiOiJ1OTYxaTVnZmVkbTQifQ.q2LKmN8YfJHwgJabHCQaXVFfTvvHkHVeQ7Zx5jwBF7I");
            System.out.println("result : " + result);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }


    @Test
    public void testRefreshToken() throws Exception{
        /*Map<String,String> params = new HashMap<String,String>();
        params.put("grantType","refresh_token");
        params.put("appKey","A9A9WH1i");
        params.put("refreshToken","84b5dccf757a4a0c82bf843874d64f39");
        String result = HttpClient.doGeturlparams("https://openapi.chanjet.com/auth/refreshToken", params);
        System.out.println("result == " + result);*/
    }


    @Test
    public void testHQ() throws Exception{
        /*Map<String,String> map = new HashMap<String,String>();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("method","uploadOrder");
        params.put("Request_Channel","WEB");
        params.put("prvid","00001");//供应商编码
        params.put("tel","15828574775");
        //prvKey 由供应商编码登录后在[用户中心]-[直配接口设置]设置或生成。（红旗对tel赋权之后才能设置prvKey）
        //params.put("token",Md5.md5(prvKey + prvid + "15828574775" + "2021-07-21 09:39:46"));
        params.put("token",Md5.md5("00001" + "00001" + "15828574775" + "2021-07-21 09:39:46"));
        params.put("timestamp","2021-07-21 09:39:46");
        List<Map<String,Object>> datalist = new ArrayList<Map<String,Object>>();
        Map<String,Object> orderMap1 = new HashMap<String,Object>();//代表一个订单
        orderMap1.put("lnkshpno","12345678901234567890");
        orderMap1.put("hndno","0000112345678901");
        orderMap1.put("prvid","00001");//供应商编码
        orderMap1.put("dptid","001");
        orderMap1.put("mkdat","2021-07-21 09:39:46");
        //orderMap1.put("sndusr","送货人（可为空，11位手机号。详见1.7注3）");
        orderMap1.put("snddat","20210721");
        //orderMap1.put("brief","备注（可为空，最长为40位）");

        List<Map<String,Object>> itemsList = new ArrayList<Map<String,Object>>();//代表一个订单里面的商品明细
        Map<String,Object> sp1 = new HashMap<String,Object>();//第一个商品
        sp1.put("gdsid","00001");
        sp1.put("prvgdsid","00001");
        sp1.put("qty","1");
        sp1.put("prvprc","100");
        sp1.put("prvamt","100");
        sp1.put("bthno","20210721");
        sp1.put("vlddat","30");
        sp1.put("crtdat","20210721");
        itemsList.add(sp1);
        orderMap1.put("items",itemsList);
        orderMap1.put("sign",Md5.md5(JSONObject.toJSONString(orderMap1)));//这段代码的位置 可能会有影响！
        datalist.add(orderMap1);
        params.put("datas",datalist);
        String jsondata = JSONObject.toJSONString(params);
        String json1 = Des.desEncrypt("6wHgSX54",jsondata);
        System.out.println("json1 == " + json1);
        map.put("json",json1);
        String reslut = HttpClient.doPostTestFour("https://www.hqwg.com.cn:9993/?OAH024",map);
        System.out.println("reslut == " + reslut);*/
    }
}





















