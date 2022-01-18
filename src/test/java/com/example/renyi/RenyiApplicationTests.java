package com.example.renyi;

import ch.qos.logback.core.util.FileUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.example.renyi.entity.TData;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class RenyiApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void tt() throws  Exception{
        String filepath = "C:\\Users\\mexjj\\Desktop\\111\\tcgdd.xlsx";
        Sheet sheet = new Sheet(1,0, TData.class);
        List<TData> TDatalist = new ArrayList<TData>();
        for(int i=0;i<4;i++){
            TData tData = new TData();
            tData.setVoucherDate("2022-01-15");
            tData.setCode("xxxxx9999");
            tData.setPartner("putiantaili");
            tData.setPartnerName("putiantaili111");
            TDatalist.add(tData);
        }
        OutputStream out = new FileOutputStream(filepath);
        ExcelWriter writer = EasyExcelFactory.getWriter(out, ExcelTypeEnum.XLS, false);
        writer.write(TDatalist,sheet);
        writer.finish();
    }
}
