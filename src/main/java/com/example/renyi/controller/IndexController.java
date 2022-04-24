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
        LOGGER.error("----------------------------------开始！--------------------------------------------");
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
            String templateFileName = FileUtil.class.getResource("/").getPath()+"templates"+File.separator + "tcgdd.xlsx";
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


    // 只能让 叶庆 导入 做好的 普天T+ 名称匹配表
    @RequestMapping(value="/updatePTT", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String updatePTT(@RequestParam(value = "file")MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws IOException {
        //思路： 1. 叶庆上传做好的 普天T+ 名称匹配表
        //      2. 解析除结果之后。放入LIST<Ptt>
        //      3. 删掉之前的excel，重新创建写入一个新的即可
        // 解析上传的 excel 文件到 list 里面，并转换成对应的T+ list 数据
        InputStream inputStream = file.getInputStream();
        ExcelListener listener = new ExcelListener();
        ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
        com.alibaba.excel.metadata.Sheet sheet = new Sheet(1,0,Ptt.class);//已经做好的 普天T+名称匹配表是 从 第一个sheet 的 第一行 开始获取数据
        excelReader.read(sheet);

        List<Ptt> ptlist = new ArrayList<Ptt>();//最终用来写入的数据
        List<Object> list = listener.getDatas();//当前从上传的excel中获取的数据
        for(Object oo : list){
            Ptt ptt = (Ptt)oo;
            ptlist.add(ptt);
        }

        // 正式环境的时候 需要修改 此路径
        File pttexcel = new File("C:\\apache-tomcat-9.0.56\\webapps\\renyi\\WEB-INF\\classes\\excel\\ptT.xlsx");
        if(pttexcel.exists()){
            pttexcel.delete();
        }
        pttexcel.createNewFile();

        ExcelWriter excelWriter = EasyExcel.write(pttexcel).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").build();
        excelWriter.write(ptlist, writeSheet);
        excelWriter.finish();

        return "0000";
    }



    //下载excel
    @RequestMapping(value="/downloadexcel", method = {RequestMethod.GET,RequestMethod.POST})
    public void downloadexcel(HttpServletRequest request,HttpServletResponse response) throws Exception {

        //ClassPathResource classPathResource = new ClassPathResource("excel/ptT.xlsx");
        //File pttexcel = new File("C:\\apache-tomcat-9.0.56\\webapps\\renyi\\WEB-INF\\classes\\excel\\ptT.xlsx");

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
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
                inStream.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }


    @RequestMapping(value="/testexcel", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView testexcel(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("excel");
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
        mav.setViewName("testpp");
        return mav;
    }


    @RequestMapping(value="/doBase64", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String doBase64(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        String imgNameFlag = request.getParameter("imgNameFlag");
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
        // 直接 写 死 ！
        pas.put("AppKey","");
        pas.put("AppSecret","");
        JsonRootBean sajrb = basicService.getSaOrder(pas);//返回了 T+ 销货单的实体类
        LOGGER.error("这个销货单 的 明细 内容 " + sajrb.toString());//这个销货单 的 明细 内容。
        //调用 新的 services 转换成HQ 的参数，并调用HQ接口，返回结果
        if(sajrb.getData().getBusinessType().getName().equals("普通销售")){
            String HQresult = basicService.HQsaorder(sajrb); //红旗文档1.1
        }else{
            String HQresult = basicService.HQsabackorder(sajrb);//销售退货  红旗文档1.2
        }

        // 先上传 配货单，后 调用 红旗的 图片上传接口
        basicService.HQimage(code,r3);

        //下面这部分代码是 流 转 文件下载 用的。 测试通过后就注销了。
        /*String filepath = "D:\\new9.jpg";
        BASE64Decoder decoder = new BASE64Decoder();
        //Base64解码
        byte[] b = decoder.decodeBuffer(r3);
        for(int i=0;i<b.length;i++) {
            if(b[i]<0) {//调整异常数据
                b[i]+=256;
            }
        }
        //生成jpeg图片
        OutputStream out = new FileOutputStream(filepath);
        out.write(b);
        out.flush();
        out.close();*/
        //System.out.println("图片格式为："+ imgIndex);
        //System.out.println(base64Img.substring(0,base64Img.indexOf(",")));;
        //System.out.println(base64Img.substring(base64Img.indexOf(",")+1,base64Img.length()));;
        return "0000";
    }


    //根据单据编号 获取  附件内容
    @RequestMapping(value="/getfjidByCode", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String getfjidByCode(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        String code = request.getParameter("code");//单据编号
        LOGGER.info("code ============================== "  +  code );
        List<Map<String,String>> fjs = basicService.getfjidByCode(code);
        String id = fjs.get(0).get("id");
        String imagSize = fjs.get(0).get("FileSize");
        Map<String,Object> imgMap = new HashMap<String,Object>();
        imgMap.put("id",id);
        imgMap.put("imagSize",imagSize);
        JSONObject job = new JSONObject(imgMap);
        return job.toJSONString();
    }
}