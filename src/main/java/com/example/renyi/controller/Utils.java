package com.example.renyi.controller;

import com.example.renyi.entity.Ptt;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    //mendian 实际上是 excel 里面的 地址！
    public static String getCKbyName(String mendian){
        // -----------------------  成都 ---------------------------------- //
        if(mendian.contains("成都新城市广场仁肄商贸华为授权体验店")){return "0101011001";}
        if(mendian.contains("成都龙泉万达仁肄商贸华为授权体验店")){return "0101010401";}
        if(mendian.contains("成都世豪广场仁肄商贸华为授权体验店")){return "0101010101";}
        if(mendian.contains("成都武侯吾悦广场仁肄商贸华为授权体验店")){return "0101010201";}
        if(mendian.contains("成都国贸时尚汇仁肄商贸华为授权体验店")){return "0101010801";}
        if(mendian.contains("成都悠方仁肄商贸华为授权体验店")){return "0101010601";}
        if(mendian.contains("科华北路62号力宝大厦南楼17楼05号")){return "01010201";}//成都渠道总库

        // -----------------------  绵阳 ----------------------------------//
        if(mendian.contains("绵阳市涪城区临园路东段78号福星楼A座1905")){return "01020201";}//绵阳渠道总库
        if(mendian.contains("绵阳涪城万达聚仁科技华为授权体验店")){return "010301";}//绵阳PLUS库

        // -----------------------  贵阳 ----------------------------------//
        if(mendian.contains("贵阳市仁讯星力城华为授权体验店")){return "0301010101";}//汇金
        if(mendian.contains("贵州省贵阳市荔星名店华为融合店")){return "0301010201";}//荔星(FD)
        if(mendian.contains("中华南路新大陆电脑城1楼华为融合店")){return "0301010301";}//新大陆(FD)

        // -----------------------  上海 ----------------------------------//
        if(mendian.contains("上海市闵行区新龙路1333弄万科七宝国际30幢221室")){return "0401011201";}//上海总库
        if(mendian.contains("上海市仁肄徐汇绿地缤纷城华为授权体验店")){return "0401010601";}//绿地缤纷城
        if(mendian.contains("上海市长宁区金虹桥商业广场华为授权体验店")){return "0401010401";}//金虹桥

        // -----------------------  昆明 ----------------------------------//
        if(mendian.contains("昆明宝略大悦城华为授权体验店")){return "0201010501";}//大悦城
        if(mendian.contains("昆明宝略前兴路万达广场华为授权体验店")){return "0201010101";}//昆明西山万达
        if(mendian.contains("昆明宝略爱琴海华为授权体验店")){return "0201010201";}//爱琴海
        if(mendian.contains("昆明宝略东风东路华为授权体验店")){return "0201010301";}//昆明恒隆库
        if(mendian.contains("云南省昆明市五华区圆通北路127号云大晟苑2号门5楼508室")){return "020101011601";}//昆明佰腾总库
        if(mendian.contains("云南省昆明市盘龙区瑞鼎城爱琴海宝略华为授权体验店") && mendian.contains("样机")){return "0201010402";}//昆明瑞鼎城MSC样机库
        if(mendian.contains("云南省昆明市盘龙区瑞鼎城爱琴海宝略华为授权体验店") && !mendian.contains("样机")){return "0201010401";}//昆明瑞鼎城MSC库

        // -----------------------  苏州 ----------------------------------//
        if(mendian.contains("苏州市吴江万象汇爀苒华为授权体验店")){return "0501010301";}//苏州万象汇库
        if(mendian.contains("吴中区爀苒科技丽丰华为合作店")){return "0501010201";}//苏州丽丰库
        if(mendian.contains("苏州市虎丘区永旺梦乐城爀苒华为授权体验店")){return "0501010401";}//苏州新区永旺库
        if(mendian.contains("苏州市姑苏区印象城爀苒华为授权体验店")){return "0501010801";}//苏州印象城库
        if(mendian.contains("苏州市吴江区凤凰荟购物中心爀苒华为授权体验店")){return "0501010101";}//苏州凤凰荟库

        else return "";
    }


    public static Map<String, String> getResultMap(String mendian,String xm){
        //根据 普天的门店名称：返回 部门code，业务员code，供应商code
        Map<String, String> result = new HashMap<String, String>();
        if(mendian.contains("成都") && xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101004");//成都普天
            result.put("departmentCode","HW-SC-CD-CPB");//成都的华为产品部
            result.put("userCode","CD-037");//雍聪
        }
        if(mendian.contains("成都") && !xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101008");//成都普天FD
            result.put("departmentCode","HW-SC-CD-HYQD-QD");//成都华为渠道部
            result.put("userCode","CD-040");//严开伟
        }
        if(mendian.contains("绵阳") && xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101001");//绵阳-普天太力
            result.put("departmentCode","HW-SC-MY-CPB");//绵阳华为产品部
            result.put("userCode","MY-022");// 李文龙
        }
        if(mendian.contains("绵阳") && !xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101002");//绵阳-普天太力FD
            result.put("departmentCode","HW-SC-MY-CPB");//绵阳华为产品部
            result.put("userCode","MY-022");// 李文龙
        }
        if(mendian.contains("上海") && xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101500");
            result.put("departmentCode","HW-SH-CPB");
            result.put("userCode","SH-009");//姚邦峰
        }
        if(mendian.contains("上海") && !xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101501");
            result.put("departmentCode","HW-SH-CPB");
            result.put("userCode","SH-009");//姚邦峰
        }
        if(mendian.contains("昆明") && xm.contains("华为体验店供货平台")){
            result.put("merchantcode","KM000912");
            result.put("departmentCode","HW-YN-KM-CPB");
            result.put("userCode","KM-004");//李海飞
        }
        if(mendian.contains("昆明") && xm.contains("MSC")){
            result.put("merchantcode","KM000913");
            result.put("departmentCode","HW-YN-KM-CPB");
            result.put("userCode","KM-004");//李海飞
        }
        if(mendian.contains("贵阳") && xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101402");//贵阳普天
            result.put("departmentCode","HW-GZ-GY-CPB");//贵阳华为产品部
            result.put("userCode","GY-007");// 李涛
        }
        if(mendian.contains("贵阳") && !xm.contains("华为体验店供货平台")){
            result.put("merchantcode","020101404");//贵阳普天FD
            result.put("departmentCode","HW-GZ-GY-CPB");//贵阳华为产品部
            result.put("userCode","GY-007");// 李涛
        }
        if(mendian.contains("苏州") && !mendian.contains("苏州市姑苏区印象城爀苒华为授权体验店")){
            result.put("merchantcode","020102504");
            result.put("departmentCode","HW-JS-SZ-CPB");
            result.put("userCode","SZ-009");//王来香
        }
        if(mendian.contains("苏州") && mendian.contains("苏州市姑苏区印象城爀苒华为授权体验店")){
            result.put("merchantcode","020101600");//苏州印象城-普天太力
            result.put("departmentCode","HW-JS-SZ-LS-YXC");//苏州华为印象城店
            result.put("userCode","SZ-065");//印象城王来香
        }
        return result;
    }

}
