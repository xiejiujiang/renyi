package com.example.renyi.controller;

import com.chanjet.openapi.sdk.java.DefaultChanjetClient;
import com.chanjet.openapi.sdk.java.domain.CreateTenantContent;
import com.chanjet.openapi.sdk.java.exception.ChanjetApiException;
import com.chanjet.openapi.sdk.java.request.CreateTenantRequest;
import com.chanjet.openapi.sdk.java.response.CreateTenantResponse;

public class SdkTest {
    public static void main(String[] args) {
        try {
            //创建client
            DefaultChanjetClient defaultChanjetClient = new DefaultChanjetClient("https://openapi.chanjet.com");

            //创建请求对象
            CreateTenantRequest createTenantRequest = new CreateTenantRequest();

            //设置开放平台公共请求参数
            createTenantRequest.setAppKey("58jNuL4M");
            createTenantRequest.setAppSecret("423E90F07754E6B803E316A1DF7A848D");
            createTenantRequest.setRequestUri("/tplus/api/v2/warehouse/Query");
            //createTenantRequest.setRequestUri("/financial/orgAndUser/createTenant");
            createTenantRequest.setContentType("application/json");
            createTenantRequest.setOpenToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpc3YiLCJpc3MiOiJjaGFuamV0IiwidXNlcklkIjoiNjAwMTM0NTc3ODgiLCJvcmdJZCI6IjkwMDE1MzM5NDA4IiwiYWNjZXNzX3Rva2VuIjoiMjFmOTI3NGMtOTIzNi00ODljLTliOTctZDJiOTAwYTA1YjBjIiwiYXVkIjoiaXN2IiwibmJmIjoxNjQ0ODA0NDM3LCJhcHBJZCI6IjU4Iiwic2NvcGUiOiJhdXRoX2FsbCIsImlkIjoiZGM5NjhmNDMtOTQ5My00ZTNkLWEwNmMtOGY5ZjdkMjVjODA5IiwiZXhwIjoxNjQ1MzIyODM3LCJpYXQiOjE2NDQ4MDQ0MzcsIm9yZ0FjY291bnQiOiJ1cXk2aG5wcnIyYWoifQ.2HsiaSdoj2sUXrnybZQHPqa4tON2pupphkvYLibJ28k");

            //设置header参数,接口如无appKey、appSecret、openToken、Content-Type四个参数之外的请求头，则不需要传
            //createTenantRequest.addHeader("key", "value");

            //设置query参数,接口无query参数则不需要传
            //createTenantRequest.addQueryParam("code", "0101010101");

            //设置业务参数对象
            //CreateTenantContent createTenantContent = new CreateTenantContent();
            //createTenantContent.setTenantId("tenant_987... ...");
            //createTenantRequest.setBizContent(createTenantContent);
            //发起请求并响应
            CreateTenantResponse createTenantResponse = defaultChanjetClient.execute(createTenantRequest);
            if (createTenantResponse.getResult()) {
                System.out.println("调用成功。" + createTenantResponse.getResult().toString());
            } else {
                System.out.println("调用失败，原因：" + createTenantResponse.getError().getMsg());
            }
        } catch (ChanjetApiException e) {
            //做异常处理
            System.out.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
