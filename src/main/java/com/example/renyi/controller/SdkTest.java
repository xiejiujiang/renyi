package com.example.renyi.controller;

import com.alibaba.fastjson.JSONObject;
import com.chanjet.openapi.sdk.java.DefaultChanjetClient;
import com.chanjet.openapi.sdk.java.domain.CreateTenantContent;
import com.chanjet.openapi.sdk.java.exception.ChanjetApiException;
import com.chanjet.openapi.sdk.java.request.CreateTenantRequest;
import com.chanjet.openapi.sdk.java.response.CreateTenantResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SdkTest {
    public static void main(String[] args) {
        try {
            Map<String,Object> dto = new HashMap<String,Object>();
            Map<String,Object> sa = new HashMap<String,Object>();
            Map<String,Object> Department = new HashMap<String,Object>();
            Department.put("Code","HY");//部门编码
            sa.put("Department",Department);
            Map<String,Object> Clerk = new HashMap<String,Object>();
            Clerk.put("Code","CD-030");//业务员编码
            sa.put("Clerk",Clerk);
            sa.put("VoucherDate","2022-03-12");//单据日期
            sa.put("ExternalCode","Taobao-202006150335598");//外部订单号，不可以重复（MD5，建议记录）
            Map<String,Object> Customer = new HashMap<String,Object>();
            Customer.put("Code","CD-030");//客户编码
            sa.put("Customer",Customer);
            Map<String,Object> SettleCustomer = new HashMap<String,Object>();
            SettleCustomer.put("Code","CD-030");//结算客户编码（一般等同于 客户编码）
            sa.put("SettleCustomer",SettleCustomer);
            Map<String,Object> BusinessType = new HashMap<String,Object>();
            BusinessType.put("Code","15");//业务类型编码，15–普通销售；16–销售退货
            sa.put("BusinessType",BusinessType);
            Map<String,Object> InvoiceType = new HashMap<String,Object>();
            InvoiceType.put("Code","01");//票据类型，枚举类型；00--普通发票，01--专用发票，02–收据；为空时，默认按收据处理
            sa.put("InvoiceType",InvoiceType);
            Map<String,Object> Warehouse = new HashMap<String,Object>();
            Warehouse.put("Code","0101010101");//表头上的 仓库编码
            sa.put("Warehouse",Warehouse);
            Map<String,Object> ReciveType = new HashMap<String,Object>();
            ReciveType.put("Code","00");//收款方式，枚举类型；00--限期收款，01--全额订金，02--全额现结，03--月结，04--分期收款，05--其它；
            sa.put("ReciveType",ReciveType);
            Map<String,Object> RdStyle = new HashMap<String,Object>();
            RdStyle.put("Code","201");//出库类别，RdStyleDTO对象，默认为“线上销售”类别； 具体值 我是查的数据库。
            sa.put("RdStyle",RdStyle);
            sa.put("Memo","这是 我的 备注内容，请注意查看！");//备注
            List<Map<String,Object>> SaleDeliveryDetailsList = new ArrayList<Map<String,Object>>();
            Map<String,Object> DetailM1 = new HashMap<String,Object>();
            Map<String,Object> DetailM1Warehouse = new HashMap<String,Object>();
            DetailM1Warehouse.put("code","0101010101");//明细1 的 仓库编码
            DetailM1.put("Warehouse",DetailM1Warehouse);
            Map<String,Object> DetailM1Inventory = new HashMap<String,Object>();
            DetailM1Inventory.put("code","mexjj999");//明细1 的 存货编码
            DetailM1.put("Inventory",DetailM1Inventory);
            Map<String,Object> DetailM1Unit = new HashMap<String,Object>();
            DetailM1Unit.put("Name","个");//明细1 的 存货计量单位
            DetailM1.put("Unit",DetailM1Unit);
            DetailM1.put("Quantity","1");//明细1 的 数量
            DetailM1.put("TaxRate","13");//明细1 的 税率
            DetailM1.put("OrigTaxAmount","9999.00");//明细1 的 含税金额
            DetailM1.put("idsourcevouchertype","43");//明细1 的 来源单据类型ID
            DetailM1.put("sourceVoucherCode","SO-2022-03-0004");//明细1 的 来源单据单据编号
            DetailM1.put("sourceVoucherDetailId","6");//明细1 的 来源单据单据对应的明细行ID

            //Map<String,Object> DetailM2 = new HashMap<String,Object>();

            SaleDeliveryDetailsList.add(DetailM1);
            //SaleDeliveryDetailsList.add(DetailM2);
            sa.put("SaleDeliveryDetails",SaleDeliveryDetailsList);
            dto.put("dto",sa);
            String js = JSONObject.toJSONString(dto);
            System.out.println(js);

            //创建client
            /*DefaultChanjetClient defaultChanjetClient = new DefaultChanjetClient("https://openapi.chanjet.com");

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
            }*/
        } catch (Exception e) {
            //做异常处理
            System.out.println("调用遭遇异常，原因：" + e.getMessage());
            //throw new RuntimeException(e.getMessage(), e);
        }
    }
}
