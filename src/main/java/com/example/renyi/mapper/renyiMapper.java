package com.example.renyi.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface renyiMapper {

    void updateRetailDetails();

    void updateSadetails();

    Float getCDDistricntKC(@Param("department") String department);

    Float getCDDistricntPUorder(@Param("department") String department);

    Float getDistricntKC(@Param("department") String department);

    Float getDistricntPUorder(@Param("department") String department);
}
