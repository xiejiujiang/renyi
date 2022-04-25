package com.example.renyi.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.renyi.HQorderBack.sa.JsonRootBean;
import com.example.renyi.SAsubscribe.SACsubJsonRootBean;
import com.example.renyi.mapper.orderMapper;
import com.example.renyi.service.BasicService;
import com.example.renyi.service.HQservice;
import com.example.renyi.utils.AESUtils;
import com.example.renyi.utils.Des;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/token")
public class TokenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.renyi.controller.TokenController.class);


    @Autowired
    private BasicService basicService;

    @Autowired
    private HQservice hqService;

    @Autowired
    private orderMapper orderMapper;

    //这个里面 主要 用来 接受 code ,刷新 token ，更新对应的数据库

    @RequestMapping(value="/recode", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String recode(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("------------------------------- 正式OAuth回调地址  ------------------------------------------");
        String code = request.getParameter("code");
        //第一次授权后，会有这个code,立刻调用 一次 授权码换token接口 ，拿到完整的 token 相关信息，并写入数据库。

        //3月17日思考： 暂时不用接口来访问，直接在线访问后 拿到第一次的数据，并 复制 填入数据库表中接口（后续定时任务来更新）
        return code;
    }


    //T+ 的 消息订阅的接口。
    @RequestMapping(value="/ticket", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String reticket(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("------------------------------- 正式消息接收地址，包含 ticket，消息订阅，授权 ------------------------------------------");
        try{
            InputStreamReader reader=new InputStreamReader(request.getInputStream(),"utf-8");
            BufferedReader buffer=new BufferedReader(reader);
            String params=buffer.readLine();
            JSONObject jsonObject = JSONObject.parseObject(params);
            String encryptMsg = jsonObject.getString("encryptMsg");
            String destr = AESUtils.aesDecrypt(encryptMsg,"123456789012345x");
            // {"id":"AC1C04B100013301500B4A9B012DB2EC","appKey":"A9A9WH1i","appId":"58","msgType":"SaleDelivery_Audit","time":"1649994072443","bizContent":{"externalCode":"","voucherID":"23","voucherDate":"2022/4/15 0:00:00","voucherCode":"SA-2022-04-0011"},"orgId":"90015999132","requestId":"86231b63-f0c2-4de1-86e9-70557ba9cd62"}
            JSONObject job = JSONObject.parseObject(destr);
            if(job.getString("msgType").equals("SaleDelivery_Audit")){
                //销货单的审核
                // 由于  我已经 通过扩展JS 进行 了 审核后的接口调用，故 此处可以 不要了。
                /*SACsubJsonRootBean jrb =  job.toJavaObject(SACsubJsonRootBean.class);//销货单的订阅信息DTO
                String voucherCode = jrb.getBizContent().getVoucherCode();

                String OrgId = jrb.getOrgId();
                //根据 voucherCode 查询 此 销货单的明细内容
                Map<String,String> pas = new HashMap<String,String>();
                pas.put("OrgId",OrgId);
                pas.put("code",voucherCode);//销货单的单号
                // 通过 OrgId 来获取 AppKey 和 AppSecret
                Map<String,String> apk = orderMapper.getAppKeySecretByAppKey(OrgId);
                pas.put("AppKey",apk.get("AppKey"));
                pas.put("AppSecret",apk.get("AppSecret"));
                JsonRootBean sajrb = basicService.getSaOrder(pas);//返回了 T+ 销货单的实体类
                //LOGGER.error("这个销货单 的 明细 内容 " + sajrb.toString());//这个销货单 的 明细 内容。
                //调用 新的 services 转换成HQ 的参数，并调用HQ接口，返回结果
                if(sajrb.getData().getBusinessType().getName().equals("普通销售")){
                    String HQresult = basicService.HQsaorder(sajrb); //红旗文档1.1
                }else{
                    String HQresult = basicService.HQsabackorder(sajrb);//销售退货  红旗文档1.2
                }*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "{ \"result\":\"success\" }";
    }


    // 已 弃 用！！！！
    //系统管理里面  的 消息订阅验证接口，官方已经 弃用了。 所以把代码注销掉。
    @RequestMapping(value="/dy2kai", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String dy2kai(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        String echostr = request.getParameter("echostr");
        try{
            /*String nonce = request.getParameter("nonce");
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");

            InputStreamReader reader=new InputStreamReader(request.getInputStream(),"utf-8");
            BufferedReader buffer=new BufferedReader(reader);
            String params=buffer.readLine();
            LOGGER.error("请求参数: "+params);
            JSONObject jsonObject = JSONObject.parseObject(params);
            String code = jsonObject.getString("Code");
            LOGGER.error("Code =============== " + code);
            LOGGER.error("当前操作，0 保存，1 审核，2 弃审，3 删除，4 取消中止，5 中止");
            String state = jsonObject.getString("SendState");
            LOGGER.error("SendState =============== " + state);
            String OrgId = jsonObject.getString("OrgId");
            LOGGER.error("OrgId =============== " + OrgId);
            //如果是销货单，并且是 审核 条件，
            if(code.contains("SA") && state.equals("1")){
                //查询对应的这个订单明细。并调用services，访问 红旗
                Map<String,String> pas = new HashMap<String,String>();
                pas.put("OrgId",OrgId);
                pas.put("code",code);//销货单的单号
                // 通过 OrgId 来获取 AppKey 和 AppSecret
                Map<String,String> apk = orderMapper.getAppKeySecretByAppKey(OrgId);
                pas.put("AppKey",apk.get("AppKey"));
                pas.put("AppSecret",apk.get("AppSecret"));
                JsonRootBean jrb = basicService.getSaOrder(pas);//返回了 T+ 销货单的实体类
                LOGGER.error("这个销货单 的 明细 内容 " + jrb.toString());//这个销货单 的 明细 内容。
                //调用 新的 services 转换成HQ 的参数，并调用HQ接口，返回结果
                String HQresult = basicService.HQsaorder(jrb);

            }*/
        }catch (Exception e){
            e.printStackTrace();
        }
        return echostr;
    }



    // 红旗文档1.3
    //给 红旗 提供的  直配单  的 回传地址
    //测试消息订阅的接口。  key = 8aue2u3q
    @RequestMapping(value="/hqordernotice", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String hqordernotice(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("------------------------------- 红旗发起了 直配单据回传请求  ------------------------------------------");
        try{
            String saorder = request.getParameter("saorder");
            String decryptData = Des.desDecrypt("8aue2u3q", saorder);
            LOGGER.info("直配单 的 回调确认：decryptData == " + decryptData);
            JSONObject job = JSONObject.parseObject(decryptData);
            // 我选择 接受 差异数量。并进行 处理（生成红字的 销货单，并审核，但是这个红单 不会 触发 红旗的接口。）
            JsonRootBean jrb = job.toJavaObject(JsonRootBean.class);
            Map<String,String> params = new HashMap<String,String>();
            params.put("code",jrb.getHndno());// 这是T+ 的 单据编号，对应 HQ 里面的  手动单号！！！！
            com.example.renyi.saentity.JsonRootBean sajrb = basicService.getSaOrder(params);
            String result = hqService.createTSaOrderByHQ(jrb,sajrb);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "{ \"code\":\"200\" }";
    }

    // 红旗文档1.4
    //给 红旗 提供的  退单  的 回传地址
    //测试消息订阅的接口。  key = 8aue2u3q
    @RequestMapping(value="/hqorderbacknotice", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String hqorderbacknotice(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("------------------------------- 红旗发起了 退单回传请求  ------------------------------------------");
        try{
            String saorder = request.getParameter("saorder");
            String decryptData = Des.desDecrypt("8aue2u3q", saorder);
            LOGGER.info("退单  decryptData == " + decryptData);
            JSONObject job = JSONObject.parseObject(decryptData);
            com.example.renyi.HQorderBack.ba.JsonRootBean jbr = job.toJavaObject(com.example.renyi.HQorderBack.ba.JsonRootBean.class);



        }catch (Exception e){
            e.printStackTrace();
        }
        return "{ \"code\":\"200\" }";
    }


    // 红旗文档1.5
    //给 红旗 提供的  门店退回订单回传  的 回传地址
    //测试消息订阅的接口。  key = 8aue2u3q
    @RequestMapping(value="/hqorderback", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String hqorderback(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("------------------------------- 红旗发起了 门店退回订单回传  ------------------------------------------");
        try{
            String saorder = request.getParameter("saorder");
            String decryptData = Des.desDecrypt("8aue2u3q", saorder);
            LOGGER.info("decryptData == " + decryptData);
            JSONObject job = JSONObject.parseObject(decryptData);
            String lnkshpno = job.getString("lnkshpno");//真实送货单号
            String hndno = job.getString("hndno");//手工单号
            // 表示 这个 单据 必须要 重新上传给 红旗 1.1 接口 ， 新传的送货单可以和原单的hndno不同

        }catch (Exception e){
            e.printStackTrace();
        }
        return "{ \"code\":\"200\" }";
    }

    // 红旗文档1.6
    //给 红旗 提供的  图片重传通知接口
    //测试消息订阅的接口。  key = 8aue2u3q
    @RequestMapping(value="/imgreupload", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String imgreupload(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("------------------------------- 红旗发起了 图片重传通知  ------------------------------------------");
        try{
            String saorder = request.getParameter("saorder");
            String decryptData = Des.desDecrypt("8aue2u3q", saorder);
            LOGGER.info("decryptData == " + decryptData);
            JSONObject job = JSONObject.parseObject(decryptData);
            String lnkshpno = job.getString("lnkshpno");//真实送货单号
            String hndno = job.getString("hndno");//手工单号
            String prvid = job.getString("prvid");//供应商编码
            String dptid = job.getString("dptid");//退货门店（最少3位，不足3位前面补0）
            String sndusr = job.getString("sndusr");//送货人
            String snddat = job.getString("snddat");//送货时间
            String sign = job.getString("sign");//当前数据完整json字符串的MD5

        }catch (Exception e){
            e.printStackTrace();
        }
        return "{ \"code\":\"200\" }";
    }
}