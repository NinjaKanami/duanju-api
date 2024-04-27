package com.sqx.modules.orders.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.orders.entity.Orders;

public interface OrdersService extends IService<Orders> {
    /**
     * 处理所有订单
     *
     * @param orders
     * @return
     */
    Result insertOrders(Orders orders);

    Result payMoney(Long orderId);

    Result payOrdersInviteMoney(Long ordersId);

    Result insertCourseOrders(Long courseId, Long courseDetailsId,Long userId);

    Result insertVipOrders(Long vipDetailsId, Long userId);

    Result refundOrder(Long ordersId, String refundContent);

    Result selectOrders(Integer page, Integer limit, String ordersNo,Integer status,Long userId,Long courseId,
                        Integer flag,String time,String userName,Integer ordersType,String startTime,String endTime,
                        Long sysUserId,String qdCode,String sysUserName);

    Result selectOrderByUserId(Integer page, Integer limit, Long userId);

    Result selectAllOrderByUserId(Integer page, Integer limit, Long userId);

    Result deleteOrders(String ids);

    Orders selectOrderById(Long orderId);

    Orders selectOrderByOrdersNo(String ordersNo);

    Double statisticsIncomeMoney(String time,Integer flag,Integer ordersType);

    Double statisticsPingMoney(String time, Integer flag, Integer ordersType);

    Orders selectOrdersByCourseIdAndUserId(Long userId,Long courseId);

    Result selectOrdersMoneyList(Integer page,Integer limit,Integer flag,String time,Integer ordersType);

    Integer selectOrdersCount(Integer status,Integer ordersType,Integer flag,String time,Long sysUserId);

    Double selectOrdersMoney(Integer status,Integer ordersType,Integer flag,String time,Long courseId,Long sysUserId);

    Double selectFenXiaoMoney(Integer type,Long sysUserId,Integer flag,String time);

    Integer selectOrdersCountStatisticsByYear(Integer flag,String time,Integer status);

    Result selectAgencyMoneyByTime(UserEntity userEntity, Integer type);

    Result selectCourseMoneyByTime(UserEntity userEntity, Integer type);

    Result selectChannelMoneyByTime(UserEntity userEntity, Integer type);

    Result selectChannelMoneyByDatetime(UserEntity userEntity, String time);

}
