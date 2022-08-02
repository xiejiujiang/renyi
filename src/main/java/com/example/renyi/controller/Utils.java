package com.example.renyi.controller;

import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    //mendian 实际上是 excel 里面的 地址！
    public static Map<String,String> getCKbyName(String mendian){
        Map<String,String> map = new HashMap<String,String>();
        // -----------------------  成都 ---------------------------------- //
        if(mendian.contains("成都新城市广场仁肄商贸华为授权体验店")){
            map.put("ckcode","0101011001");
            map.put("ckname","成都新城市广场库");
            return map;
        }
        if(mendian.contains("成都龙泉万达仁肄商贸华为授权体验店")){
            map.put("ckcode","0101010401");
            map.put("ckname","成都龙泉万达库");
            return map;
        }
        if(mendian.contains("成都世豪广场仁肄商贸华为授权体验店")){
            map.put("ckcode","0101010101");
            map.put("ckname","成都世豪库");
            return map;
        }
        if(mendian.contains("成都武侯吾悦广场仁肄商贸华为授权体验店")){
            map.put("ckcode","0101010201");
            map.put("ckname","成都吾悦新城库");
            return map;
        }
        if(mendian.contains("成都国贸时尚汇仁肄商贸华为授权体验店")){
            map.put("ckcode","0101010801");
            map.put("ckname","成都国贸库");
            return map;
        }
        if(mendian.contains("成都悠方购物中心仁肄商贸华为授权体验店")){
            map.put("ckcode","0101010601");
            map.put("ckname","成都悠方库");
            return map;
        }
        if(mendian.contains("科华北路62号力宝大厦南楼17楼05")){
            map.put("ckcode","01010201");
            map.put("ckname","成都渠道总库");
            return map;
        }
        if(mendian.contains("成都市锦江区锦华万达广场仁肄华为授权体验店")
                || mendian.contains("锦华万达店")
                || mendian.contains("锦江区锦华路68号")
                || mendian.contains("SCN225001")){
            map.put("ckcode","0101011501");
            map.put("ckname","成都锦华MSC库");
            return map;
        }

        // -----------------------  绵阳 ----------------------------------//
        if(mendian.contains("绵阳市涪城区临园路东段78号福星楼A座1905")){
            map.put("ckcode","01020201");
            map.put("ckname","绵阳渠道总库");
            return map;
        }
        if(mendian.contains("绵阳涪城万达聚仁科技华为授权体验店")){
            map.put("ckcode","010301");
            map.put("ckname","绵阳PLUS库");
            return map;
        }

        // -----------------------  贵阳 ----------------------------------//
        if(mendian.contains("贵阳市仁讯星力城华为授权体验店")){
            map.put("ckcode","0301010101");
            map.put("ckname","贵阳汇金库");
            return map;
        }
        if(mendian.contains("贵州省贵阳市荔星名店华为融合店")){
            map.put("ckcode","0301010201");
            map.put("ckname","贵阳荔星库");
            return map;
        }
        if(mendian.contains("中华南路新大陆电脑城1楼华为融合店")){
            map.put("ckcode","0301010301");
            map.put("ckname","贵阳新大陆库");
            return map;
        }

        // -----------------------  上海 ----------------------------------//
        if(mendian.contains("上海市闵行区新龙路1333弄万科七宝国际30幢221室")){
            map.put("ckcode","0401011201");
            map.put("ckname","上海总库");
            return map;
        }
        if(mendian.contains("上海市仁肄徐汇绿地缤纷城华为授权体验店")){
            map.put("ckcode","0401010601");
            map.put("ckname","上海绿地缤纷城库");
            return map;
        }
        if(mendian.contains("金虹桥商业广场")){
            map.put("ckcode","0401010401");
            map.put("ckname","上海金虹桥库");
            return map;
        }

        // -----------------------  昆明 ----------------------------------//
        if(mendian.contains("昆明宝略大悦城华为授权体验店")) {
            map.put("ckcode", "0201010501");
            map.put("ckname", "昆明大悦城库");
            return map;
        }
        if(mendian.contains("昆明宝略前兴路万达广场华为授权体验店")) {
            map.put("ckcode", "0201010101");
            map.put("ckname", "昆明西山万达库");
            return map;
        }
        if(mendian.contains("昆明宝略爱琴海华为授权体验店")) {
            map.put("ckcode", "0201010201");
            map.put("ckname", "昆明爱琴海库");
            return map;
        }
        if(mendian.contains("昆明宝略东风东路华为授权体验店")) {
            map.put("ckcode", "0201010301");
            map.put("ckname", "昆明恒隆库");
            return map;
        }
        if(mendian.contains("云南省昆明市五华区圆通北路127号云大晟苑2号门5楼508室")) {
            map.put("ckcode", "020101011601");
            map.put("ckname", "昆明佰腾总库");
            return map;
        }
        if(mendian.contains("云南省昆明市盘龙区瑞鼎城爱琴海宝略华为授权体验店") && mendian.contains("样机")) {
            map.put("ckcode", "0201010402");
            map.put("ckname", "昆明瑞鼎城MSC样机库");
            return map;
        }
        if(mendian.contains("SCN223712") || (mendian.contains("云南省昆明市盘龙区瑞鼎城爱琴海宝略华为授权体验店") && !mendian.contains("样机"))) {
            map.put("ckcode", "0201010401");
            map.put("ckname", "昆明瑞鼎城MSC库");
            return map;
        }
        if(mendian.contains("昆明市五华区宜家家居宝略华为授权体验店")) {
            map.put("ckcode", "0201012001");
            map.put("ckname", "昆明宜家库");
            return map;
        }
        if(mendian.contains("同德广场")) {
            map.put("ckcode", "0201013001");
            map.put("ckname", "昆明同德库");
            return map;
        }
        if(mendian.contains("呈贡万达广场")) {
            map.put("ckcode", "0201010601");
            map.put("ckname", "昆明呈贡库");
            return map;
        }


        // -----------------------  苏州 ----------------------------------//
        if(mendian.contains("苏州市吴江万象汇爀苒华为授权体验店")) {
            map.put("ckcode", "0501010301");
            map.put("ckname", "苏州万象汇库");
            return map;
        }
        if(mendian.contains("吴中区爀苒科技丽丰华为合作店") || mendian.contains("宝带东路399号丽丰购物中心")) {
            map.put("ckcode", "0501010201");
            map.put("ckname", "苏州丽丰库");
            return map;
        }
        if(mendian.contains("苏州市虎丘区永旺梦乐城爀苒华为授权体验店")) {
            map.put("ckcode", "0501010401");
            map.put("ckname", "苏州新区永旺库");
            return map;
        }
        if(mendian.contains("苏州市姑苏区印象城爀苒华为授权体验店")) {
            map.put("ckcode", "0501010801");
            map.put("ckname", "苏州印象城库");
            return map;
        }
        if(mendian.contains("苏州市吴江区凤凰荟购物中心爀苒华为授权体验店")) {
            map.put("ckcode", "0501010101");
            map.put("ckname", "苏州凤凰荟库");
            return map;
        }
        return map;
    }


    public static Map<String, String> getResultMap(String mendian,String xm){
        //根据 普天的门店名称：返回 部门code，业务员code，供应商code
        Map<String, String> result = new HashMap<String, String>();
        if(mendian.contains("成都") && xm.contains("华为体验店供货平台") && !mendian.contains("成都市锦江区锦华万达")){
            result.put("merchantcode","020101004");//成都-普天太力
            result.put("merchantname","成都-普天太力");
            result.put("departmentCode","HW-SC-CD-CPB");//成都华为产品部
            result.put("departmentName","成都华为产品部");
            result.put("userCode","CD-037");//雍聪
            result.put("userName","雍聪");
            return result;
        }
        if(mendian.contains("成都市锦江区锦华万达") && (xm.contains("MSC供货") || mendian.contains("样机"))){
            result.put("merchantcode","020102064");
            result.put("merchantname","成都-MSC(普天太力)");
            result.put("departmentCode","HW-MSCSYB-DM-CDMSCJH");
            result.put("departmentName","成都锦华万达MSC店");
            result.put("userCode","CD-062");
            result.put("userName","刘豪");
            return result;
        }
        if(mendian.contains("成都") && !xm.contains("华为体验店供货平台") && !xm.contains("MSC供货")){
            result.put("merchantcode","020101008");//成都-普天太力FD
            result.put("merchantname","成都-普天太力FD");
            result.put("departmentCode","HW-SC-CD-HYQD-QD");//成都华为渠道部
            result.put("departmentName","成都华为渠道部");
            result.put("userCode","CD-040");//严开伟
            result.put("userName","严开伟");
            return result;
        }
        if(mendian.contains("绵阳") && xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101001");//绵阳-普天太力
            result.put("merchantname","绵阳-普天太力");
            result.put("departmentCode","HW-SC-MY-CPB");//绵阳华为产品部
            result.put("departmentName","绵阳华为产品部");
            result.put("userCode","MY-022");// 李文龙
            result.put("userName","李文龙");
            return result;
        }
        if(mendian.contains("绵阳") && !xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101002");//绵阳-普天太力FD
            result.put("merchantname","绵阳-普天太力FD");
            result.put("departmentCode","HW-SC-MY-CPB");//绵阳华为产品部
            result.put("departmentName","绵阳华为产品部");
            result.put("userCode","MY-022");// 李文龙
            result.put("userName","李文龙");
            return result;
        }
        if(mendian.contains("上海") && xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101500");
            result.put("merchantname","上海-普天太力");
            result.put("departmentCode","HW-SH-CPB");
            result.put("departmentName","上海华为产品部");
            result.put("userCode","SH-009");//姚邦峰
            result.put("userName","姚邦峰");
            return result;
        }
        if(mendian.contains("上海") && !xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101501");
            result.put("merchantname","上海-普天太力FD");
            result.put("departmentCode","HW-SH-CPB");
            result.put("departmentName","上海华为产品部");
            result.put("userCode","SH-009");//姚邦峰
            result.put("userName","姚邦峰");
            return result;
        }
        if(mendian.contains("昆明") && xm.contains("华为体验店供货平台")){
            result.put("merchantcode","KM000912");
            result.put("merchantname","昆明-普天太力");
            result.put("departmentCode","HW-YN-KM-CPB");
            result.put("departmentName","昆明华为产品部");
            result.put("userCode","KM-004");//李海飞
            result.put("userName","李海飞");//李海飞
            return result;
        }
        if(mendian.contains("昆明") && xm.contains("MSC")){
            result.put("merchantcode","KM000913");
            result.put("merchantname","昆明-MSC(普天太力)");
            result.put("departmentCode","HW-YN-KM-CPB");
            result.put("departmentName","昆明华为产品部");
            result.put("userCode","KM-004");//李海飞
            result.put("userName","李海飞");
            return result;
        }
        if(mendian.contains("贵阳") && xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101402");//贵阳普天
            result.put("merchantname","贵阳-普天太力");
            result.put("departmentCode","HW-GZ-GY-CPB");//贵阳华为产品部
            result.put("departmentName","贵阳华为产品部");
            result.put("userCode","GY-007");// 李涛
            result.put("userName","李涛");
            return result;
        }
        if(mendian.contains("贵阳") && !xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101404");//贵阳普天FD
            result.put("merchantname","贵阳-普天太力（FD）");
            result.put("departmentCode","HW-GZ-GY-CPB");//贵阳华为产品部
            result.put("departmentName","贵阳华为产品部");
            result.put("userCode","GY-007");// 李涛
            result.put("userName","李涛");
            return result;
        }
        if(mendian.contains("苏州") && !mendian.contains("苏州市姑苏区印象城爀苒华为授权体验店")){
            result.put("merchantcode","020102504");
            result.put("merchantname","苏州-普天太力");
            result.put("departmentCode","HW-JS-SZ-CPB");
            result.put("departmentName","苏州华为产品部");
            result.put("userCode","SZ-009");//王来香
            result.put("userName","王来香");
            return result;
        }
        if(mendian.contains("苏州") && mendian.contains("苏州市姑苏区印象城爀苒华为授权体验店")){
            result.put("merchantcode","020101600");//苏州印象城-普天太力
            result.put("merchantname","苏州印象城-普天太力");
            result.put("departmentCode","HW-JS-SZ-LS-YXC");//苏州华为印象城店
            result.put("departmentName","苏州华为印象城店");
            result.put("userCode","SZ-065");//印象城王来香
            result.put("userName","印象城王来香");
            return result;
        }
        return result;
    }


    public static Map<String, String> getZYOUResultMap(String shdz){
        Map<String, String> result = new HashMap<String,String>();
        if(shdz.contains("成都") && shdz.contains("力宝大厦")){
            result.put("merchantcode","020101012");
            result.put("merchantname","成都-华为(中国邮电)");
            result.put("departmentCode","HW-SC-CD-HYQD-QD");//成都华为渠道部
            result.put("departmentName","成都华为渠道部");
            result.put("userCode","CD-040");//严开伟
            result.put("userName","严开伟");
            return result;
        }
        if(shdz.contains("成都") && shdz.contains("锦华万达")){
            result.put("merchantcode","020101012");
            result.put("merchantname","成都-华为(中国邮电)");
            result.put("departmentCode","HW-SC-CD-CPB");//成都华为产品部
            result.put("departmentName","成都华为产品部");
            result.put("userCode","CD-037");//雍聪
            result.put("userName","雍聪");
            return result;
        }
        if(shdz.contains("贵阳")){
            result.put("merchantcode","020101403");
            result.put("merchantname","贵阳-中国邮电");
            result.put("departmentCode","HW-GZ-GY-CPB");//贵阳华为产品部
            result.put("departmentName","贵阳华为产品部");
            result.put("userCode","GY-007");// 李涛
            result.put("userName","李涛");
            return result;
        }
        if(shdz.contains("绵阳")){
            result.put("merchantcode","020102052");
            result.put("merchantname","绵阳-中国邮电器材西南有限公司");
            result.put("departmentCode","HW-SC-MY-CPB");//绵阳华为产品部
            result.put("departmentName","绵阳华为产品部");
            result.put("userCode","MY-022");// 李文龙
            result.put("userName","李文龙");
            return result;
        }
        if(shdz.contains("苏州")){
            result.put("merchantcode","020102505");
            result.put("merchantname","苏州-中国邮电器材");
            result.put("departmentCode","HW-JS-SZ-CPB");
            result.put("departmentName","苏州华为产品部");
            result.put("userCode","SZ-009");//王来香
            result.put("userName","王来香");
            return result;
        }
        if(shdz.contains("上海")){
            result.put("merchantcode","020101504");
            result.put("merchantname","上海-中国邮电器材华东公司");
            result.put("departmentCode","HW-SH-CPB");
            result.put("departmentName","上海华为产品部");
            result.put("userCode","SH-009");//姚邦峰
            result.put("userName","姚邦峰");
            return result;
        }
        if(shdz.contains("昆明")){
            result.put("merchantcode","KM000929");
            result.put("merchantname","昆明-云南中邮普泰移动通信设备");
            result.put("departmentCode","HW-YN-KM-CPB");
            result.put("departmentName","昆明华为产品部");
            result.put("userCode","KM-004");
            result.put("userName","李海飞");
            return result;
        }
        return result;
    }

    /*问叶庆，如何下单的*/
    public static Map<String, String> getMSCResultMap(String shdz){
        Map<String, String> result = new HashMap<String,String>();
        if(shdz.contains("锦江区锦华路68号1幢") || shdz.contains("SCN225001")){ // 成都的MSC
            result.put("merchantcode","020102070");
            result.put("merchantname","成都-MSC(PMALL商城）");
            result.put("departmentCode","HW-MSCSYB-DM-CDMSCJH");
            result.put("departmentName","成都锦华万达MSC店");
            result.put("userCode","CD-037");
            result.put("userName","雍聪");
            return result;
        }

        if(shdz.contains("SCN223712")){ // 昆明 的 瑞鼎城
            result.put("merchantcode","KM000940");
            result.put("merchantname","昆明-MSC(PMALL商城)");
            result.put("departmentCode","HW-YN-KM-CPB");
            result.put("departmentName","昆明华为产品部");
            result.put("userCode","KM-004");
            result.put("userName","李海飞");
            return result;
        }
        return result;
    }

    public static Map<String,String> getAppleCKbyName(String mddm){
        Map<String, String> result = new HashMap<String,String>();
        switch (mddm){
            case "000":
                result.put("ckcode", "010102010");
                result.put("ckname", "零售总库上海区");
                break;
            case "200":
                result.put("ckcode", "020100021");
                result.put("ckname", "上海闵行剑川库");
                break;
            case "201":
                result.put("ckcode", "020100011");
                result.put("ckname", "上海徐汇华泾库");
                break;
            case "202":
                result.put("ckcode", "020200011");
                result.put("ckname", "苏州木渎库");
                break;
            case "203":
                result.put("ckcode", "020200021");
                result.put("ckname", "苏州星湖天街库");
                break;
            case "204":
                result.put("ckcode", "020100031");
                result.put("ckname", "上海临港万达库");
                break;
            case "205":
                result.put("ckcode", "020100041");
                result.put("ckname", "上海正大乐城库");
                break;
            case "206":
                result.put("ckcode", "020300011");
                result.put("ckname", "合肥悦方库");
                break;
            case "207":
                result.put("ckcode", "020400011");
                result.put("ckname", "南京龙湾天街库");
                break;
            case "208":
                result.put("ckcode", "020400021");
                result.put("ckname", "南京六合天街库");
                break;
            case "209":
                result.put("ckcode", "020300021");
                result.put("ckname", "合肥万科广场库");
                break;
            case "300":
                result.put("ckcode", "040100011");
                result.put("ckname", "昆明富康城库");
                break;
            case "301":
                result.put("ckcode", "040200011");
                result.put("ckname", "贵阳印象城库");
                break;
            case "302":
                result.put("ckcode", "040300011");
                result.put("ckname", "成都空港华联库");
                break;
            case "303":
                result.put("ckcode", "040100021");
                result.put("ckname", "昆明宜家库");
                break;
            case "304":
                result.put("ckcode", "040400011");
                result.put("ckname", "长沙喜盈门库");
                break;
        }
        return result;
    }

    public static void main(String[] args) {
        String shdz = "四川-成都-武侯区-科华北路62号力宝大厦南楼17楼05";
        Map<String,String> map = getZYOUResultMap(shdz);
        System.out.println("map.getmercode === " + map.get("merchantcode"));
        System.out.println("map.merchantname === " + map.get("merchantname"));
        System.out.println("map.departmentCode === " + map.get("departmentCode"));
        System.out.println("map.departmentName === " + map.get("departmentName"));
        System.out.println("map.userCode === " + map.get("userCode"));
        System.out.println("map.userName === " + map.get("userName"));
    }
}