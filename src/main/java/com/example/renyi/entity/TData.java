package com.example.renyi.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class TData extends BaseRowModel {

    @ExcelProperty(index = 0)
    private String voucherDate;

    @ExcelProperty(index = 1)
    private String code;

    @ExcelProperty(index = 2)
    private String partner;

    @ExcelProperty(index = 3)
    private String partnerName;



}
