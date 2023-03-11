package com.example.renyi.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface renyiMapper {

    void updateRetailDetails();

    void updateSadetails();

    Float getCDDistricntKC(@Param("department") String department);

    Float getCDDistricntPUorder(@Param("department") String department);

    Float getDistricntKC(@Param("department") String department);

    Float getDistricntKCNOmsc(@Param("department") String department);

    Float getDistricntPUorder(@Param("department") String department);

    Float getDistricntPUorderNOmsc(@Param("department") String department);

    void updateFKSQdesc(@Param("code") String code);

    List<Map<String,Object>> getRetailDataListByCode(@Param("code")String code);

    List<Map<String,Object>> getRetailSettleListByCode(@Param("code")String code);

    void updateReretailAStateByCode(@Param("code")String code);

    String getClerkcreditQuantity(@Param("idclerk")String idclerk);
    String getClerkaRBalance(@Param("idclerk")String idclerk);
    String getClerkaCxje(@Param("idclerk")String idclerk);


    Map<String, Object> getCustomerSaleDetails(@Param("idcustomer")String idcustomer);

    String getCustomerSaleEarliest(@Param("idcustomer")String idcustomer);
}
