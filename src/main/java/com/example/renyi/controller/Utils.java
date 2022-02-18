package com.example.renyi.controller;

import com.example.renyi.entity.Ptt;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static String getCKbyName(String mendian){
        if(mendian.contains("成都新城市广场仁肄商贸华为授权体验店")){return "0101011001";}
        if(mendian.contains("成都龙泉万达仁肄商贸华为授权体验店")){return "0101010401";}
        if(mendian.contains("成都世豪广场仁肄商贸华为授权体验店")){return "0101010101";}
        if(mendian.contains("成都武侯吾悦广场仁肄商贸华为授权体验店")){return "0101010201";}
        if(mendian.contains("成都国贸时尚汇仁肄商贸华为授权体验店")){return "0101010801";}
        if(mendian.contains("成都悠方仁肄商贸华为授权体验店")){return "0101010601";}
        if(mendian.contains("科华北路62号力宝大厦南楼17楼05号")){return "01010201";}//成都渠道总库


        else return "";
    }


    public static Map<String, String> getResultMap(String mendian,String xm){
        //根据 普天的门店名称：返回 部门code，业务员code，供应商code
        Map<String, String> result = new HashMap<String, String>();
        if(mendian.contains("成都") && xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101004");//成都的普天供应商编码
            result.put("departmentCode","HW-SC-CD-CPB");//成都的华为产品部
            result.put("userCode","CD-037");//成都的业务员code  雍聪
        }
        if(mendian.contains("成都") && !xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101004");//成都的普天供应商编码
            result.put("departmentCode","HW-SC-CD-HYQD-QD");//成都华为渠道部
            result.put("userCode","CD-040");//成都的业务员code  严开伟
        }
        if(mendian.contains("绵阳")){
            result.put("merchantcode","020101001");
            result.put("departmentCode","HW-SC-MY-CPB");
            result.put("userCode","MY-022");// 李文龙
        }
        if(mendian.contains("上海")){
            result.put("merchantcode","020101500");
            result.put("departmentCode","HW-SH-CPB");
            result.put("userCode","SH-009");//姚邦峰
        }
        if(mendian.contains("昆明")){
            result.put("merchantcode","KM000912");
            result.put("departmentCode","HW-YN-KM-CPB");
            result.put("userCode","KM-004");//李海飞
        }
        if(mendian.contains("贵阳")){
            result.put("merchantcode","020101402");
            result.put("departmentCode","HW-GZ-GY-CPB");
            result.put("userCode","GY-007");// 李涛
        }
        if(mendian.contains("苏州")){
            result.put("merchantcode","020102504");
            result.put("departmentCode","HW-JS-SZ-CPB");
            result.put("userCode","SZ-009");//王来香
        }
        return result;
    }

}
