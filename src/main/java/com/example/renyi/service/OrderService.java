package com.example.renyi.service;

import java.util.Map;

public interface OrderService {

    String getTnameByCode(String code);

    Map<String,Object> getDistricntKC(String department, String thistotal,String code);

    Map<String,Object> getClerkCustomer(String idclerk, String idcustomer);
}
