package com.sqx.modules.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.orders.entity.Orders;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface KsService {

    Result payOrder(Long orderId) throws Exception;

    Result payMoney(Long payClassifyId, Long userId) throws Exception;

    String payBack(String kwaisign,JSONObject jsonObject);

    boolean refund(Orders orders);

}