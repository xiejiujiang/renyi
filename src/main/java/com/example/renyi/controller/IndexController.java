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

import com.example.renyi.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
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

    @Autowired
    private OrderService OrderService;

    @RequestMapping(value="/mav", method = RequestMethod.GET) //网址
    public ModelAndView Info(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        //String userid = "";
        //Cookie[] cookies = request.getCookies();
        LOGGER.error("----------------------------------开始！--------------------------------------------");
        /*try{
            for(int i=0;i<cookies.length;i++){
                Cookie cookie = cookies[i];
                //  name == AIPortal_Res_Account,value == 1_23,comment == null ,其中 23 是 eap_user（用户权限里面的用户ID）
                LOGGER.error("name == "+cookie.getName()+",value == " + cookie.getValue()+",comment == "+cookie.getComment());
                if("AIPortal_Res_Account".equals(cookie.getName())){
                    String value = cookie.getValue();
                    userid = value.substring(value.indexOf("_")+1 ,value.length());
                    break;
                }
            }
            String idperson = OrderService.getTnameByCode(userid);
            request.getSession().putValue("idperson",idperson);
            mav.addObject("result",idperson);// 员工ID
        }catch (Exception e){
            e.printStackTrace();
        }*/
        mav.setViewName("user");
        LOGGER.error("----------------------------------是谁打开了江哥的页面！--------------------------------------------");
        return mav; //返回user.html
    }


    // 导入。转换下载
    @RequestMapping(value="/upload", method = {RequestMethod.GET,RequestMethod.POST})
    public void upload(@RequestParam(value = "file")MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws IOException {
        // ------------  先解析出 名称匹配的 关系表 --------------------------------//
        Map<String,Ptt> pttMapp = new HashMap<String, Ptt>();
        try {
            //String idperson = request.getParameter("idperson");
            //LOGGER.error("------------------- 传进来的idperson === " + idperson + "--------------------------");
            //String idperson1 = (String) request.getSession().getValue("idperson");
            //LOGGER.error("------------------- Session中的idperson === " + idperson1 + "--------------------------");

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
                LOGGER.error("-------------------- 名称匹配关系表解析成功！--------------------");
            }


            // 解析上传的 excel 文件到 list 里面，并转换成对应的T+ list 数据
            InputStream inputStream = file.getInputStream();
            ExcelListener listener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
            com.alibaba.excel.metadata.Sheet sheet = new Sheet(1,3,Putian.class);
            excelReader.read(sheet);

            List<Map<String,String>> ptlist = new ArrayList<Map<String,String>>();
            //String timeflag = System.currentTimeMillis()+"";//时间戳，用来标记单号，以后应该会换成普天系统的单号
            List<Object> list = listener.getDatas();
            for(Object oo : list){
                Map<String,String> reobject = new HashMap<String,String>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                reobject.put("today",sdf.format(new Date()));//默认是 今天

                Putian pt = (Putian)oo;
                String ptmdmc = pt.getAddress();//普天门店地址，返回门店名称
                Map<String,String> ckmap = Utils.getCKbyName(ptmdmc);//T+仓库编码
                if(ckmap == null || "".equals(ckmap.get("ckcode"))){
                    LOGGER.error("-------------------- 地址名称是：" + ptmdmc + " ,没有找到对应的仓库名称！！！ 请检查代码配置");
                }
                reobject.put("ckcode",ckmap.get("ckcode"));
                reobject.put("ckname",ckmap.get("ckname"));//T+ 仓库名称

                // ---- 注意！ 这里根据 ptmdmc 普天系统里的门店名称 来 判断出 地区：往来单位：部门：业务员。 且！ 每个仓库 需要 不同的 订单号 -----//
                reobject.put("djcode",pt.getOrdernmb());//单号
                // 供应商 code
                reobject.put("merchantcode",Utils.getResultMap(ptmdmc,pt.getXm()).get("merchantcode"));
                reobject.put("merchantname",Utils.getResultMap(ptmdmc,pt.getXm()).get("merchantname"));
                // 部门 code
                reobject.put("departmentCode",Utils.getResultMap(ptmdmc,pt.getXm()).get("departmentCode"));
                reobject.put("departmentName",Utils.getResultMap(ptmdmc,pt.getXm()).get("departmentName"));
                // 业务员 code
                reobject.put("userCode",Utils.getResultMap(ptmdmc,pt.getXm()).get("userCode"));
                reobject.put("userName",Utils.getResultMap(ptmdmc,pt.getXm()).get("userName"));
                //含税
                reobject.put("taxflag","是");

                String ptmc = pt.getJx()+pt.getYs();
                if(pttMapp.get(ptmc) == null || "".equals(pttMapp.get(ptmc))){
                    LOGGER.error("-------------------- 普天名称："+ptmc+" 对应的 T+ 名称没找到！请及时更新excel匹配表");
                }
                String tcode = pttMapp.get(ptmc)==null?"":pttMapp.get(ptmc).getTcode();//对应的 T+ 的 编码
                String tname = pttMapp.get(ptmc)==null?"":pttMapp.get(ptmc).getTname();//对应的 T+ 的 名称
                reobject.put("tcode",tcode);//对应的 T+ 的 编码
                reobject.put("tname",tname);//对应的 T+ 的 名称
                reobject.put("danwei",pttMapp.get(ptmc)==null?"个":pttMapp.get(ptmc).getTdw());// 对应的T+ 单位

                reobject.put("spdj",pt.getSpdj());//商品单价（含税 13%）
                reobject.put("tax","0.13");//税率
                reobject.put("fhsl",pt.getSpsl());//数量
                reobject.put("taxAcount",""+Float.valueOf(pt.getSpsl())*Float.valueOf(pt.getSpdj()));// 含税金额
                ptlist.add(reobject);
            }
            LOGGER.error("-------------------- 上传的excel一共解析出了 " + ptlist.size() + " 行数据！开始写入 T+ 标准模板");

            //----------------------------------读取标准的T+excel，写数据，然后下载--------------------------------------//
            OutputStream out = null;
            BufferedOutputStream bos = null;
            //String templateFileName = "/excel/tcgdd.xlsx";
            String templateFileName = FileUtil.class.getResource("/").getPath()+"templates"+File.separator + "tcgdd.xlsx";
            LOGGER.error("-------------------- T+的标准订单导入模板路径：" + templateFileName + "");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("whoami.xls", "utf-8");
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
            LOGGER.error("-------------------- 写入完成，请下载江哥的爱 --------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}