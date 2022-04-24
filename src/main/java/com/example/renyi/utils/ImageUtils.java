package com.example.renyi.utils;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtils {

    /**
     * 将网络图片进行Base64位编码,图片的url路径，如http://.....xx.jpg
     */
    public static String encodeImgageToBase64(URL imageUrl) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageUrl);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(outputStream.toByteArray());
    }



    /**
     * 将本地图片进行Base64位编码
     */
    public static String encodeImgageToBase64(File imageFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(outputStream.toByteArray());
    }


    //通过base64位 判断图片有多少kb
    public static Integer getimageSize(String image){//参数就是上面的编码之后的str
        //需要先去掉头部的 data:image/jpeg;base64,
        String str = image.substring(image.indexOf(";base64,")+8,image.length());
        Integer equalIndex = str.indexOf("=");//找到 等号，把 等号 也去掉
        if(str.indexOf("=") > 0 ){
            str = str.substring(0,equalIndex);
        }
        Integer strLength = str.length();// 原来的字符流大小，单位为 字节
        Integer size = strLength - (strLength/8)*2; //计算后得到的文件流大小，单位为字节
        return size/1024;
    }



    public static void main(String[] args) throws  Exception{
        String st1 = ImageUtils.encodeImgageToBase64(new URL("http://ra03a3vjl.hn-bkt.clouddn.com/1.png"));
        System.out.println("st1 == " + st1);
        //int ss = ImageUtils.getimageSize(st1);
        //System.out.println("ss == " + ss);
    }
}
