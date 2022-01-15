package com.example.renyi.controller;

import com.example.renyi.entity.Ptt;

import java.util.Map;

public class Utils {

    public static Map<String, Ptt> pttMap;// 名字 关系 MAP

    public static String getCKbyName(String mendian){
        if("成都新城市广场仁肄商贸华为授权体验店".equals(mendian)){return "0101011001";}
        if("成都龙泉万达仁肄商贸华为授权体验店".equals(mendian)){return "0101010401";}
        if("成都世豪广场仁肄商贸华为授权体验店".equals(mendian)){return "0101010101";}
        if("成都武侯吾悦广场仁肄商贸华为授权体验店".equals(mendian)){return "0101010201";}
        if("成都国贸时尚汇仁肄商贸华为授权体验店".equals(mendian)){return "0101010801";}
        if("成都悠方仁肄商贸华为授权体验店".equals(mendian)){return "0101010601";}
        else return "";
    }


    public static Map<String, Ptt> getRelationMap(Map<String, Ptt> map){
        pttMap = map;
        return pttMap;
    }
}
