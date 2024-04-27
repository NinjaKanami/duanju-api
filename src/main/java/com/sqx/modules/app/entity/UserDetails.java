package com.sqx.modules.app.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDetails {
    /**
     * 本月订单数量
      */
    private int monthlyOrderNum;
    /**
     * 本月充值点券
     */
    private BigDecimal monthlyRechargeMoney;
    /**
     *本月提现数量
     */
    private int monthWithdrawalNum;
    /**
     * 本月提现点券
     */
    private  BigDecimal monthlyWithdrawalMoney;


}
