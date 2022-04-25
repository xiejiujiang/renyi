package com.example.renyi.service;

import com.example.renyi.HQorderBack.sa.JsonRootBean;

public interface HQservice {

    //根据 红旗 回调的内容，生成T+ 的销货单 并 审核
    public String createTSaOrderByHQ(JsonRootBean jrb,com.example.renyi.saentity.JsonRootBean sajrb);
}
