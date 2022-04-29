package com.example.renyi.service;

import com.example.renyi.entity.Ptt;
import com.example.renyi.saentity.JsonRootBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface BasicService {

    //这个里面 封装了 所有  T+ 基础类的 接口API 调用方法

    //创建项目
    public String createXM(Map<String,String> params);

    //查询项目
    public String getXM(Map<String,String> params);

    //创建项目分类
    public String createXMClass(Map<String,String> params);

    //查询项目分类
    public String getXMClass(Map<String,String> params);

    //查询仓库
    public String getWarehouse(Map<String,String> params);

    //创建仓库
    public String createWarehouse(Map<String,String> params);

    //创建凭证
    public String createPZ(Map<String,String> params);


    //查询销货单详情接口
    public JsonRootBean getSaOrder(Map<String,String> params);

    //根据 销货单str 访问 HQ 的  上传直配送货单
    public String  HQsaorder(JsonRootBean jrb);


    //根据 销货单str 访问 HQ 的  上传直配 退货单
    public String  HQsabackorder(JsonRootBean jrb);

    //根据 单据编号 返回 附件 ID
    public List<Map<String,String>> getfjidByCode(String code);

    //根据 base64的图片编码 和 code 调用红旗的上传图片
    public String  HQimage(String code,String base64img);


    public List<Map<String,String>> getPUTIANListByFile(MultipartFile file, Map<String, Ptt> pttMapp);

    public List<Map<String,String>> getZYOUListByFile(MultipartFile file,Map<String, Ptt> pttMapp);

    public List<Map<String,String>> getMSCListByFile(MultipartFile file,Map<String, Ptt> pttMapp);
}
