package com.example.renyi.controller;

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
        if(mendian.contains("科华北路62号力宝大厦南楼17楼05号")){
            map.put("ckcode","01010201");
            map.put("ckname","成都渠道总库");
            return map;
        }
        if(mendian.contains("成都市锦江区锦华万达广场仁肄华为授权体验店")){
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
        if(mendian.contains("上海市长宁区金虹桥商业广场华为授权体验店")){
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
        if(mendian.contains("云南省昆明市盘龙区瑞鼎城爱琴海宝略华为授权体验店") && !mendian.contains("样机")) {
            map.put("ckcode", "0201010401");
            map.put("ckname", "昆明瑞鼎城MSC库");
            return map;
        }
        if(mendian.contains("昆明市五华区宜家家居宝略华为授权体验店")) {
            map.put("ckcode", "0201012001");
            map.put("ckname", "昆明宜家库");
            return map;
        }


        // -----------------------  苏州 ----------------------------------//
        if(mendian.contains("苏州市吴江万象汇爀苒华为授权体验店")) {
            map.put("ckcode", "0501010301");
            map.put("ckname", "苏州万象汇库");
            return map;
        }
        if(mendian.contains("吴中区爀苒科技丽丰华为合作店")) {
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
        if(mendian.contains("成都") && xm.contains("华为体验店供货平台")){
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

}
