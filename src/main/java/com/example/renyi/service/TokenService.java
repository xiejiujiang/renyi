package com.example.renyi.service;

import java.util.List;

public interface TokenService {

    public String refreshToken();

    //处理仁肄的  零售单和销货单上 考核成本没有反写成功的情况
    public void updateRenyiOrder();

    //获取 转换 结果
    String getRetailToSaleResult(List<String> codelist);
}
