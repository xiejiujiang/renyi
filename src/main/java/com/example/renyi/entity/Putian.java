package com.example.renyi.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class Putian extends BaseRowModel {

    @ExcelProperty(index = 0)
    private String mdmc;

    @ExcelProperty(index = 1)
    private String pl;

    @ExcelProperty(index = 2)
    private String cpmc;

    @ExcelProperty(index = 3)
    private String pp;

    @ExcelProperty(index = 4)
    private String jx;

    @ExcelProperty(index = 5)
    private String ys;

    @ExcelProperty(index = 6)
    private String fgs;

    @ExcelProperty(index = 7)
    private String xm;

    @ExcelProperty(index = 8)
    private String spdj;

    @ExcelProperty(index = 9)
    private String fhsl;

    @ExcelProperty(index = 10)
    private String pc;

    public String getMdmc() {
        return mdmc;
    }

    public void setMdmc(String mdmc) {
        this.mdmc = mdmc;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getCpmc() {
        return cpmc;
    }

    public void setCpmc(String cpmc) {
        this.cpmc = cpmc;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getJx() {
        return jx;
    }

    public void setJx(String jx) {
        this.jx = jx;
    }

    public String getYs() {
        return ys;
    }

    public void setYs(String ys) {
        this.ys = ys;
    }

    public String getFgs() {
        return fgs;
    }

    public void setFgs(String fgs) {
        this.fgs = fgs;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getSpdj() {
        return spdj;
    }

    public void setSpdj(String spdj) {
        this.spdj = spdj;
    }

    public String getFhsl() {
        return fhsl;
    }

    public void setFhsl(String fhsl) {
        this.fhsl = fhsl;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    @Override
    public String toString() {
        return "putian{" +
                "mdmc='" + mdmc + '\'' +
                ", pl='" + pl + '\'' +
                ", cpmc='" + cpmc + '\'' +
                ", pp='" + pp + '\'' +
                ", jx='" + jx + '\'' +
                ", ys='" + ys + '\'' +
                ", fgs='" + fgs + '\'' +
                ", xm='" + xm + '\'' +
                ", spdj='" + spdj + '\'' +
                ", fhsl='" + fhsl + '\'' +
                ", pc='" + pc + '\'' +
                '}';
    }
}
