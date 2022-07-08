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

import com.example.renyi.saentity.JsonRootBean;
import com.example.renyi.service.BasicService;
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
import sun.misc.BASE64Decoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@Controller
@RequestMapping(value = "/xkk")
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.renyi.controller.IndexController.class);

    @Autowired
    private OrderService OrderService;

    @Autowired
    private BasicService basicService;

    @RequestMapping(value="/mav", method = {RequestMethod.GET,RequestMethod.POST}) //网址
    public ModelAndView Info(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        LOGGER.info("----------------------------------开始！--------------------------------------------");
        /* 这部分代码是我之前测试 获取用户登录信息的，只要 你的 T+ 和 你这个自定义页面 在 相同的域名下，就可以 这样获取。
        String userid = "";
        Cookie[] cookies = request.getCookies();
        try{
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
        LOGGER.info("----------------------------------是谁打开了江哥的页面！--------------------------------------------");
        return mav; //返回user.html
    }


    // 导入。转换下载
    @RequestMapping(value="/upload", method = {RequestMethod.GET,RequestMethod.POST})
    public void upload(@RequestParam(value = "file")MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws IOException {
        try {
            // ------------  先解析出 名称匹配的 关系表 --------------------------------//
            Map<String,Ptt> pttMapp = new HashMap<String, Ptt>();
            ClassPathResource classPathResource = new ClassPathResource("excel/ptT.xlsx");
            String fileName = file.getOriginalFilename();// 获取 传入的 文件 名称！
            LOGGER.info("fileName ========================= " + fileName);
            if(classPathResource.exists()){
                InputStream inputStream = classPathResource.getInputStream();
                ExcelListener listener = new ExcelListener();
                ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);

                if(fileName.contains("普天")){
                    LOGGER.info("--------------------------- 开始解析 普天  ---------------------------" + fileName.contains("普天"));
                    com.alibaba.excel.metadata.Sheet sheet = new Sheet(1,0, Ptt.class);
                    excelReader.read(sheet);
                }
                if(fileName.contains("中邮")){
                    LOGGER.info("--------------------------- 开始解析 中邮  ---------------------------" + fileName.contains("中邮"));
                    com.alibaba.excel.metadata.Sheet sheet = new Sheet(3,0, Ptt.class);
                    excelReader.read(sheet);
                    com.alibaba.excel.metadata.Sheet sheet2 = new Sheet(4,0, Ptt.class);
                    excelReader.read(sheet2);
                }
                if(fileName.contains("MSC")){
                    LOGGER.info("--------------------------- 开始解析 MSC  ---------------------------" + fileName.contains("MSC"));
                    com.alibaba.excel.metadata.Sheet sheet = new Sheet(2,0, Ptt.class);
                    excelReader.read(sheet);
                }

                //获取数据
                List<Object> list = listener.getDatas();
                for(Object oo : list){
                    Ptt ptt = (Ptt)oo;
                    pttMapp.put(ptt.getPtmc(),ptt);
                }
                LOGGER.info("-------------------- 名称匹配关系表解析成功！--------------------");
            }

            // 根据 普天的订单excel 转换 成T+ 的 list
            List<Map<String,String>> ptlist = new ArrayList<Map<String,String>>();
            if(fileName.contains("普天")){
                ptlist = basicService.getPUTIANListByFile(file,pttMapp);
            }
            if(fileName.contains("中邮")){
                ptlist = basicService.getZYOUListByFile(file,pttMapp);
            }
            if(fileName.contains("MSC")){
                ptlist = basicService.getMSCListByFile(file,pttMapp);
            }

            //----------------------------------读取标准的T+excel，写数据，然后下载--------------------------------------//
            OutputStream out = null;
            BufferedOutputStream bos = null;
            String templateFileName = FileUtil.class.getResource("/").getPath()+"templates"+File.separator + "tcgdd.xlsx";
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
            excelWriter.fill(ptlist, writeSheet);
            excelWriter.finish();
            bos.flush();
            LOGGER.info("-------------------- 写入完成，请下载江哥的爱 --------------------");

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }


    // 只能让 叶庆 导入 做好的  名称匹配表
    @RequestMapping(value="/updatePTT", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String updatePTT(@RequestParam(value = "file")MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws IOException {
        //思路： 1. 叶庆上传做好的 普天T+ 名称匹配表
        //      2. 解析除结果之后。放入LIST<Ptt>
        //      3. 删掉之前的excel，重新创建写入一个新的即可
        // 解析上传的 excel 文件到 list 里面，并转换成对应的T+ list 数据
        InputStream inputStream = file.getInputStream();
        // 正式环境的时候 此路径
        File pttexcel = new File("C:\\apache-tomcat-9.0.56\\webapps\\renyi\\WEB-INF\\classes\\excel\\ptT.xlsx");
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
        WriteSheet writeSheet1 = EasyExcel.writerSheet(1,"普天太力-存货转换").build();
        excelWriter.write(ptlist, writeSheet1);
        //excelWriter.finish();

        //ExcelListener listener2 = new ExcelListener();
        //ExcelReader excelReader2 = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
        //已经做好的 普天T+名称匹配表是 从 第一个sheet 的 第一行 开始获取数据
        com.alibaba.excel.metadata.Sheet sheet2 = new Sheet(2,0,Ptt.class);
        excelReader.read(sheet2);
        List<Ptt> ptlist2 = new ArrayList<Ptt>();//最终用来写入的数据
        List<Object> list2 = listener.getDatas();//当前从上传的excel中获取的数据
        for(Object oo : list2){
            Ptt ptt = (Ptt)oo;
            ptlist2.add(ptt);
        }

        List<Ptt>  pll2 = ptlist2.subList(ptlist.size(),list2.size());
        WriteSheet writeSheet2 = EasyExcel.writerSheet(2,"PM系统-存货转换").build();
        excelWriter.write(pll2, writeSheet2);
        //excelWriter.finish();


        //ExcelListener listener3 = new ExcelListener();
        //ExcelReader excelReader3 = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
        //已经做好的 普天T+名称匹配表是 从 第一个sheet 的 第一行 开始获取数据
        com.alibaba.excel.metadata.Sheet sheet3 = new Sheet(3,0,Ptt.class);
        excelReader.read(sheet3);
        List<Ptt> ptlist3 = new ArrayList<Ptt>();//最终用来写入的数据
        List<Object> list3 = listener.getDatas();//当前从上传的excel中获取的数据
        for(Object oo : list3){
            Ptt ptt = (Ptt)oo;
            ptlist3.add(ptt);
        }
        List<Ptt>  pll3 = ptlist3.subList(ptlist.size()+pll2.size(),list3.size());
        WriteSheet writeSheet3 = EasyExcel.writerSheet(3,"中邮-存货转换").build();
        excelWriter.write(pll3, writeSheet3);
        //excelWriter.finish();


        //ExcelListener listener4 = new ExcelListener();
        //ExcelReader excelReader4 = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
        //已经做好的 普天T+名称匹配表是 从 第一个sheet 的 第一行 开始获取数据
        com.alibaba.excel.metadata.Sheet sheet4 = new Sheet(4,0,Ptt.class);
        excelReader.read(sheet4);
        List<Ptt> ptlist4 = new ArrayList<Ptt>();//最终用来写入的数据
        List<Object> list4 = listener.getDatas();//当前从上传的excel中获取的数据
        for(Object oo : list4){
            Ptt ptt = (Ptt)oo;
            ptlist4.add(ptt);
        }
        WriteSheet writeSheet4 = EasyExcel.writerSheet(4,"中邮全屋智能-存货转换").build();
        excelWriter.write(ptlist4.subList(ptlist.size()+pll2.size()+pll3.size(),list4.size()), writeSheet4);
        excelWriter.finish();

        return "0000";
    }



    //下载excel
    @RequestMapping(value="/downloadexcel", method = {RequestMethod.GET,RequestMethod.POST})
    public void downloadexcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        // 下载本地文件
        String fileName = "ptT.xlsx".toString(); // 文件的默认保存名
        // 读到流中
        InputStream inStream = new FileInputStream("C:\\apache-tomcat-9.0.56\\webapps\\renyi\\WEB-INF\\classes\\excel\\ptT.xlsx");
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


    @RequestMapping(value="/testexcel", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView testexcel(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("excell");
        return mav;
    }


    @RequestMapping(value="/ptdesc", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView ptdesc(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ptdesc");
        return mav;
    }

    @RequestMapping(value="/testpp", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView testpp(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("testppp");
        return mav;
    }


    // 弃用！！！  由于在选项设置中 设置了图片上传至本地，所以，可以直接拿到图片了！
    //T+ 审核后 调用 此接口。 先 调用 HQ的配单 接口，再调用 图片上传接口
    @RequestMapping(value="/doBase64", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String doBase64(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        /*String imgNameFlag = request.getParameter("imgNameFlag");
        String code = request.getParameter("code");//单据编号
        //System.out.println("前台传入的 code ： " + code);
        String base64Img = request.getParameter("base64Img");
        String debase64Img = URLDecoder.decode(base64Img);// 哈哈，这个就是 解码之后的
        System.out.println("前台传入的 base64编码 解码之后 ： " + debase64Img);
        String headstr = debase64Img.substring(0,debase64Img.indexOf(",")+1);
        //System.out.println("保留base64字符串的开头部分 ： " + headstr);
        //去掉base64字符串的开头部分
        String r3 = debase64Img.substring(debase64Img.indexOf(",")+1);
        //System.out.println("去掉base64字符串的开头部分 ： " + r3);

        //先上传 配货单，后 调用 红旗的 图片上传接口，根据 voucherCode 查询 此 销货单的明细内容
        Map<String,String> pas = new HashMap<String,String>();
        pas.put("OrgId","OrgId");
        pas.put("code",code);//销货单的单号
        // 通过 OrgId 来获取 AppKey 和 AppSecret
        //Map<String,String> apk = orderMapper.getAppKeySecretByAppKey(OrgId);
        pas.put("AppKey","djrUbeB2");// 直接 写 死 ！
        pas.put("AppSecret","F707B3834D9448B2A81856DE4E42357A");// 直接 写 死 ！
        JsonRootBean sajrb = basicService.getSaOrder(pas);//返回了 T+ 销货单的实体类
        LOGGER.error("这个销货单 的 明细 内容 " + sajrb.toString());//这个销货单 的 明细 内容。
        //调用 新的 services 转换成HQ 的参数，并调用HQ接口，返回结果
        if(sajrb.getData().getBusinessType().getName().equals("普通销售")){
            String HQresult = basicService.HQsaorder(sajrb); //红旗文档1.1
            if(HQresult.equals(200)){
                // 先上传 配货单，后 调用 红旗的 图片上传接口
                basicService.HQimage(code,r3);
            }
        }else{
            String HQresult = basicService.HQsabackorder(sajrb);//销售退货  红旗文档1.2
            if(HQresult.equals(200)){
                // 先上传 配货单，后 调用 红旗的 图片上传接口
                basicService.HQimage(code,r3);
            }
        }*/
        return "0000";
    }


    //根据单据编号 获取  附件内容
    @RequestMapping(value="/getfjidByCode", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String getfjidByCode(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        String code = request.getParameter("code");//单据编号
        LOGGER.info("点击审核后，根据传入的code，获取附件相关信息 ============================== "  +  code );
        List<Map<String,String>> fjs = basicService.getfjidByCode(code);
        String id = fjs.get(0).get("id");
        String imagSize = fjs.get(0).get("FileSize");
        Map<String,Object> imgMap = new HashMap<String,Object>();
        imgMap.put("id",id);
        imgMap.put("imagSize",imagSize);
        JSONObject job = new JSONObject(imgMap);
        return job.toJSONString();
    }


    //根据 部门 查询当前 库存结存金额+采购订单未执行的金额
    @RequestMapping(value="/getDistricntKC", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String getDistricntKC(HttpServletRequest request, HttpServletResponse response){
        String department = request.getParameter("department");
        String thistotal = request.getParameter("thistotal");
        LOGGER.error("---------------department == "+department+" ---------------");
        LOGGER.error("---------------thistotal == "+thistotal+" ---------------");
        String result = OrderService.getDistricntKC(department,thistotal);
        return result;
    }
}