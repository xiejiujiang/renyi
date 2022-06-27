package com.example.renyi.utils;

import com.example.renyi.saentity.SaleDeliveryDetails;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Md5 {

    public static String md5(String data){
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes("UTF-8"));
            byte[] digest = md.digest();
            int i;
            for (int offset = 0; offset < digest.length; offset++) {
                i = digest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    sb.append(0);
                sb.append(Integer.toHexString(i));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static String getHQSadetailszpflag(com.example.renyi.saentity.JsonRootBean sajrb){
        String  flag = "0";// 默认不是赠品。
        List<SaleDeliveryDetails> lsit = sajrb.getData().getSaleDeliveryDetails();
        for(SaleDeliveryDetails SaleDeliveryDetail : lsit){
            String zpflag = SaleDeliveryDetail.getIsPresent();// zpflag = 1 代表这个明细行 是 赠品。
            if("1".equals(zpflag)){
                flag = "1";
            }else {
                flag = "0";
                return flag;
            }
        }
        return flag;
    }
}