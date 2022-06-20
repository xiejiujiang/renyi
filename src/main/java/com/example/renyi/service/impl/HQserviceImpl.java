package com.example.renyi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.renyi.HQorderBack.sa.JsonRootBean;
import com.example.renyi.SAsubscribe.SACsubJsonRootBean;
import com.example.renyi.mapper.orderMapper;
import com.example.renyi.service.BasicService;
import com.example.renyi.service.HQservice;
import com.example.renyi.utils.HttpClient;
import com.example.renyi.utils.MapToJson;
import com.example.renyi.utils.Md5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HQserviceImpl implements HQservice {

    private static final Logger LOGGER = LoggerFactory.getLogger(HQserviceImpl.class);

    @Autowired
    private orderMapper orderMapper;

    @Autowired
    private BasicService basicService;

    @Override
    public String createTSaOrderByHQ(JsonRootBean jrb,com.example.renyi.saentity.JsonRootBean sajrb) {
        try{
            String json = MapToJson.getSAparamsJson(jrb,sajrb);
            if(!json.equals("0000")){
                String access_token = orderMapper.getTokenByAppKey("djrUbeB2");//appKey
                String result = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Create",json,
                        "djrUbeB2",
                        "F707B3834D9448B2A81856DE4E42357A",
                        access_token);
                LOGGER.info("-------------- 调用T+ 创建 差异销货单的接口后返回：" + result + " --------------s");
                JSONObject jon = JSONObject.parseObject(result);
                if("0".equals(jon.getString("code"))){//如果 销货单 创建 成功！ 再 调用  审核 功能
                    String data = jon.getString("data");
                    JSONObject dataJob = JSONObject.parseObject(data);
                    String auditjson = "{\n" +
                            "  \"param\": {\n" +
                            "    \"externalCode\": \" " + Md5.md5(dataJob.getString("ID") + dataJob.getString("Code")) + " \",\n" +
                            "    \"voucherID\": \" "+ dataJob.getString("ID") +" \",\n" +
                            "    \"voucherCode\": \" "+ dataJob.getString("Code") +" \"\n" +
                            "  }\n" +
                            "}";

                    String auditResult = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Audit",auditjson,
                            "djrUbeB2",
                            "F707B3834D9448B2A81856DE4E42357A",
                            access_token);
                }else{//如果 创建 T+ 销货单 失败！ 应该 怎么办呢？
                    String json2 = MapToJson.getSAparamsJson(jrb,sajrb);
                    String result2 = HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Create",json2,
                            "djrUbeB2",
                            "F707B3834D9448B2A81856DE4E42357A",
                            access_token);
                    JSONObject jon2 = JSONObject.parseObject(result2);
                    if("0".equals(jon2.getString("code"))) {//如果 销货单 创建 成功！ 再调用  审核 功能
                        String data2 = jon2.getString("data");
                        JSONObject dataJob2 = JSONObject.parseObject(data2);
                        String auditjson2 = "{\n" +
                                "  \"param\": {\n" +
                                "    \"externalCode\": \" " + Md5.md5(dataJob2.getString("ID") + dataJob2.getString("Code")) + " \",\n" +
                                "    \"voucherID\": \" "+ dataJob2.getString("ID") +" \",\n" +
                                "    \"voucherCode\": \" "+ dataJob2.getString("Code") +" \"\n" +
                                "  }\n" +
                                "}";
                        HttpClient.HttpPost("/tplus/api/v2/SaleDeliveryOpenApi/Audit",auditjson2,
                                "djrUbeB2",
                                "F707B3834D9448B2A81856DE4E42357A",
                                access_token);
                    }
                }
            }else{
                LOGGER.info("-------------- 红旗回调了1.3接口，但是没有差异数量，所以不会再生成 T+ 的销货单了！ --------------");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "0000";
    }


    // 这个方法 是 我 专门 迭代 controller 的 处理逻辑。非常重要！
    public void dealHQservice(SACsubJsonRootBean jrb){
        try {
            String OrgId = jrb.getOrgId();
            String voucherCode = jrb.getBizContent().getVoucherCode();
            LOGGER.info("=============== 销货单: "+voucherCode+" 审核 来了！=============== ");
            //根据 voucherCode 查询 此 销货单的明细内容
            Map<String,String> pas = new HashMap<String,String>();
            pas.put("OrgId",OrgId);
            pas.put("code",voucherCode);//销货单的单号
            pas.put("AppKey","djrUbeB2");
            pas.put("AppSecret","F707B3834D9448B2A81856DE4E42357A");
            com.example.renyi.saentity.JsonRootBean sajrb = basicService.getSaOrder(pas);//返回了 T+ 销货单的实体类
            if(sajrb == null || sajrb.getData() == null || sajrb.getData().getSettleCustomer() == null || sajrb.getData().getSettleCustomer().getName() == null){
                LOGGER.info("=============== 销货单: "+voucherCode+" 调用T+接口获取完整信息失败！直接弃审 ！=============== ");
                //因为 我无法判断是不是红旗的。烦啊！
                basicService.unAuditZDorder(voucherCode);//弃审。
                return;
            }

            //如果是红旗的销货单审核的内容，就需要上传，否则，不做任何处理
            LOGGER.info("--------------"+ voucherCode + "： 这一单对应的结算客户名字是: " + sajrb.getData().getSettleCustomer().getName());
            String memo = ""+ sajrb.getData().getMemo();
            LOGGER.info("--------------"+ voucherCode + "： 这一单对应的备注内容是: " + memo);
            String hq16flag = ""+orderMapper.getTSAorderFlag(voucherCode);// 这个参数 只是用来处理 重新上传图片的标志！！！
            if(sajrb.getData().getSettleCustomer().getName().contains("红旗") && !memo.contains("差异自动生成") && !hq16flag.contains("HQ1.6")) {
                // 进入这里，说明此单 一定要上传。先把 状态 更新成 0
                Map<String,String> upMap = new HashMap<String,String>();
                upMap.put("flag","0");upMap.put("code",voucherCode);
                orderMapper.updateUploadHQState(upMap);

                //获取 此单 的附件 内容
                List<Map<String,String>> fjlist = orderMapper.getfjidByCode(voucherCode);
                if(fjlist != null && fjlist.size() != 0 && ( fjlist.get(0).get("FileType").contains("jpg") || fjlist.get(0).get("FileType").contains("png") )){
                    String HQresult = "";
                    if(sajrb.getData().getBusinessType().getName().equals("普通销售")){
                        HQresult = basicService.HQsaorder(sajrb);  //红旗文档1.1
                    }else{//销售退货
                        HQresult = basicService.HQsabackorder(sajrb); //红旗文档1.2
                    }
                    // 判断上传 结果， 要么 继续上传 图片，要么 弃审！
                    if("200".equals(HQresult)){// 单据 上传给 红旗 成功了！ 马上 上传图片
                        Map<String,String> upMap1 = new HashMap<String,String>();
                        upMap1.put("flag","1");upMap1.put("code",voucherCode);
                        orderMapper.updateUploadHQState(upMap1);
                        LOGGER.info("-------------- 销货单: " + voucherCode + " 上传给 红旗 成功，马上开始上传图片！--------------");
                        basicService.uploadHQimg(voucherCode,fjlist);
                    }else{
                        LOGGER.info("=============== 销货单: " + voucherCode + " 上传给 红旗失败了，也没有上传图片！所以马上弃审这一单！===============");
                        Map<String,String> upMap1 = new HashMap<String,String>();
                        upMap1.put("flag","0");upMap1.put("code",voucherCode);
                        upMap1.put("memo","(此单上传红旗失败！，原因是："+HQresult+",如已修改，请忽略，再审核即可重新上传");
                        orderMapper.updateUploadHQState(upMap1);
                        orderMapper.updateTOrderMemo(upMap1);
                        basicService.unAuditZDorder(voucherCode);
                    }
                }else{
                    //附件不合格，没有上传给红旗，直接 弃审
                    LOGGER.info("=============== 销货单: " + voucherCode + " 附件不合格（格式和大小），无法上传图片给红旗！也没有上传单据内容！马上弃审！===============");
                    Map<String,String> upMap1 = new HashMap<String,String>();
                    upMap1.put("flag","0");upMap1.put("code",voucherCode);
                    upMap1.put("memo","(此单上传红旗失败！，原因是：附件没有或者格式不正确,如已修改，请忽略，再审核即可重新上传");
                    orderMapper.updateUploadHQState(upMap1);
                    orderMapper.updateTOrderMemo(upMap1);
                    basicService.unAuditZDorder(voucherCode);
                }
            }
            //这里处理 ，审核，但是不上传 单据内容，只上传 图片！
            if(sajrb.getData().getSettleCustomer().getName().contains("红旗") && !memo.contains("差异自动生成") && hq16flag.contains("HQ1.6")){
                //获取 此单 的附件 内容
                List<Map<String,String>> fjlist = orderMapper.getfjidByCode(voucherCode);
                if(fjlist != null && fjlist.size() != 0 && ( fjlist.get(0).get("FileType").contains("jpg") || fjlist.get(0).get("FileType").contains("png") )){
                    basicService.uploadHQimg(voucherCode,fjlist);
                }else{
                    //附件不合格，没有上传给红旗，直接 弃审
                    LOGGER.info("=============== 销货单: " + voucherCode + " 附件不合格（格式和大小），无法上传图片给红旗！也没有上传单据内容！马上弃审！===============");
                    Map<String,String> upMap1 = new HashMap<String,String>();
                    upMap1.put("flag","0");upMap1.put("code",voucherCode);
                    upMap1.put("memo","(此单上传红旗失败！，原因是：附件没有或者格式不正确,如已修改，请忽略，再审核即可重新上传");
                    orderMapper.updateUploadHQState(upMap1);
                    orderMapper.updateTOrderMemo(upMap1);
                    basicService.unAuditZDorder(voucherCode);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
