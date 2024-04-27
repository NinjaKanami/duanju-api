package com.sqx.modules.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.orders.entity.Orders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface DyService {

    Result payOrder(Long orderId) throws Exception;

    Result payMoney(Long payClassifyId, Long userId) throws Exception;

    Result payVirtualAppOrder(Long orderId) throws Exception;

    Result payVirtualAppOrder(Long payClassifyId,Long userId) throws Exception;

    String payBack(HttpServletRequest request, HttpServletResponse response);

    String virtualNotify(JSONObject jsonObject);

    boolean refund(Orders orders);

}