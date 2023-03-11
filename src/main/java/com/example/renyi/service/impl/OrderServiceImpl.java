package com.example.renyi.service.impl;

import com.example.renyi.mapper.renyiMapper;
import com.example.renyi.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private renyiMapper renyiMapper;

    @Override
    public String getTnameByCode(String code) {
        return "";
    }

    @Override
    public Map<String,Object> getDistricntKC(String department, String thistotal,String code) {
        Map<String,Object> result = new HashMap<String,Object>();
        if(thistotal == null || "".equals(thistotal)){
            thistotal = "0";
        }
        Float thisnow = Float.valueOf(thistotal);
        //根据部门判断不同的 地区 SQL
        if(department.contains("成都")){ //除了车业务和全屋的商品。剩下的不超过最高限值 1550万  包含MSC 的 200万了哦
            Float now = renyiMapper.getCDDistricntKC("%成都%"); //当前库存结存金额
            Float ddnow = renyiMapper.getCDDistricntPUorder("%成都%");//采购订单未执行完的总金额
            LOGGER.error("------------ 成都 现在的库存金额是："+now+ " ------------");
            LOGGER.error("------------ 成都 现在的采购订单未入库金额是："+ddnow+ " ------------");
            if((now+ddnow) > 16500000f || (now+ddnow+thisnow) > 16500000f) {
                result.put("data","0");
                result.put("msg","当前库存金额"+(now+ddnow)+"元，库存金额上限1650万，禁止下单，请联系王洪田");//当前库存金额*元，库存金额上限*万，禁止下单！
                LOGGER.error("------------ 马上开始更新备注了！ -----------" + code);
                if(!"xjj".equals(code) && !"".equals(code)){//说明是付款申请单
                    //更新付款申请单的备注，追加 “超额” 二字
                    LOGGER.error("------------ 1111111111111111111111111 -----------" + code);
                    renyiMapper.updateFKSQdesc(code);
                    LOGGER.error("------------ 2222222222222222222222222 -----------" + code);
                }
            }else{
                result.put("data","1");
            }
        }
        if(department.contains("绵阳")){ //除了车业务和全屋的商品。剩下的不超过最高限值 500万
            Float now = renyiMapper.getDistricntKC("%绵阳%");
            Float ddnow = renyiMapper.getDistricntPUorder("%绵阳%");//采购订单未执行完的总金额
            LOGGER.error("------------ 绵阳 现在的库存金额是："+now+ " ------------");
            LOGGER.error("------------ 绵阳 现在的采购订单未入库金额是："+ddnow+ " ------------");
            if((now+ddnow) > 4000000f || (now+ddnow+thisnow) > 4000000f) {
                result.put("data","0");
                result.put("msg","当前库存金额"+(now+ddnow)+"元，库存金额上限400万，禁止下单，请联系王洪田");//当前库存金额*元，库存金额上限*万，禁止下单！
                if(!"xjj".equals(code) && !"".equals(code)){//说明是付款申请单
                    //更新付款申请单的备注，追加 “超额” 二字
                    renyiMapper.updateFKSQdesc(code);
                }
            }else{
                result.put("data","1");
            }
        }
        if(department.contains("昆明")){ //除了车业务和全屋的商品。剩下的不超过最高限值 1650万
            Float now = renyiMapper.getDistricntKC("%昆明%");
            Float ddnow = renyiMapper.getDistricntPUorder("%昆明%");//采购订单未执行完的总金额
            LOGGER.error("------------ 昆明 现在的库存金额是："+now+ " ------------");
            LOGGER.error("------------ 昆明 现在的采购订单未入库金额是："+ddnow+ " ------------");
            if((now+ddnow) > 16000000f || (now+ddnow+thisnow) > 16000000f) {
                result.put("data","0");
                result.put("msg","当前库存金额"+(now+ddnow)+"元，库存金额上限1600万，禁止下单，请联系王洪田");//当前库存金额*元，库存金额上限*万，禁止下单！
                if(!"xjj".equals(code) && !"".equals(code)){//说明是付款申请单
                    //更新付款申请单的备注，追加 “超额” 二字
                    renyiMapper.updateFKSQdesc(code);
                }
            }else{
                result.put("data","1");
            }
        }
        /*if(department.contains("锦华MSC")){ //除了车业务和全屋的商品。剩下的不超过最高限值 200万
            Float now = renyiMapper.getDistricntKC("%锦华MSC%");
            Float ddnow = renyiMapper.getDistricntPUorder("%锦华MSC%");//采购订单未执行完的总金额
            LOGGER.error("------------ 锦华MSC 现在的库存金额是："+now+ " ------------");
            LOGGER.error("------------ 锦华MSC 现在的采购订单未入库金额是："+ddnow+ " ------------");
            if((now+ddnow) > 2000000f || (now+ddnow+thisnow) > 2000000f) {
                result.put("data","0");
                result.put("msg","当前库存金额"+(now+ddnow)+"元，库存金额上限200万，禁止下单，请联系王洪田");//当前库存金额*元，库存金额上限*万，禁止下单！
                if(!"xjj".equals(code) && !"".equals(code)){//说明是付款申请单
                    //更新付款申请单的备注，追加 “超额” 二字
                    renyiMapper.updateFKSQdesc(code);
                }
            }else{
                result.put("data","1");
            }
        }*/
        if(department.contains("贵阳")){ //除了车业务和全屋的商品。剩下的不超过最高限值 300万
            Float now = renyiMapper.getDistricntKC("%贵阳%");
            Float ddnow = renyiMapper.getDistricntPUorder("%贵阳%");//采购订单未执行完的总金额
            LOGGER.error("------------ 贵阳 现在的库存金额是："+now+ " ------------");
            LOGGER.error("------------ 贵阳 现在的采购订单未入库金额是："+ddnow+ " ------------");
            if((now+ddnow) > 2000000f || (now+ddnow+thisnow) > 2000000f) {
                result.put("data","0");
                result.put("msg","当前库存金额"+(now+ddnow)+"元，库存金额上限200万，禁止下单，请联系王洪田");//当前库存金额*元，库存金额上限*万，禁止下单！
                if(!"xjj".equals(code) && !"".equals(code)){//说明是付款申请单
                    //更新付款申请单的备注，追加 “超额” 二字
                    renyiMapper.updateFKSQdesc(code);
                }
            }else{
                result.put("data","1");
            }
        }
        if(department.contains("上海") && !department.contains("MSC")){ //除了车业务和全屋的商品。剩下的不超过最高限值 800万
            Float now = renyiMapper.getDistricntKCNOmsc("%上海%");//不能包含MSC的
            Float ddnow = renyiMapper.getDistricntPUorderNOmsc("%上海%");//采购订单未执行完的总金额  不能包含MSC的
            LOGGER.error("------------ 上海 现在的库存金额是："+now+ " ------------");
            LOGGER.error("------------ 上海 现在的采购订单未入库金额是："+ddnow+ " ------------");
            if((now+ddnow) > 8000000f || (now+ddnow+thisnow) > 8000000f) {
                result.put("data","0");
                result.put("msg","当前库存金额"+(now+ddnow)+"元，库存金额上限800万，禁止下单，请联系王洪田");//当前库存金额*元，库存金额上限*万，禁止下单！
                if(!"xjj".equals(code) && !"".equals(code)){//说明是付款申请单
                    //更新付款申请单的备注，追加 “超额” 二字
                    renyiMapper.updateFKSQdesc(code);
                }
            }else{
                result.put("data","1");
            }
        }
        if(department.contains("上海") && department.contains("MSC")){ //500W
            Float now = renyiMapper.getDistricntKC("%奉贤%");//库存对于的金额,如果后面新增了，就来这里新增、
            Float ddnow = renyiMapper.getDistricntPUorder("%奉贤%");//采购订单未执行完的总金额
            LOGGER.error("------------ 上海MSC 现在的库存金额是："+now+ " ------------");
            LOGGER.error("------------ 上海MSC 现在的采购订单未入库金额是："+ddnow+ " ------------");
            if((now+ddnow) > 5000000f || (now+ddnow+thisnow) > 5000000f) {
                result.put("data","0");
                result.put("msg","当前库存金额"+(now+ddnow)+"元，库存金额上限500万，禁止下单，请联系王洪田");//当前库存金额*元，库存金额上限*万，禁止下单！
                if(!"xjj".equals(code) && !"".equals(code)){//说明是付款申请单
                    //更新付款申请单的备注，追加 “超额” 二字
                    renyiMapper.updateFKSQdesc(code);
                }
            }else{
                result.put("data","1");
            }
        }
        if(department.contains("苏州")){ //除了车业务和全屋的商品。剩下的不超过最高限值 500万
            Float now = renyiMapper.getDistricntKC("%苏州%");
            Float ddnow = renyiMapper.getDistricntPUorder("%苏州%");//采购订单未执行完的总金额
            LOGGER.error("------------ 苏州 现在的库存金额是："+now+ " ------------");
            LOGGER.error("------------ 苏州 现在的采购订单未入库金额是："+ddnow+ " ------------");
            if((now+ddnow) > 6000000f || (now+ddnow+thisnow) > 6000000f) {
                result.put("data","0");
                result.put("msg","当前库存金额"+(now+ddnow)+"元，库存金额上限600万，禁止下单，请联系王洪田");//当前库存金额*元，库存金额上限*万，禁止下单！
                if(!"xjj".equals(code) && !"".equals(code)){//说明是付款申请单
                    //更新付款申请单的备注，追加 “超额” 二字
                    renyiMapper.updateFKSQdesc(code);
                }
            }else{
                result.put("data","1");
            }
        }
        return result;
    }


    //根据 销货单上的 业务员ID，往来单位，查询此业务员的 信用额度和应收余额， 以及 此客户的是否超账龄了。
    public Map<String, Object> getClerkCustomer(String idclerk, String idcustomer) {
        Map<String,Object> result = new HashMap<String,Object>();

        String creditQuantity = renyiMapper.getClerkcreditQuantity(idclerk);
        String aRBalance = renyiMapper.getClerkaRBalance(idclerk); // 之1 收款单未收的
        String cxje = renyiMapper.getClerkaCxje(idclerk);// 之2 的 通过 预收冲应收的 未收金额

        //获取客户档案中的账龄设置天数，以及应收余额，以及 最近一次的 销货单日期
        Map<String, Object> customerresult = renyiMapper.getCustomerSaleDetails(idcustomer);

        //此客户 最早 一次的 未结清的审核了的 销货单日期 据今 的 天数
        Long difday = 0L;
        String earliestDay = renyiMapper.getCustomerSaleEarliest(idcustomer);//yyyy-MM-dd HH:mm:ss
        try{
            if(earliestDay != null && !"".equals(earliestDay)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date saledate =  sdf.parse(earliestDay);
                Date today = new Date();
                Long timess = (today.getTime()-saledate.getTime())/86400000;
                difday = Math.abs(timess);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(Float.valueOf(customerresult.get("aRBalance").toString()) > 0
            && ((difday-10) > Float.valueOf(customerresult.get("saleCreditDays").toString()))){
            result.put("flag","0");// 不可以卖给 此 客户 !!
        }else{
            result.put("flag","1");//可以卖给 此 客户
        }
        result.put("creditQuantity",creditQuantity);//此员工 信用额度
        result.put("aRBalance",Float.valueOf(aRBalance)-Float.valueOf(cxje));//此员工 应收余额
        return result;
    }
}