package com.example.renyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface orderMapper {

    public String getTnameByCode(String code);
}
