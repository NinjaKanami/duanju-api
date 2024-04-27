package com.sqx.modules.orders.controller;

import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(value = "订单信息", tags = {"订单信息"})
@RequestMapping(value = "/order")
public class OrdersController extends AbstractController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;

    @GetMapping("/selectOrders")
    @ApiOperation("订单信息列表")
    public Result selectOrders(Integer page,Integer limit,String ordersNo,Integer status,Long userId,
                               Long courseId,Integer flag,String time,String userName,Integer ordersType,
                               String startTime,String endTime,Long sysUserId,String qdCode,String sysUserName){
        return ordersService.selectOrders(page,limit,ordersNo,status,userId,courseId,flag,time,userName,ordersType,
                startTime,endTime,sysUserId,qdCode,sysUserName);
    }

    @GetMapping("/deleteOrders")
    @ApiOperation("删除订单")
    public Result deleteOrders(String ids){
        return ordersService.deleteOrders(ids);
    }

    @GetMapping("/selectOrderByUserId")
    @ApiOperation("我的订单")
    public Result selectOrderByUserId(Integer page, Integer limit, Long userId) {
        return ordersService.selectOrderByUserId(page, limit, userId);
    }

    @GetMapping("/selectOrdersMoneyList")
    @ApiOperation("订单收入分析")
    public Result selectOrdersMoneyList(Integer page,Integer limit,Integer flag,String time,Integer ordersType){
        return ordersService.selectOrdersMoneyList(page, limit, flag, time,ordersType);
    }

    @PostMapping("/refundOrders")
    @ApiOperation("退款订单")
    public Result refundOrders(Long ordersId,String refundContent){
        return ordersService.refundOrder(ordersId,refundContent);
    }


    @GetMapping("/selectOrdersCount")
    @ApiOperation("订单统计")
    public Result selectOrdersCount(Integer flag,String time,Long sysUserId){
        //短剧订单 总  待  完  退
        Integer sumCourseOrdersCount = ordersService.selectOrdersCount(null, 1, flag, time,sysUserId);
        Integer daiCourseKeOrdersCount = ordersService.selectOrdersCount(0, 1, flag, time,sysUserId);
        Integer wanCourseKeOrdersCount = ordersService.selectOrdersCount(1, 1, flag, time,sysUserId);
        Integer tuiCourseOrdersCount = ordersService.selectOrdersCount(2, 1, flag, time,sysUserId);
        //短剧钱
        Double sumCourseOrdersMoney = ordersService.selectOrdersMoney(null, 1, flag, time,null,sysUserId);
        Double daiCourseOrdersMoney = ordersService.selectOrdersMoney(0, 1, flag, time,null,sysUserId);
        Double wanCourseOrdersMoney = ordersService.selectOrdersMoney(1, 1, flag, time,null,sysUserId);
        Double tuiCourseOrdersMoney = ordersService.selectOrdersMoney(2, 1, flag, time,null,sysUserId);
        //会员订单 总  待  完  退
        Integer sumMemberOrdersCount = ordersService.selectOrdersCount(null, 2, flag, time,sysUserId);
        Integer daiMemberKeOrdersCount = ordersService.selectOrdersCount(0, 2, flag, time,sysUserId);
        Integer wanMemberKeOrdersCount = ordersService.selectOrdersCount(1, 2, flag, time,sysUserId);
        Integer tuiMemberOrdersCount = ordersService.selectOrdersCount(2, 2, flag, time,sysUserId);
        //会员钱
        Double sumMemberOrdersMoney = ordersService.selectOrdersMoney(null, 2, flag, time,null,sysUserId);
        Double daiMemberOrdersMoney = ordersService.selectOrdersMoney(0, 2, flag, time,null,sysUserId);
        Double wanMemberOrdersMoney = ordersService.selectOrdersMoney(1, 2, flag, time,null,sysUserId);
        Double tuiMemberOrdersMoney = ordersService.selectOrdersMoney(2, 2, flag, time,null,sysUserId);
        Map<String,Object> result=new HashMap<>();
        result.put("sumCourseOrdersCount",sumCourseOrdersCount);result.put("daiCourseKeOrdersCount",daiCourseKeOrdersCount);
        result.put("wanCourseKeOrdersCount",wanCourseKeOrdersCount);result.put("tuiCourseOrdersCount",tuiCourseOrdersCount);
        result.put("sumCourseOrdersMoney",sumCourseOrdersMoney);result.put("daiCourseOrdersMoney",daiCourseOrdersMoney);
        result.put("wanCourseOrdersMoney",wanCourseOrdersMoney);result.put("tuiCourseOrdersMoney",tuiCourseOrdersMoney);
        result.put("sumMemberOrdersCount",sumMemberOrdersCount);result.put("daiMemberKeOrdersCount",daiMemberKeOrdersCount);
        result.put("wanMemberKeOrdersCount",wanMemberKeOrdersCount);result.put("tuiMemberOrdersCount",tuiMemberOrdersCount);
        result.put("sumMemberOrdersMoney",sumMemberOrdersMoney);result.put("daiMemberOrdersMoney",daiMemberOrdersMoney);
        result.put("wanMemberOrdersMoney",wanMemberOrdersMoney);result.put("tuiMemberOrdersMoney",tuiMemberOrdersMoney);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectCourseOrdersMoneyCount")
    @ApiOperation("统计具体剧的收益")
    public Result selectCourseOrdersMoneyCount(Long courseId,Long sysUserId){
        //总收益
        Double sumMoney = ordersService.selectOrdersMoney(1, 1, null, null,courseId,sysUserId);
        String dateTime = DateUtils.format(new Date());
        //年
        Double yearMoney = ordersService.selectOrdersMoney(1, 1, 3, dateTime,courseId,sysUserId);
        //月
        Double monthMoney = ordersService.selectOrdersMoney(1, 1, 2, dateTime,courseId,sysUserId);
        //日
        Double dayMoney = ordersService.selectOrdersMoney(1, 1, 1, dateTime,courseId,sysUserId);
        Map<String,Double> result=new HashMap<>();
        result.put("sumMoney",sumMoney);
        result.put("yearMoney",yearMoney);
        result.put("monthMoney",monthMoney);
        result.put("dayMoney",dayMoney);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectFenXiaoMoney")
    @ApiOperation("统计分销点券")
    public Result selectFenXiaoMoney(Long sysUserId,Integer flag,String time){
        //一级
        Double oneMoney = ordersService.selectFenXiaoMoney(1, sysUserId, flag, time);
        //二级
        Double twoMoney = ordersService.selectFenXiaoMoney(2, sysUserId, flag, time);
        //渠道
        Double qdMoney = ordersService.selectFenXiaoMoney(3, sysUserId, flag, time);
        //总分销
        Double sumMoney = ordersService.selectFenXiaoMoney(4, sysUserId, flag, time);
        Map<String,Object> result=new HashMap<>();
        result.put("oneMoney",oneMoney);
        result.put("twoMoney",twoMoney);
        result.put("qdMoney",qdMoney);
        result.put("sumMoney",sumMoney);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectOrdersCountStatisticsByYear")
    @ApiOperation("订单数量统计")
    public Result selectOrdersCountStatisticsByYear(String startTime,String endTime){
        //总数量 0待支付 1已支付 2已退款
        List<Integer> ordersCountList=new ArrayList<>();
        List<Integer> ordersDaiFuKuanCountList=new ArrayList<>();
        List<Integer> ordersYiZhiFuCountList=new ArrayList<>();
        List<Integer> ordersYiTuiKuanLunCountList=new ArrayList<>();
        List<String> year=new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(parse);
        while (true){
            String dateTime = simpleDateFormat.format(calendar.getTime());
            //状态 0待支付 1已支付 2已退款
            //总订单数
            Integer ordersCount = ordersService.selectOrdersCountStatisticsByYear(1, dateTime, null);
            ordersCountList.add(ordersCount);
            //0待支付
            Integer ordersDaiFuKuanCount = ordersService.selectOrdersCountStatisticsByYear(1, dateTime, 0);
            ordersDaiFuKuanCountList.add(ordersDaiFuKuanCount);
            //1已支付
            Integer ordersJinXinCount = ordersService.selectOrdersCountStatisticsByYear(1, dateTime, 1);
            ordersYiZhiFuCountList.add(ordersJinXinCount);
            //2已退款
            Integer ordersQuXiaoCount = ordersService.selectOrdersCountStatisticsByYear(1, dateTime, 2);
            ordersYiTuiKuanLunCountList.add(ordersQuXiaoCount);
            year.add(dateTime);
            if(dateTime.equals(endTime)){
                break;
            }
            calendar.add(Calendar.DATE,1);
        }
        Map<String,Object> result=new HashMap<>();
        result.put("ordersCountList",ordersCountList);
        result.put("ordersDaiFuKuanCountList",ordersDaiFuKuanCountList);
        result.put("ordersYiZhiFuCountList",ordersYiZhiFuCountList);
        result.put("ordersYiTuiKuanLunCountList",ordersYiTuiKuanLunCountList);
        result.put("year",year);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectAgencyMoneyByTime")
    @ApiOperation("获取达人推广达人收益统计")
    public Result selectAgencyMoneyByTime(Long userId, Integer type){
        UserEntity userEntity = userService.getById(userId);
        return ordersService.selectAgencyMoneyByTime(userEntity,type);
    }

    @GetMapping("/selectCourseMoneyByTime")
    @ApiOperation("获取达人推广短剧收益统计")
    public Result selectCourseMoneyByTime(Long userId, Integer type){
        UserEntity userEntity = userService.getById(userId);
        return ordersService.selectCourseMoneyByTime(userEntity,type);
    }

    @GetMapping("/selectChannelMoneyByTime")
    @ApiOperation("获取剧荐管推广短剧收益统计")
    public Result selectChannelMoneyByTime(Long userId, Integer type){
        UserEntity userEntity = userService.getById(userId);
        return ordersService.selectChannelMoneyByTime(userEntity,type);
    }


    @GetMapping("/selectChannelMoneyByDatetime")
    @ApiOperation("获取剧荐管推广短剧收益统计")
    public Result selectChannelMoneyByDatetime(Long userId,String time){
        UserEntity userEntity = userService.getById(userId);
        return ordersService.selectChannelMoneyByDatetime(userEntity,time);
    }

}
