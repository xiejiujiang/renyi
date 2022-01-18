package com.example.renyi.controller;


import ch.qos.logback.core.util.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONObject;
import com.example.renyi.entity.Ptt;
import com.example.renyi.entity.Putian;
import com.example.renyi.entity.TData;
import com.example.renyi.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/xkk")
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.renyi.controller.IndexController.class);

    @RequestMapping(value="/mav", method = RequestMethod.GET) //网址
    public ModelAndView Info(HttpServletRequest request, HttpServletResponse response) {
        String req = request.getParameter("sky");
        ModelAndView mav = new ModelAndView();
        mav.addObject("result",req);
        mav.setViewName("user");
        return mav; //返回user.html
    }


    // 导入。转换下载
    @RequestMapping(value="/upload", method = {RequestMethod.GET,RequestMethod.POST})
    public void upload(@RequestParam(value = "file")MultipartFile file,HttpServletResponse response) throws IOException {
        // ------------  先解析出 名称匹配的 关系表 --------------------------------//
        Map<String,Ptt> pttMapp = new HashMap<String, Ptt>();
        try {
            ClassPathResource classPathResource = new ClassPathResource("excel/ptT.xlsx");
            if(classPathResource.exists()){
                InputStream inputStream = classPathResource.getInputStream();
                ExcelListener listener = new ExcelListener();
                ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
                com.alibaba.excel.metadata.Sheet sheet = new Sheet(1,0, Ptt.class);
                excelReader.read(sheet);
                //获取数据
                List<Object> list = listener.getDatas();
                for(Object oo : list){
                    Ptt ptt = (Ptt)oo;
                    pttMapp.put(ptt.getPtmc(),ptt);
                }
                LOGGER.error("----------------------------- 名称匹配关系表解析成功！-------------------------------------");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        // 解析上传的 excel 文件到 list 里面，并转换成对应的T+ list 数据
        InputStream inputStream = file.getInputStream();
        ExcelListener listener = new ExcelListener();
        ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
        com.alibaba.excel.metadata.Sheet sheet = new Sheet(1,3,Putian.class);
        excelReader.read(sheet);
        List<Object> list = listener.getDatas();

        List<Map<String,String>> ptlist = new ArrayList<Map<String,String>>();
        for(Object oo : list){
            Map<String,String> reobject = new HashMap<String,String>();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
            reobject.put("today",sdf.format(new Date()));//默认是 今天
            reobject.put("djcode","SA-XJJ-0000001");//单号
            reobject.put("merchantcode","020101004");// 成都-普天太力 code
            reobject.put("departmentCode","HW-SC-CD-CPB");//成都华为产品部
            reobject.put("userCode","CD-036");//王洪田
            reobject.put("taxflag","是");//含税

            Putian pt = (Putian)oo;
            String ptmdmc = pt.getMdmc();//普天门店名称
            String ckcode = Utils.getCKbyName(ptmdmc);//T+仓库编码
            reobject.put("ckcode",ckcode);

            String ptmc = pt.getJx()+pt.getYs();
            if(pttMapp.get(ptmc)==null){
                LOGGER.error("---------------- 普天名称："+ptmc+" 对应的 T+ 名称没找到！--------------------");
            }
            String tcode = pttMapp.get(ptmc)==null?"":pttMapp.get(ptmc).getTcode();//对应的 T+ 的 名称编码
            reobject.put("tcode",tcode);
            reobject.put("danwei",pttMapp.get(ptmc)==null?"个":pttMapp.get(ptmc).getTdw());// 对应的T+ 单位

            reobject.put("spdj",pt.getSpdj());//商品单价（含税 13%）
            reobject.put("tax","0.13");//税率
            reobject.put("fhsl",pt.getFhsl());//数量
            reobject.put("taxAcount",""+Float.valueOf(pt.getFhsl())*Float.valueOf(pt.getSpdj()));// 含税金额
            ptlist.add(reobject);
        }
        LOGGER.error("--------------------上传的excel一共解析出了 " + ptlist.size() + " 行数据！开始写入 T+ 标准模板-----------------------");

        //----------------------------------读取标准的T+excel，写数据，然后下载--------------------------------------//
        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            //String templateFileName = "/excel/tcgdd.xlsx";
            String templateFileName = FileUtil.class.getResource("/").getPath()+"templates"+File.separator + "tcgdd.xlsx";
            LOGGER.error("---------------------T+的标准订单导入模板路径：" + templateFileName + "-----------------------------");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("xjj.xls", "utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);
            //读取Excel
            ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(templateFileName).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            //list map 是查询并需导出的数据，并且里面的字段和excel需要导出的字段对应
            // 直接写入Excel数据
            excelWriter.fill(ptlist, writeSheet);
            excelWriter.finish();
            bos.flush();
            LOGGER.error("-----------------------------写入完成，请下载江哥的爱------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}