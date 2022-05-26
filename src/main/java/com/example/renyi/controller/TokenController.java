package com.example.renyi.controller;

import com.alibaba.excel.util.FileUtils;
import com.alibaba.fastjson.JSONObject;
import com.example.renyi.HQorderBack.sa.JsonRootBean;
import com.example.renyi.SAsubscribe.SACsubJsonRootBean;
import com.example.renyi.mapper.orderMapper;
import com.example.renyi.service.BasicService;
import com.example.renyi.service.HQservice;
import com.example.renyi.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
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
        LOGGER.info("------------------- 正式OAuth回调地址 -------------------");
        String code = request.getParameter("code");
        //第一次授权后，会有这个code,立刻调用 一次 授权码换token接口 ，拿到完整的 token 相关信息，并写入数据库。
        //3月17日思考： 暂时不用接口来访问，直接在线访问后 拿到第一次的数据，并 复制 填入数据库表中接口（后续定时任务来更新）
        return code;
    }


    //T+ 的 消息订阅的接口。
    @RequestMapping(value="/ticket", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String reticket(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("------------------- 正式消息接收地址，包含 ticket，消息订阅，授权 -------------------");
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
                SACsubJsonRootBean jrb =  job.toJavaObject(SACsubJsonRootBean.class);//销货单的订阅信息DTO
                String voucherCode = jrb.getBizContent().getVoucherCode();
                LOGGER.info("-------------- 销货单 审核 来了！============================ " + voucherCode);
                String OrgId = jrb.getOrgId();
                //根据 voucherCode 查询 此 销货单的明细内容
                Map<String,String> pas = new HashMap<String,String>();
                pas.put("OrgId",OrgId);
                pas.put("code",voucherCode);//销货单的单号
                // 通过 OrgId 来获取 AppKey 和 AppSecret
                Map<String,String> apk = orderMapper.getAppKeySecretByAppKey(OrgId);
                pas.put("AppKey",apk.get("AppKey"));
                pas.put("AppSecret",apk.get("AppSecret"));
                com.example.renyi.saentity.JsonRootBean sajrb = basicService.getSaOrder(pas);//返回了 T+ 销货单的实体类
                //如果是红旗的销货单审核的内容，就需要上传，否则，不做任何处理
                LOGGER.info("--------------"+ voucherCode + "： 这一单对应的结算客户名字是:" + sajrb.getData().getSettleCustomer().getName());
                String memo = ""+sajrb.getData().getMemo();
                LOGGER.info("--------------"+voucherCode + "： 这一单对应的备注内容是 " + memo);

                if(sajrb.getData().getSettleCustomer().getName().contains("红旗") && !memo.contains("差异自动生成")){
                    //调用 新的 services 转换成HQ 的参数，并调用HQ接口，返回结果
                    if(sajrb.getData().getBusinessType().getName().equals("普通销售")){
                        List<Map<String,String>> fjlist = orderMapper.getfjidByCode(voucherCode);
                        //有附件 并且 格式必须是 jpg 或 png(小于200K)  才 允许  上传给 红旗
                        if(fjlist.size()>0 && (fjlist.get(0).get("FileType").contains("jpg") || fjlist.get(0).get("FileType").contains("png") )){
                            String HQresult = basicService.HQsaorder(sajrb);  //红旗文档1.1
                            if(HQresult.contains("200")) {//上传成功，立刻上传图片！！
                                LOGGER.info("-------------- 销货单: " + voucherCode + " 上传给 红旗 成功，马上开始上传图片！！");
                                int imgsize = 10>fjlist.size()?fjlist.size():10;//最多只能10张
                                for(int imgint = 0;imgint < imgsize;imgint ++){
                                    Map<String,String> fjmap = fjlist.get(imgint);
                                    String imgurl = fjmap.get("UploadPath");
                                    if(imgurl.contains("http")){//如果是手机端上传的图片，是http的
                                        //String img64 = ImageUtils.encodeImgageToBase64(new URL(imgurl));

                                        File file = ImgUU.urlToFile(new URL(imgurl));
                                        byte[] bytes = FileUtils.readFileToByteArray(file);
                                        bytes = ImgUU.compressPicForScale(bytes, 200, "x");// 压缩后的图片小于200kb
                                        BASE64Encoder encoder = new BASE64Encoder();
                                        // 返回Base64编码过的字节数组字符串
                                        String img64 = encoder.encode(bytes);
                                        String base64 = URLEncoder.encode(img64,"UTF-8");
                                        String  imgreuslt = basicService.HQimage(voucherCode,base64,imgint);  //红旗文档1.7
                                        //判断图片上传的结果！！！（注意，此时，单据已经成功上传了！！）
                                        //LOGGER.info(" 销货单: " + voucherCode + " 的图片上传结果是：" + imgreuslt);
                                    }else{//如果是电脑上传的图片，没有http。是一个 windows文件路径。
                                        //String img64 = ImageUtils.encodeImgageToBase64(new File(imgurl));

                                        byte[] bytes = FileUtils.readFileToByteArray(new File(imgurl));
                                        bytes = ImgUU.compressPicForScale(bytes, 200, "x");// 压缩后的图片小于200kb
                                        BASE64Encoder encoder = new BASE64Encoder();
                                        // 返回Base64编码过的字节数组字符串
                                        String img64 = encoder.encode(bytes);
                                        String base64 = URLEncoder.encode(img64,"UTF-8");
                                        String  imgreuslt = basicService.HQimage(voucherCode,base64,imgint);  //红旗文档1.7
                                        //判断图片上传的结果！！！（注意，此时，单据已经成功上传了！！）
                                        //LOGGER.info(" 销货单: " + voucherCode + " 的图片上传结果是：" + imgreuslt);
                                    }
                                }
                            }else{
                                LOGGER.info(" 销货单: " + voucherCode + " 上传给 红旗失败了，也没有上传图片！所以马上弃审这一单！");
                                //弃审这一单
                                String auditjson = "{\n" +
                                        "  \"param\": {\n" +
                                        "    \"voucherCode\": \""+voucherCode+"\"\n" +
                                        "  }\n" +
                                        "}";
                                String access_token = orderMapper.getTokenByAppKey("djrUbeB2");//appKey
                                String auditResult = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/UnAudit",auditjson,
                                        "djrUbeB2",
                                        "F707B3834D9448B2A81856DE4E42357A",
                                        access_token);
                                JSONObject unauditjob = JSONObject.parseObject(auditResult);
                                if(unauditjob.getString("code").contains("999")){//如果弃审失败，就再弃审一次 试试
                                    auditResult = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/UnAudit",auditjson,
                                            "djrUbeB2",
                                            "F707B3834D9448B2A81856DE4E42357A",
                                            access_token);
                                }
                                LOGGER.info("单号： " + voucherCode + " 的弃审结果是：" + auditResult);
                            }
                        }else{
                            LOGGER.info(" 销货单: " + voucherCode + " 附件不合格（格式和大小），无法上传图片给红旗！也没有上传单据内容！马上弃审！");
                            String auditjson = "{\n" +
                                    "  \"param\": {\n" +
                                    "    \"voucherCode\": \""+voucherCode+"\"\n" +
                                    "  }\n" +
                                    "}";
                            String access_token = orderMapper.getTokenByAppKey("djrUbeB2");//appKey
                            String auditResult = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/UnAudit",auditjson,
                                    "djrUbeB2",
                                    "F707B3834D9448B2A81856DE4E42357A",
                                    access_token);
                            JSONObject unauditjob = JSONObject.parseObject(auditResult);
                            if(unauditjob.getString("code").contains("999")){//如果弃审失败，就再弃审一次 试试
                                auditResult = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/UnAudit",auditjson,
                                        "djrUbeB2",
                                        "F707B3834D9448B2A81856DE4E42357A",
                                        access_token);
                            }
                            LOGGER.info("单号： " + voucherCode + "的弃审结果是：" + auditResult);
                        }
                    }else{//销售退货  红旗文档1.2
                        String HQresult = basicService.HQsabackorder(sajrb);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "{ \"result\":\"success\" }";
    }


    // 已 弃 用！！！！  被上面挨着这个 替代了 。
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
            //LOGGER.error("请求参数: "+params);
            JSONObject jsonObject = JSONObject.parseObject(params);
            String code = jsonObject.getString("Code");
            LOGGER.error("管理员的二开消息订阅Code =============== " + code);
            LOGGER.error("当前操作，0 保存，1 审核，2 弃审，3 删除，4 取消中止，5 中止");
            String state = jsonObject.getString("SendState");
            LOGGER.error("SendState =============== " + state);
            String OrgId = jsonObject.getString("OrgId");
            //LOGGER.error("OrgId =============== " + OrgId);
            //如果是销货单，并且是 审核 条件，
            if(state.equals("1")){
                //查询对应的这个订单明细。并调用services，访问 红旗
                Map<String,String> pas = new HashMap<String,String>();
                pas.put("OrgId",OrgId);
                pas.put("code",code);//销货单的单号
                // 通过 OrgId 来获取 AppKey 和 AppSecret
                Map<String,String> apk = orderMapper.getAppKeySecretByAppKey(OrgId);
                pas.put("AppKey",apk.get("AppKey"));
                pas.put("AppSecret",apk.get("AppSecret"));

                LOGGER.info("新的开始：== " + OrgId+","+code+","+apk.get("AppKey")+","+apk.get("AppSecret"));

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
        LOGGER.info("------------------------------- 红旗发起了 直配单据回传请求  ------------------------------------------");
        String restr = "";
        try{
            String saorder = request.getParameter("json");
            String decryptData = Des.desDecrypt(HQDemo.deskey, saorder);
            LOGGER.info("------------------ 1.3接口 接受到红旗的信息 :  decryptData == " + decryptData);
            JSONObject job = JSONObject.parseObject(decryptData);
            // 我选择 接受 差异数量。并进行 处理（生成红字的 销货单，并审核，但是这个红单 不会 触发 红旗的接口。）
            JsonRootBean jrb = job.toJavaObject(JsonRootBean.class);

            String hndno = jrb.getHndno();//手工单号
            String vourchcode = hndno.substring(5,hndno.length());

            Map<String,String> params = new HashMap<String,String>();
            params.put("code",vourchcode);// 这是T+ 的 单据编号，对应 HQ 里面的  手动单号！！！！
            params.put("AppKey","djrUbeB2");// 直接 写 死 ！
            params.put("AppSecret","F707B3834D9448B2A81856DE4E42357A");// 直接 写 死 ！
            com.example.renyi.saentity.JsonRootBean sajrb = basicService.getSaOrder(params);
            //需要处理红旗返回的 差异 数量 问题。
            String result = hqService.createTSaOrderByHQ(jrb,sajrb);
            restr = "{ \"code\":\"200\" }";
        }catch (Exception e){
            e.printStackTrace();
            restr = "{ \"code\":\"999\",\"msg\":\"异常报错，需要检擦参数内容\"}";
        }
        return restr;
    }

    // 红旗文档1.4
    //给 红旗 提供的  退单  的 回传地址
    //测试消息订阅的接口。  key = 8aue2u3q
    @RequestMapping(value="/hqorderbacknotice", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String hqorderbacknotice(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("------------------------------- 红旗发起了 退单回传请求  ------------------------------------------");
        try{
            String saorder = request.getParameter("json");
            String decryptData = Des.desDecrypt(HQDemo.deskey, saorder);
            LOGGER.info("------------------ 1.4接口 接受到红旗的信息 : decryptData == " + decryptData);
            JSONObject job = JSONObject.parseObject(decryptData);
            com.example.renyi.HQorderBack.ba.JsonRootBean jbr = job.toJavaObject(com.example.renyi.HQorderBack.ba.JsonRootBean.class);
            // 此处，啥也没做哈！
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
        LOGGER.info("------------------------------- 红旗发起了 门店退回订单回传  ------------------------------------------");
        try{
            String saorder = request.getParameter("json");
            String decryptData = Des.desDecrypt(HQDemo.deskey, saorder);

            LOGGER.info("----------------- 1.5接口 接受到红旗的信息 : decryptData == " + decryptData);
            JSONObject job = JSONObject.parseObject(decryptData);
            String lnkshpno = job.getString("lnkshpno");//真实送货单号
            String hndno = job.getString("hndno");//手工单号
            // 表示 这个 单据 必须要 重新上传给 红旗 1.1 接口 ， 新传的送货单可以和原单的hndno不同
            String vourchcode = hndno.substring(5,hndno.length());
            String auditjson = "{\n" +
                    "  \"param\": {\n" +
                    "    \"voucherCode\": \""+vourchcode+"\"\n" +
                    "  }\n" +
                    "}";
            String access_token = orderMapper.getTokenByAppKey("djrUbeB2");//appKey
            String auditResult = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/UnAudit",auditjson,
                    "djrUbeB2",
                    "F707B3834D9448B2A81856DE4E42357A",
                    access_token);
            LOGGER.info("----------------- 因为1.5接口，单号： " + hndno + "的弃审结果是：" + auditResult);
            //弃审成功之后，T+ 设置了 消息提醒，需要 业务员 重新 提交 审核（可能是 图片问题，或者 其他问题。）
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
        LOGGER.info("------------------------------- 红旗发起了 图片重传通知  ------------------------------------------");
        try{
            String saorder = request.getParameter("json");
            String decryptData = Des.desDecrypt(HQDemo.deskey, saorder);
            LOGGER.info("----------------- 1.6 图片重传接口 接受到红旗的信息 : decryptData == " + decryptData);
            JSONObject job = JSONObject.parseObject(decryptData);
            String lnkshpno = job.getString("lnkshpno");//真实送货单号
            String hndno = job.getString("hndno");//手工单号
            String prvid = job.getString("prvid");//供应商编码
            String dptid = job.getString("dptid");//退货门店（最少3位，不足3位前面补0）
            String sndusr = job.getString("sndusr");//送货人
            String snddat = job.getString("snddat");//送货时间
            String sign = job.getString("sign");//当前数据完整json字符串的MD5

            String voucherCode = hndno.substring(5,hndno.length());
            //直接再上传图片！！！  管他妈的！
            List<Map<String,String>> fjlist = orderMapper.getfjidByCode(voucherCode);
            //有附件 并且 格式必须是 jpg 或 png(小于200K)  才 允许  上传给 红旗
            if(fjlist.size()>0 && (fjlist.get(0).get("FileType").contains("jpg") || fjlist.get(0).get("FileType").contains("png") )){
                int imgsize = 10 > fjlist.size() ? fjlist.size():10;//最多只能10张
                for(int imgint = 0;imgint < imgsize;imgint ++){
                    Map<String,String> fjmap = fjlist.get(imgint);
                    String imgurl = fjmap.get("UploadPath");
                    if(imgurl.contains("http")){//如果是手机端上传的图片，是http的
                        //String img64 = ImageUtils.encodeImgageToBase64(new URL(imgurl));

                        File file = ImgUU.urlToFile(new URL(imgurl));
                        byte[] bytes = FileUtils.readFileToByteArray(file);
                        bytes = ImgUU.compressPicForScale(bytes, 200, "x");// 压缩后的图片小于200kb
                        BASE64Encoder encoder = new BASE64Encoder();
                        // 返回Base64编码过的字节数组字符串
                        String img64 = encoder.encode(bytes);
                        String base64 = URLEncoder.encode(img64,"UTF-8");
                        String  imgreuslt = basicService.HQimage(voucherCode,base64,imgint);  //红旗文档1.7
                        //判断图片上传的结果！！！（注意，此时，单据已经成功上传了！！）
                        //LOGGER.info(" 销货单: " + voucherCode + " 的图片上传结果是：" + imgreuslt);
                    }else{//如果是电脑上传的图片，没有http。是一个 windows文件路径。
                        //String img64 = ImageUtils.encodeImgageToBase64(new File(imgurl));

                        byte[] bytes = FileUtils.readFileToByteArray(new File(imgurl));
                        bytes = ImgUU.compressPicForScale(bytes, 200, "x");// 压缩后的图片小于200kb
                        BASE64Encoder encoder = new BASE64Encoder();
                        // 返回Base64编码过的字节数组字符串
                        String img64 = encoder.encode(bytes);
                        String base64 = URLEncoder.encode(img64,"UTF-8");
                        String  imgreuslt = basicService.HQimage(voucherCode,base64,imgint);  //红旗文档1.7
                        //判断图片上传的结果！！！（注意，此时，单据已经成功上传了！！）
                        //LOGGER.info(" 销货单: " + voucherCode + " 的图片上传结果是：" + imgreuslt);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "{ \"code\":\"200\" }";
    }
}