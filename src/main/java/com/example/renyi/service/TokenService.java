package com.example.renyi.service;

public interface TokenService {

    public String refreshToken();


    //处理仁肄的  零售单和销货单上 考核成本没有反写成功的情况
    public void updateRenyiOrder();
}
