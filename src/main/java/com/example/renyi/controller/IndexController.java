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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/xkk")
public class IndexController {

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
        Map<String,Ptt> pttMapp = new HashMap<String, Ptt>();// 名字 关系 MAP
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
                System.out.println("list.size() == " + list.size());
                for(Object oo : list){
                    Ptt ptt = (Ptt)oo;
                    pttMapp.put(ptt.getPtmc(),ptt);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        InputStream inputStream = file.getInputStream();
        //实例化实现了AnalysisEventListener接口的类
        ExcelListener listener = new ExcelListener();
        //传入参数
        ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
        //读取信息
        com.alibaba.excel.metadata.Sheet sheet = new Sheet(1,3,Putian.class);
        excelReader.read(sheet);

        //获取数据
        List<Object> list = listener.getDatas();
        List<Map<String,String>> ptlist = new ArrayList<Map<String,String>>();
        for(Object oo : list){
            Map<String,String> reobject = new HashMap<String,String>();
            reobject.put("today","2022-01-15");
            reobject.put("djcode","SA-XJJ-0000001");
            reobject.put("merchantcode","020101004");
            reobject.put("departmentCode","HW-SC-CD-CPB");
            reobject.put("userCode","CD-036");
            reobject.put("taxflag","1");//含税

            Putian pt = (Putian)oo;
            String ptmdmc = pt.getMdmc();//普天门店名称
            String tmdcode = Utils.getCKbyName(ptmdmc);//T+门店编码
            reobject.put("tmdcode",tmdcode);

            String ptmc = pt.getJx()+pt.getYs();
            System.out.println("ptmc == " + ptmc );
            String tcode = pttMapp.get(ptmc)==null?"":pttMapp.get(ptmc).getTcode();//对应的 T+ 的 名称编码
            reobject.put("tcode",tcode);
            reobject.put("danwei",pttMapp.get(ptmc)==null?"个":pttMapp.get(ptmc).getTdw());

            reobject.put("spdj",pt.getSpdj());//商品单价（含税 13%）
            reobject.put("fhsl",pt.getFhsl());//数量
            ptlist.add(reobject);
        }
        System.out.println("一共读取了 " + ptlist.size() + " 行数据！");//ptlist 包含了所有门店的下货内容。


        //----------------------------------读取标准的T+excel，写数据，然后下载--------------------------------------//

        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            //String templateFileName = "/excel/tcgdd.xlsx";
            String templateFileName = FileUtil.class.getResource("/").getPath()+"templates"+File.separator + "tcgdd.xls";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("下载后的名称.xls", "utf-8");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}