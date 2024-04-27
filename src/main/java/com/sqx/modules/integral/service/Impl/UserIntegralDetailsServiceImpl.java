package com.sqx.modules.integral.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.integral.dao.UserIntegralDetailsDao;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.integral.service.UserIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserIntegralDetailsServiceImpl extends ServiceImpl<UserIntegralDetailsDao, UserIntegralDetails> implements UserIntegralDetailsService {

    @Autowired
    private UserIntegralDetailsDao userIntegralDetailsDao;
    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Override
    public IPage selectUserIntegralDetailsByUserId(int page, int limit, Long userId) {

        IPage<UserIntegralDetails> page1 = userIntegralDetailsDao.selectPage(new Page<>(page, limit), new QueryWrapper<UserIntegralDetails>().eq(userId != null, "user_id", userId).orderByDesc("create_time"));

        return page1;
    }


    @Override
    public Result signIn(Long userId){
        //先判断今天是否签过到
        UserIntegralDetails userIntegralDetails1 = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, new Date());
        if(userIntegralDetails1!=null){
            return Result.error("今天已经签到过了，请明天再来吧！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userIntegralService.selectById(userId);
        //每周初始积分
        CommonInfo one = commonInfoService.findOne(102);
        //累计签到叠加积分
        CommonInfo two = commonInfoService.findOne(103);
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        //获取当前日期时第几天  第一天则重新开始计时
        int num=0;
        int day=1;
        UserIntegralDetails userIntegralDetails2 = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, cal.getTime());
        if(userIntegralDetails2==null){
            num=Integer.parseInt(one.getValue());
        }else{
            if(userIntegralDetails2.getDay()==7){
                num=Integer.parseInt(one.getValue());
            }else{
                num=userIntegralDetails2.getNum()+Integer.parseInt(two.getValue());
                day=userIntegralDetails2.getDay()+1;
            }

        }
        userIntegralService.updateIntegral(1,userId,num);
        UserIntegralDetails userIntegralDetails=new UserIntegralDetails();
        userIntegralDetails.setClassify(1);
        userIntegralDetails.setContent("签到获得:"+num+"积分");
        userIntegralDetails.setCreateTime(sdf.format(new Date()));
        userIntegralDetails.setNum(num);
        userIntegralDetails.setType(1);
        userIntegralDetails.setUserId(userId);
        userIntegralDetails.setDay(day);
        userIntegralDetailsDao.insert(userIntegralDetails);
        return Result.success("签到成功，获得"+num+"积分");
    }

    @Override
    public Result selectIntegralDay(Long userId){
        Calendar cal=Calendar.getInstance();
        UserIntegralDetails nowIntegral = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        UserIntegralDetails yesterdayIntegral = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, cal.getTime());
        Map<String,Object> result=new HashMap<>();
        result.put("nowIntegral",nowIntegral);
        result.put("yesterdayIntegral",yesterdayIntegral);
        return Result.success().put("data",result);
    }

    @Override
    public Result creditsExchange(Long userId,Integer integral){
        UserIntegral userIntegral = userIntegralService.selectById(userId);
        CommonInfo one = commonInfoService.findOne(104);
        if(userIntegral.getIntegralNum()>=integral){
            BigDecimal money = BigDecimal.valueOf(integral).divide(new BigDecimal(one.getValue())).setScale(2,BigDecimal.ROUND_DOWN);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userIntegralService.updateIntegral(2,userId,integral);
            UserIntegralDetails userIntegralDetails=new UserIntegralDetails();
            userIntegralDetails.setClassify(0);
            userIntegralDetails.setContent("积分兑换点券，消耗积分："+integral+",兑换点券："+money);
            userIntegralDetails.setCreateTime(sdf.format(new Date()));
            userIntegralDetails.setNum(integral);
            userIntegralDetails.setType(2);
            userIntegralDetails.setUserId(userId);
            userIntegralDetailsDao.insert(userIntegralDetails);
            double v = Double.parseDouble(String.valueOf(money));
            userMoneyService.updateMoney(1,userId,v);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setTitle("[积分]积分兑换");
            userMoneyDetails.setContent("增加点券:"+money);
            userMoneyDetails.setType(1);
            userMoneyDetails.setClassify(1);
            userMoneyDetails.setMoney(BigDecimal.valueOf(v));
            userMoneyDetails.setCreateTime(sdf.format(new Date()));
            userMoneyDetailsService.save(userMoneyDetails);
            return Result.success("积分兑换成功！");
        }else{
            return Result.error("积分数量不足！");
        }
    }

}