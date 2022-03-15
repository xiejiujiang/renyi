package com.example.renyi.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Controller
@RequestMapping(value = "/token")
public class TokenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.renyi.controller.TokenController.class);

    //这个里面 主要 用来 接受 code ,刷新 token ，更新对应的数据库

    @RequestMapping(value="/recode", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String recode(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("------------------------------- 正式OAuth回调地址  ------------------------------------------");
        String code = request.getParameter("code");
        //第一次授权后，会有这个code,立刻调用 一次 授权码换token接口 ，拿到完整的 token 相关信息，并写入数据库。


        return code;
    }


    @RequestMapping(value="/ticket", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String reticket(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("------------------------------- 正式消息接收地址  ------------------------------------------");
        return "heheda";
    }


    //消息订阅验证接口
    @RequestMapping(value="/dy2kai", method = {RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody String dy2kai(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        String echostr = request.getParameter("echostr");
        try{
            String nonce = request.getParameter("nonce");
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");

            InputStreamReader reader=new InputStreamReader(request.getInputStream(),"utf-8");
            BufferedReader buffer=new BufferedReader(reader);
            String params=buffer.readLine();
            LOGGER.error("请求参数: "+params);
            JSONObject jsonObject = JSONObject.parseObject(params);
            LOGGER.error("Code =============== " + jsonObject.getString("Code"));
            LOGGER.error("当前操作，0 保存，1 审核，2 弃审，3 删除，4 取消中止，5 中止");
            LOGGER.error("SendState =============== " + jsonObject.getString("SendState"));
            LOGGER.error("-----------------------------------操作结束-----------------------------------");
        }catch (Exception e){
            e.printStackTrace();
        }
        return echostr;
    }

}