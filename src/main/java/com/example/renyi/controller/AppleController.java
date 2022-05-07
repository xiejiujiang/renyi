package com.example.renyi.controller;

import ch.qos.logback.core.util.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.renyi.entity.Ptt;
import com.example.renyi.service.BasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
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

@CrossOrigin
@Controller
@RequestMapping(value = "/apple")
public class AppleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.renyi.controller.AppleController.class);

    @Autowired
    private BasicService basicService;


    //订单转换页面
    @RequestMapping(value="/dddr", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView ptdesc(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        LOGGER.info("-------------------  苹果的页面打开事件  ----------------------");
        mav.setViewName("apple/excell");
        return mav;
    }


    //匹配表更新模板
    @RequestMapping(value="/testpp", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView testpp(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("apple/testppp");
        return mav;
    }


    // 导入,转换,下载
    @RequestMapping(value="/upload", method = {RequestMethod.GET,RequestMethod.POST})
    public void upload(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        try {
            // ------------  先解析出 名称匹配的 关系表 --------------------------------//
            Map<String, Ptt> appleMapp = new HashMap<String, Ptt>();
            ClassPathResource classPathResource = new ClassPathResource("excel/appleT.xlsx");
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
                    appleMapp.put(ptt.getPtmc(),ptt);
                }
                LOGGER.info("-------------------- 苹果名称匹配关系表解析成功！--------------------");
            }

            // 根据 苹果的订单excel 转换 成T+ 的 list
            List<Map<String,String>> appleTList = basicService.getAPPLEListByFile(file,appleMapp);
            //----------------------------------读取标准的T+excel，写数据，然后下载--------------------------------------//
            OutputStream out = null;
            BufferedOutputStream bos = null;
            String templateFileName = FileUtil.class.getResource("/").getPath()+"templates"+ File.separator + "tcgdd.xlsx";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String excelName = URLEncoder.encode("whoami.xls", "utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(excelName.getBytes("UTF-8"), "ISO-8859-1"));
            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);
            //读取Excel
            ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(templateFileName).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            //list map 是查询并需导出的数据，并且里面的字段和excel需要导出的字段对应
            // 直接写入Excel数据
            excelWriter.fill(appleTList, writeSheet);
            excelWriter.finish();
            bos.flush();
            LOGGER.info("-------------------- 写入完成，请下载江哥的爱 --------------------");
        }catch (Exception e){
            //e.printStackTrace();
        }
    }


    // 导入 做好的  名称匹配表，格式固定的
    @RequestMapping(value="/updatePTT", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String updatePTT(@RequestParam(value = "file")MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try{
            InputStream inputStream = file.getInputStream();
            // 正式环境的时候 此路径
            File pttexcel = new File("C:\\apache-tomcat-9.0.56\\webapps\\renyi\\WEB-INF\\classes\\excel\\appleT.xlsx");
            if(pttexcel.exists()){
                pttexcel.delete();
            }
            pttexcel.createNewFile();
            ExcelWriter excelWriter = EasyExcel.write(pttexcel).build();
            ExcelListener listener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);

            //已经做好的 普天T+名称匹配表是 从 第一个sheet 的 第一行 开始获取数据
            com.alibaba.excel.metadata.Sheet sheet = new Sheet(1,0,Ptt.class);
            excelReader.read(sheet);
            List<Ptt> ptlist = new ArrayList<Ptt>();//最终用来写入的数据
            List<Object> list = listener.getDatas();//当前从上传的excel中获取的数据
            for(Object oo : list){
                Ptt ptt = (Ptt)oo;
                ptlist.add(ptt);
            }
            WriteSheet writeSheet1 = EasyExcel.writerSheet(1,"英迈SKU匹配表").build();
            excelWriter.write(ptlist, writeSheet1);
            excelWriter.finish();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "0000";
    }

    //下载 已经 存在的 英迈匹配表
    @RequestMapping(value="/downloadexcel", method = {RequestMethod.GET,RequestMethod.POST})
    public void downloadexcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        // 下载本地文件
        String fileName = "appleT.xlsx".toString(); // 文件的默认保存名
        // 读到流中
        InputStream inStream = new FileInputStream("C:\\apache-tomcat-9.0.56\\webapps\\renyi\\WEB-INF\\classes\\excel\\appleT.xlsx");
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0){
                response.getOutputStream().write(b, 0, len);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}