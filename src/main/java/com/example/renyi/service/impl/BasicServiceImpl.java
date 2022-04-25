package com.example.renyi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.renyi.controller.HQDemo;
import com.example.renyi.mapper.orderMapper;
import com.example.renyi.saentity.Clerk;
import com.example.renyi.saentity.JsonRootBean;
import com.example.renyi.saentity.SaleDeliveryDetails;
import com.example.renyi.service.BasicService;
import com.example.renyi.utils.Des;
import com.example.renyi.utils.HttpClient;
import com.example.renyi.utils.MapToJson;
import com.example.renyi.utils.Md5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BasicServiceImpl implements BasicService {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.renyi.service.impl.BasicServiceImpl.class);

    @Autowired
    private orderMapper orderMapper;

    @Override
    public String createXM(Map<String, String> params) {
        String result = "";
        try {
            String json = MapToJson.getXMStrByMap(params);//这个参数只能 单独来 设置 生成。
            result = HttpClient.HttpPost("/tplus/api/v2/Project/Create",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // result:"31" 成功！
    }


    @Override
    public String getXM(Map<String, String> params) {
        String result = "";
        try {
            String json = "{ param:{\"Code\":\"111\"} }";
            result = HttpClient.HttpPost("/tplus/api/v2/Project/Query2",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONObject.parseArray(result);
        List<Map> xmList = jsonArray.toJavaList(Map.class);
        return result;
    }


    @Override
    public String createXMClass(Map<String, String> params) {
        String result = "";
        try {
            String json = "{dto:{\"Code\":\"09002\", \"Name\":\"销售二部\", \"Parent\":{\"Code\":\"09\"}}}";
            result = HttpClient.HttpPost("/tplus/api/v2/ProjectClass/Create",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;// result:null
    }

    @Override
    public String getXMClass(Map<String, String> params) {
        String result = "";
        try {
            String json = "{param:{Code:\"09\"}}"; //Name 也可以
            result = HttpClient.HttpPost("/tplus/api/v2/ProjectClass/Query",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;// result:null
    }

    @Override
    public String getWarehouse(Map<String, String> params) {
        String result = "";
        try {
            String json = "{param:{Code:\"09\"}}"; //Name 也可以，  {param:{}} 查所有
            result = HttpClient.HttpPost("/tplus/api/v2/warehouse/Query",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;// result:null
    }

    @Override
    public String createWarehouse(Map<String, String> params) {
        String result = "";
        try {
            //16.0 最简传参，若非 就不传 warehouseType 01：普通仓02：现场仓03：委外仓,其他参数请参照API文档
            String json = "{\"dto\": {\"Code\": \"001\",\"Name\": \"电子设备库\",\"warehouseType\": {\"Code\": \"01\"}}}";
            result = HttpClient.HttpPost("/tplus/api/v2/warehouse/Create",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;// result:"3"     其中3代表新创建的仓库ID
    }

    @Override
    public String createPZ(Map<String, String> params) {
        String result = "";
        try {
            Map<String, Object> dto = new HashMap<String, Object>();
            Map<String, Object> sa = new HashMap<String, Object>();
            sa.put("ExternalCode", Md5.md5("xjj" + System.currentTimeMillis()));
            Map<String, Object> DocType = new HashMap<String, Object>();
            DocType.put("Code", "记");
            DocType.put("Name", "记");
            sa.put("VoucherDate", "2022-03-22");
            //sa.put("AttachedVoucherNum",0); //附件数量
            sa.put("Memo", "凭证的备注！！！！！！！！");
            List<Map<String, Object>> Entrys = new ArrayList<Map<String, Object>>();

            Map<String, Object> en1 = new HashMap<String, Object>();//分录1
            en1.put("Summary", "提现");//摘要
            Map<String, Object> Account = new HashMap<String, Object>();
            Account.put("Code", "112204");//科目  只能末级 ，应收账款
            en1.put("Account", Account);
            Map<String, Object> Currency = new HashMap<String, Object>();
            Currency.put("Code", "RMB");//货币编码  如果没有开启币种管理，就是默认 RMB
            en1.put("Currency", Currency);
            en1.put("AmountCr", 999);//贷方
            //en1.put("AmountDr",0);//借方
            List<Map<String, Object>> AuxInfos = new ArrayList<Map<String, Object>>();
            Map<String, Object> fzx = new HashMap<String, Object>();

            Map<String, Object> AuxAccDepartmentMap = new HashMap<String, Object>();
            AuxAccDepartmentMap.put("Code", "999");
            fzx.put("AuxAccDepartment", AuxAccDepartmentMap);

            Map<String, Object> AuxAccPersonMap = new HashMap<String, Object>();
            AuxAccPersonMap.put("Code", "222");
            fzx.put("AuxAccPerson", AuxAccPersonMap);

            Map<String, Object> AuxAccCustomerMap = new HashMap<String, Object>();
            AuxAccCustomerMap.put("Code", "0003001");
            fzx.put("AuxAccCustomer", AuxAccCustomerMap);

            AuxInfos.add(fzx);
            en1.put("AuxInfos", AuxInfos);//辅助项
            Entrys.add(en1);


            Map<String, Object> en2 = new HashMap<String, Object>();//分录2
            en2.put("Summary", "提现");//摘要
            Map<String, Object> Account2 = new HashMap<String, Object>();
            Account2.put("Code", "100102");//科目  只能末级 ，应收账款
            en2.put("Account", Account2);
            Map<String, Object> Currency2 = new HashMap<String, Object>();
            Currency2.put("Code", "RMB");//货币编码  如果没有开启币种管理，就是默认 RMB
            en2.put("Currency", Currency2);
            //en2.put("AmountCr",0);//贷方
            en2.put("AmountDr", 999);//借方
            List<Map<String, Object>> AuxInfos2 = new ArrayList<Map<String, Object>>();
            Map<String, Object> AuxAccDepartment2 = new HashMap<String, Object>();//部门辅助项
            Map<String, Object> map4 = new HashMap<String, Object>();
            map4.put("Code", "999");//部门辅助项
            AuxAccDepartment2.put("AuxAccDepartment", map4);//部门辅助项
            AuxInfos2.add(AuxAccDepartment2);//部门辅助项
            en2.put("AuxInfos", AuxInfos2);//辅助项
            Entrys.add(en2);

            sa.put("Entrys", Entrys);
            sa.put("DocType", DocType);
            dto.put("dto", sa);
            String json = JSONObject.toJSONString(dto);
            result = HttpClient.HttpPost("/tplus/api/v2/doc/ReturnCodeCreate",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public JsonRootBean getSaOrder(Map<String, String> params) {
        JsonRootBean jrb = new JsonRootBean();
        String code = params.get("code");
        try {
            String json = "{param:{voucherCode:\"" + code + "\"}}"; //{param:{}} 查所有
            String result = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/GetVoucherDTO",
                    json,
                    params.get("AppKey"),
                    params.get("AppSecret"),
                    orderMapper.getTokenByAppKey(params.get("AppKey")));
            JSONObject job = JSONObject.parseObject(result);
            jrb = job.toJavaObject(JsonRootBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jrb;
    }

    @Override
    public String HQsaorder(JsonRootBean jrb) {
        //解析 T+ 的 销货单 DTO。 转成 HQ 的参数，然后调用API 。
        String prvid = HQDemo.prvid;
        String tel = HQDemo.tel;
        String prvkey = HQDemo.prvkey;

        String rand = "" + Math.random();//手工单号（16位。5位供应商编码+11位随机数。不可重复，存在则以此为主键进行修改）
        String hndno = rand.substring(rand.length() - 11, rand.length());//T+的单号？
        String lnkshpno = HQDemo.prvid + rand.substring(rand.length() - 15, rand.length());//真实送货单号（最长20位）
        String dptid = "688";//送货门店 最少3位，不足3位前面补0  销货单上的 客户 编码
        //dptid = jrb.getData().getCustomer().getCode();
        String mkdat = ("" + jrb.getData().getVoucherDate()).replaceAll("-", "");//制单时间

        Clerk clerk = jrb.getData().getClerk();//业务员
        String sndusr = clerk.getCode();//送货人 可为空，11位手机号   只能调接口去查了哦

        String snddat = ("" + jrb.getData().getVoucherDate()).replaceAll("-", "");//送货时间（可能是表头的自定义项）
        String brief = jrb.getData().getMemo();//销货单上的备注

        String ts = (System.currentTimeMillis() / 1000L) + "";

        StringBuffer json = new StringBuffer();
        json.append("{\"prvid\":\"" + prvid + "\",\"tel\":\"" + tel + "\",\"Request_Channel\":\"WEB\",\"method\":\"uploadOrder\",\"timestamp\":\"" + ts + "\",\"token\":\"" + Md5.md5(prvkey + prvid + tel + ts) + "\",\"datas\":[");
        StringBuilder item = new StringBuilder();
        item.append("{\"lnkshpno\":\"" + lnkshpno + "\",\"hndno\":\"" + hndno + "\",\"prvid\":\"" + prvid + "\",\"dptid\":\"" + dptid + "\",\"mkdat\":\"" + mkdat + "\",\"snddat\":\"" + snddat + "\",\"sndusr\":\"" + sndusr + "\",\"brief\":\"" + brief + "\"");
        item.append(",\"items\":[");

        List<SaleDeliveryDetails> saleDeliveryDetails = jrb.getData().getSaleDeliveryDetails();//具体的商品明细
        if (saleDeliveryDetails != null && saleDeliveryDetails.size() != 0) {
            for (int j = 0; j < saleDeliveryDetails.size(); j++) {//具体的商品明细
                SaleDeliveryDetails saleDeliveryDetail = saleDeliveryDetails.get(j);
                String gdsid = saleDeliveryDetail.getPartnerInventoryCode();//红旗商品编码（客户的）
                String prvgdsid = saleDeliveryDetail.getInventory().getCode();//供应商商品编码(T+的)
                String qty = saleDeliveryDetail.getQuantity();//数量
                String prvprc = saleDeliveryDetail.getOrigTaxPrice();//含税单价
                String prvamt = saleDeliveryDetail.getOrigTaxAmount();//含税金额
                item.append("{\"gdsid\":\"" + gdsid + "\",\"prvgdsid\":\"" + prvgdsid + "\",\"qty\":\"" + qty + "\",\"prvprc\":\"" + prvprc + "\",\"prvamt\":\"" + prvamt + "\"},");
            }
        }
        item.setLength(item.length() - 1);
        item.append("]}");
        String sign = Md5.md5(item.toString());
        item.insert(1, "\"sign\":\"" + sign + "\",");
        json.append(item.toString());
        json.append(",");
        json.setLength(json.length() - 1);
        json.append("]}");
        LOGGER.info("红旗 json == " + json);
        String result = HQDemo.request("https://www.hqwg.com.cn:9993/?OAH024", "8aue2u3q", json.toString());
        String decryptData = Des.desDecrypt("8aue2u3q", result);
        LOGGER.info("请求红旗结果：" + decryptData);

        return null;
    }


    public String HQsabackorder(JsonRootBean jrb) {
        //解析 T+ 的 销货单 DTO。 转成 HQ 的参数，然后调用API 。
        String prvid = HQDemo.prvid;
        String tel = HQDemo.tel;
        String prvkey = HQDemo.prvkey;

        String rand = "" + Math.random();//手工单号（16位。5位供应商编码+11位随机数。不可重复，存在则以此为主键进行修改）
        String hndno = rand.substring(rand.length() - 11, rand.length());
        String lnkshpno = "05192" + rand.substring(rand.length() - 15, rand.length());//真实送货单号（最长20位）
        String dptid = "688";//送货门店 最少3位，不足3位前面补0  销货单上的 客户 编码
        //dptid = jrb.getData().getCustomer().getCode();
        String mkdat = ("" + jrb.getData().getVoucherDate()).replaceAll("-", "");//制单时间

        Clerk clerk = jrb.getData().getClerk();//业务员
        String sndusr = clerk.getCode();//送货人 可为空，11位手机号   只能调接口去查了哦

        String snddat = ("" + jrb.getData().getVoucherDate()).replaceAll("-", "");//送货时间（可能是表头的自定义项）
        String brief = jrb.getData().getMemo();//销货单上的备注

        String ts = (System.currentTimeMillis() / 1000L) + "";

        StringBuffer json = new StringBuffer();
        json.append("{\"prvid\":\"" + prvid + "\",\"tel\":\"" + tel + "\",\"Request_Channel\":\"WEB\",\"method\":\"uploadSndbllBackOrder\",\"timestamp\":\"" + ts + "\",\"token\":\"" + Md5.md5(prvkey + prvid + tel + ts) + "\",\"datas\":[");
        StringBuilder item = new StringBuilder();
        item.append("{\"lnkshpno\":\"" + lnkshpno + "\",\"hndno\":\"" + hndno + "\",\"prvid\":\"" + prvid + "\",\"dptid\":\"" + dptid + "\",\"mkdat\":\"" + mkdat + "\",\"snddat\":\"" + snddat + "\",\"sndusr\":\"" + sndusr + "\",\"brief\":\"" + brief + "\"");
        item.append(",\"items\":[");

        List<SaleDeliveryDetails> saleDeliveryDetails = jrb.getData().getSaleDeliveryDetails();//具体的商品明细
        if (saleDeliveryDetails != null && saleDeliveryDetails.size() != 0) {
            for (int j = 0; j < saleDeliveryDetails.size(); j++) {//具体的商品明细
                SaleDeliveryDetails saleDeliveryDetail = saleDeliveryDetails.get(j);
                String gdsid = saleDeliveryDetail.getInventory().getCode();//T+商品编码
                String prvgdsid = saleDeliveryDetail.getInventory().getCode();//T+商品编码
                String qty = saleDeliveryDetail.getQuantity();//数量
                //String prvprc = saleDeliveryDetail.getOrigTaxPrice();//含税单价
                //String prvamt = saleDeliveryDetail.getOrigTaxAmount();//含税金额
                item.append("{\"gdsid\":\"" + gdsid + "\",\"prvgdsid\":\"" + prvgdsid + "\",\"qty\":\"" + qty + "\",\"brief\":\"行备注\"},");
            }
        }
        item.setLength(item.length() - 1);
        item.append("]}");
        String sign = Md5.md5(item.toString());
        item.insert(1, "\"sign\":\"" + sign + "\",");
        json.append(item.toString());
        json.append(",");
        json.setLength(json.length() - 1);
        json.append("]}");
        LOGGER.info("红旗 json == " + json);
        String result = HQDemo.request("https://www.hqwg.com.cn:9993/?OAH024", "8aue2u3q", json.toString());
        String decryptData = Des.desDecrypt("8aue2u3q", result);
        LOGGER.info("请求红旗结果：" + decryptData);
        return "";
    }

    //根据单据编号 获取 附件 ID
    public List<Map<String, String>> getfjidByCode(String code) {
        List<Map<String, String>> ids = orderMapper.getfjidByCode(code);
        return ids;
    }

    public String HQimage(String code, String base64img) {
        try {
            String prvid = HQDemo.prvid;
            String tel = HQDemo.tel;
            String prvkey = HQDemo.prvkey;
            String encodeBase64image = URLEncoder.encode(base64img, "UTF-8");

            String ts = (System.currentTimeMillis() / 1000L) + "";

            StringBuffer json = new StringBuffer();
            json.append("{\"prvid\":\"" + prvid + "\",\"tel\":\"" + tel + "\",\"Request_Channel\":\"WEB\",\"method\":\"uploadImage\",\"timestamp\":\"" + ts + "\",\"token\":\"" + Md5.md5(prvkey + prvid + tel + ts) + "\",\"datas\":[{");
            json.append("\"idx\":\"序号\",\"hndno\":\"" + code + "\",\"img64\":\"" + encodeBase64image + "\",");
            json.append("]}");

            LOGGER.info("调用 红旗 图片上传的 json == " + json);
            String result = HQDemo.request("https://www.hqwg.com.cn:9993/?OAH024", "8aue2u3q", json.toString());
            String decryptData = Des.desDecrypt("8aue2u3q", result);
            LOGGER.info("请求红旗结果：" + decryptData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return "";
        }
    }
}