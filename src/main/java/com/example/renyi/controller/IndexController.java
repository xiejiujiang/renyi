package com.example.renyi.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/xkk")
public class IndexController {


    @RequestMapping(value="/user", method = RequestMethod.GET) //网址
    public String Info(Model model) {
        System.out.println("------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!------");


        model.addAttribute("result", "成功显示学生信息22");
        return "user"; //返回user.html
    }


    @RequestMapping(value="/mav", method = RequestMethod.GET) //网址
    public ModelAndView Info(HttpServletRequest request, HttpServletResponse response) {
        String req = request.getParameter("sky");

        System.out.println("1111111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!------");
        ModelAndView mav = new ModelAndView();
        mav.addObject("result",req);
        mav.setViewName("user");
        return mav; //返回user.html
    }
}