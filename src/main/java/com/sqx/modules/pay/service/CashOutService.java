package com.sqx.modules.pay.service;


import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.pay.entity.CashOut;
import com.sqx.modules.utils.excel.ExcelData;

import java.util.Date;
import java.util.List;

public interface CashOutService {

    PageUtils selectCashOutList(Integer page,Integer limit,CashOut cashOut);

    ExcelData excelPayDetails(CashOut cashOut);

    int saveBody(CashOut cashOut);

    int update(CashOut cashOut);

    CashOut selectById(Long id);

    void cashOutSuccess(String openId, String date, String money, String payWay, String url);

    List<CashOut> selectCashOutLimit3();

    void refundSuccess(UserEntity userByWxId, String date, String money, String url, String content);

    Double sumMoney(String time, Integer flag);

    Integer countMoney(String time, Integer flag);

    Integer stayMoney(String time, Integer flag);

    void updateMayMoney(int i, Long userId, Double money);

    Result cashMoney(Long userId, Double money,Integer classify);

    Result sysCashMoney(Long userId, Double money);

}
