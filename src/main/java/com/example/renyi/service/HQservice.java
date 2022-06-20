package com.example.renyi.service;

import com.example.renyi.HQorderBack.sa.JsonRootBean;
import com.example.renyi.SAsubscribe.SACsubJsonRootBean;

public interface HQservice {

    //根据 红旗 回调的内容，生成T+ 的销货单 并 审核
    public String createTSaOrderByHQ(JsonRootBean jrb,com.example.renyi.saentity.JsonRootBean sajrb);


    //这个方法 是接受  T+ 的 消息订阅，判断是 销货单审核 后，处理 红旗相关的上传业务。
    public void dealHQservice(SACsubJsonRootBean jrb);
}
