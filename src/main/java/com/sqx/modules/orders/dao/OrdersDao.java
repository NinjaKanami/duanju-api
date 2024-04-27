package com.sqx.modules.orders.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.orders.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrdersDao extends BaseMapper<Orders> {

    String selectMaxCode(String newData);

    int insertOrders(Orders orders);

    IPage<Orders> selectOrdersByOrdersNo(Page<Orders> pages, @Param("ordersNo") String ordersNo,@Param("status") Integer status,
                                         @Param("userId") Long userId,@Param("courseId") Long courseId,@Param("flag") Integer flag,
                                         @Param("time") String time, @Param("userName") String userName, @Param("ordersType") Integer ordersType,
                                         @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("sysUserId") Long sysUserId,
                                         @Param("qdCode") String qdCode,@Param("sysUserName") String sysUserName);

    int deleteOrders(String[] ids);

    Double statisticsIncomeMoney(@Param("time") String time, @Param("flag") Integer flag, @Param("ordersType") Integer ordersType);

    Double statisticsPingMoney(@Param("time") String time, @Param("flag") Integer flag, @Param("ordersType") Integer ordersType);

    Orders selectOrdersByCourseIdAndUserId(Long userId,Long courseId);

    IPage<Orders> selectOrdersMoneyList(Page<Orders> page,Integer flag,String time,Integer ordersType);

    Integer selectOrdersCount(Integer status,Integer ordersType,Integer flag,String time,Long sysUserId);

    Double selectOrdersMoney(Integer status,Integer ordersType,Integer flag,String time,Long courseId,Long sysUserId);

    Double selectFenXiaoMoney(Integer type,Long sysUserId,Integer flag,String time);

    Double selectOrdersPingMoneyByMonth(String time);

    Double selectOrdersAgencyMoneyByTime(String time,String qdCode);

    Double selectOrdersAgencyMoneyByTimes(String startTime,String endTime,String qdCode);

    Integer selectCountOrdersAgencyMoneyByTime(String time,String qdCode);

    Integer selectCountOrdersAgencyMoneyByTimes(String startTime,String endTime,String qdCode);

    Integer selectOrdersCountStatisticsByYear(Integer flag,String time,Integer status);


}
