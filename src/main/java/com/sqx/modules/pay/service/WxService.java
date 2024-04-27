package com.sqx.modules.pay.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.orders.entity.Orders;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface WxService {

    Result payOrder(Long orderId, Integer type) throws Exception;

    Result payMoney(Long payClassifyId, Long userId, Integer classify) throws Exception;

    Result payMoneyOrders(Long payClassifyId,Long userId, Integer classify) throws Exception;

    Result selectSign(String signData,String sessionKey)  throws Exception;

    String notifyXPay(String textContent);

    String payBack(String resXml,Integer type);

    boolean refund(Orders orders);

}