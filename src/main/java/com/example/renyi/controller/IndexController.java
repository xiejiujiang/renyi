package com.example.renyi.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.example.renyi.entity.User;
import org.apache.poi.sl.usermodel.Sheet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/xkk")
public class IndexController {


    /*@RequestMapping(value="/user", method = RequestMethod.GET) //网址
    public String Info(Model model, File file) {


        try {
            //获取文件名
            String filename = file.getName();
            //获取文件流
            InputStream inputStream = new FileInputStream(file);
            //实例化实现了AnalysisEventListener接口的类
            ExcelListener listener = new ExcelListener();
            //传入参数
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
            //读取信息
            //excelReader.read(new Sheet(1, 0, User.class));
            //获取数据
            List<Object> list = listener.getDatas();
            if (list.size() > 1) {
                for (int i = 0; i < list.size(); i++) {
                    //Testobj = (User) list.get(i);
                    JSONObject jo = new JSONObject();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



        return "user"; //返回user.html
    }*/


    @RequestMapping(value="/mav", method = RequestMethod.GET) //网址
    public ModelAndView Info(HttpServletRequest request, HttpServletResponse response) {
        String req = request.getParameter("sky");

        System.out.println("1111111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!------");
        ModelAndView mav = new ModelAndView();
        mav.addObject("result",req);
        mav.setViewName("user");

        return mav; //返回user.html
    }


    @RequestMapping(value="/daochu", method = RequestMethod.GET) //网址
    public String daochu(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<User> userList = new ArrayList<User>();
            String filenames = "111111";
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                filenames = URLEncoder.encode(filenames, "UTF-8");
            } else {
                filenames = new String(filenames.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "filename=" + filenames + ".xlsx");
            EasyExcel.write(response.getOutputStream(), User.class).sheet("sheet").doWrite(userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user"; //返回user.html
    }

    /*@RequestMapping(value="/excel", method = RequestMethod.GET) //网址
    public ModelAndView excel(HttpServletRequest request, HttpServletResponse response) {
        String req = request.getParameter("sky");
        List<User> testList = new ArrayList<User>();
        try {
            String strUrl = "C:\\Users\\Administrator\\Desktop\\json.xlsx";
            File multipartFile = new File(strUrl);
            InputStream inputStream = new FileInputStream(multipartFile);
            //实例化实现了AnalysisEventListener接口的类
            ExcelListener listener = new ExcelListener();
            //传入参数
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
            //读取信息
            //excelReader.read(new Sheet(1, 0, User.class));
            //获取数据
            *//*List<Object> list = listener.getDatas();
            if (list.size() > 1) {
                for (int i = 0; i < list.size(); i++) {
                    User user = (User) list.get(i);
                    System.out.println("user == " + user.toString());
                }
            }*//*
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String strUrl = "C:\\Users\\Administrator\\Desktop\\json"+System.currentTimeMillis()+".xlsx";
            EasyExcel.write(strUrl,User.class).sheet("sheet").doWrite(testList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("user");
        return mav; //返回user.html
    }*/


    // 导出
    @RequestMapping(value="/download", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<User> data = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("张四" + i);
            user.setId(18 + i);
            data.add(user);
        }
        EasyExcel.write(response.getOutputStream(), User.class).sheet("模板").doWrite(data);
    }


    // 导入
    @RequestMapping(value="/upload", method = RequestMethod.GET)
    public void upload(MultipartFile file,HttpServletResponse response) throws IOException {
        //写法一
        User user = new User();
        // sheet里面可以传参 根据sheet下标读取或者根据名字读取 不传默认读取第一个
        EasyExcel.read(file.getInputStream(), User.class, new ExcelListener(user)).sheet().doRead();

        // 写法2：
        /*ExcelReader excelReader = EasyExcel.read(file.getInputStream(), Student.class, new StudentListener(studentDao)).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();*/



        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<User> data = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User uu = new User();
            uu.setName("张四" + i);
            uu.setId(18 + i);
            data.add(uu);
        }
        EasyExcel.write(response.getOutputStream(), User.class).sheet("模板").doWrite(data);
    }
}