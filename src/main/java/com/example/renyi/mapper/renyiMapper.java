package com.example.renyi.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface renyiMapper {

    void updateRetailDetails();

    void updateSadetails();

    Float getDistricntKC(@Param("department") String department);
}
