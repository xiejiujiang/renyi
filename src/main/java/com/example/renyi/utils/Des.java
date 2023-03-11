package com.example.renyi.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class Des {

    //des加密
    public static String desEncrypt(String key, String data) {
        byte[] retVal = null;
        try {
            byte[] key_byte  = key.getBytes("UTF-8");
            byte[] data_byte = data.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key_byte);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key_byte);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            retVal = cipher.doFinal(data_byte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(retVal);
    }

    //des解密
    public static String desDecrypt(String key, String data) {
        byte[] retVal = null;
        try {
            byte[] key_byte  = key.getBytes("UTF-8");
            byte[] data_byte = Base64.getDecoder().decode(data.getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key_byte);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key_byte);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            retVal = cipher.doFinal(data_byte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(retVal);
    }


    public static void main(String[] args) {
        String enstr = Des.desEncrypt("8aue2u3q","{\"sign\":\"51b978772579dc7acb658b2ee0225eff\",\"lnkshpno\":\"A2022248280\",\"hndno\":\"05192A2022248280\",\"prvid\":\"05192 \",\"dptid\":\"6702\",\"mkdat\":\"20221118215333\",\"sndusr\":\"15008463848\",\"snddat\":\"20221118\",\"brief\":\"null\",\"items\":[{ \"gdsid\":\"18002358\",\"prvgdsid\":\"0100007\",\"oriqty\":\"5.0000\",\"qty\":\"88.0000\",\"diffqty\":\"-83\",\"prvprc\":\"3.9500\",\"prvamt\":\"19.7500\",\"bthno\":\"0\",\"vlddat\":\"0\",\"crtdat\":\"0\"},{ \"gdsid\":\"18002821\",\"prvgdsid\":\"0100010\",\"oriqty\":\"7.0000\",\"qty\":\"7.0000\",\"diffqty\":\"\",\"prvprc\":\"3.1000\",\"prvamt\":\"21.7000\",\"bthno\":\"0\",\"vlddat\":\"0\",\"crtdat\":\"0\"},{ \"gdsid\":\"18002959\",\"prvgdsid\":\"0100013\",\"oriqty\":\"6.0000\",\"qty\":\"6.0000\",\"diffqty\":\"\",\"prvprc\":\"3.7500\",\"prvamt\":\"22.5000\",\"bthno\":\"0\",\"vlddat\":\"0\",\"crtdat\":\"0\"},{ \"gdsid\":\"18003111\",\"prvgdsid\":\"0100017\",\"oriqty\":\"5.0000\",\"qty\":\"5.0000\",\"diffqty\":\"\",\"prvprc\":\"4.0500\",\"prvamt\":\"20.2500\",\"bthno\":\"0\",\"vlddat\":\"0\",\"crtdat\":\"0\"},{ \"gdsid\":\"18003536\",\"prvgdsid\":\"0100033\",\"oriqty\":\"5.0000\",\"qty\":\"5.0000\",\"diffqty\":\"\",\"prvprc\":\"3.4000\",\"prvamt\":\"17.0000\",\"bthno\":\"0\",\"vlddat\":\"0\",\"crtdat\":\"0\"},{ \"gdsid\":\"18003354\",\"prvgdsid\":\"0100023\",\"oriqty\":\"10.0000\",\"qty\":\"10.0000\",\"diffqty\":\"\",\"prvprc\":\"3.4300\",\"prvamt\":\"34.3000\",\"bthno\":\"0\",\"vlddat\":\"0\",\"crtdat\":\"0\"}]}");
        System.out.println("enstr == " + enstr);
        String decryptData = Des.desDecrypt("8aue2u3q", enstr);
        System.out.println("decryptData == " + decryptData);
    }
}