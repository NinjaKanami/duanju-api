package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.dao.UserMoneyDetailsDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserMoneyDetailsServiceImpl extends ServiceImpl<UserMoneyDetailsDao, UserMoneyDetails> implements UserMoneyDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public Result queryUserMoneyDetails(Integer page, Integer limit,Long sysUserId,Long userId,Integer classify,Integer type) {
        IPage<UserMoneyDetails> page1 = new Page(page, limit);
        QueryWrapper<UserMoneyDetails> queryWrapper = new QueryWrapper();
        if(sysUserId!=null){
            queryWrapper.eq("sys_user_id", sysUserId);
        }
        if(userId!=null){
            queryWrapper.eq("user_id", userId);
        }
        if(classify!=null){
            queryWrapper.eq("classify", classify);
        }
        if(type!=null){
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByDesc("create_time");
        return Result.success().put("data", baseMapper.selectPage(page1, queryWrapper));
    }

    @Override
    public Result selectUserMoneyDetails(Integer page, Integer limit,Long userId) {
        IPage<UserMoneyDetails> page1 = new Page(page, limit);
        QueryWrapper<UserMoneyDetails> queryWrapper = new QueryWrapper();
        queryWrapper.in("classify", 10,20,21,22,30,31,40);
        queryWrapper.eq("user_id",userId);
        queryWrapper.orderByDesc("create_time");
        return Result.success().put("data", baseMapper.selectPage(page1, queryWrapper));
    }

    @Override
    public Double monthIncome(String date, Long userId) {
        return baseMapper.monthIncome(date,userId);
    }

    @Override
    public Double selectSumMoneyByClassifyAndUserId(String time,Integer classify,Long userId) {
        return baseMapper.selectSumMoneyByClassifyAndUserId(time, classify, userId);
    }

    @Override
    public Integer selectCountByClassifyAndUserId(String time,Integer classify,Long userId) {
        return baseMapper.selectCountByClassifyAndUserId(time, classify, userId);
    }

    @Override
    public List<Map<String,String>> selectSumMoneyByClassifyAndUserIdTime(String startTime, String endTime, Integer classify, Long userId) {
        return baseMapper.selectSumMoneyByClassifyAndUserIdTime(startTime,endTime, classify, userId);
    }

    @Override
    public Double selectSumMoneyByClassifyAndUserIdTimes(String startTime, String endTime, Integer classify, Long userId) {
        return baseMapper.selectSumMoneyByClassifyAndUserIdTimes(startTime,endTime, classify, userId);
    }


    @Override
    public Integer selectCountByClassifyAndUserIdTime(String startTime,String endTime,Integer classify,Long userId) {
        return baseMapper.selectCountByClassifyAndUserIdTime(startTime,endTime, classify, userId);
    }

    @Override
    public Result selectMoneyDetailsByTime(Integer page, Integer limit, String time, @LoginUser UserEntity userEntity, Integer classify){
        if(classify==1){
            if(userEntity.getAgencyIndex()==1){
                classify=10;
            }else{
                classify=20;
            }
        }else{
            classify=21;
        }
        return Result.success().put("data",baseMapper.selectMoneyDetailsByTime(new Page<>(page,limit),time,userEntity.getUserId(),classify));
    }

    @Override
    public Result selectChannelMoney(UserEntity userEntity){
        //获取本月的第一天
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startTime = DateUtils.format(calendar.getTime());
        //获取本月的最后一天
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); // 下个月的第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为下个月的第一天
        calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
        String endTime = DateUtils.format(calendar.getTime());
        //本月分红
        Double sumMoney = baseMapper.selectSumMoneyByClassifyAndUserIdTimes(startTime, endTime, 31, userEntity.getUserId());
        //本月短剧分红
        Double courseMoney = baseMapper.selectSumMoneyByClassifyAndUserIdTimes(startTime, endTime, 30, userEntity.getUserId());
        //总达人数
        int userCount = userDao.selectAgencyIndexCountByQdCodeByTime(userEntity.getChannelCode(), null, null, 1);
        //剧达人数
        int twoUserCount = userDao.selectAgencyIndexCountByQdCodeByTime(userEntity.getChannelCode(), null, null, 3);
        //梵会员数
        int oneUserCount = userDao.selectAgencyIndexCountByQdCodeByTime(userEntity.getChannelCode(), null, null, 2);
        Map<String,Object> result=new HashMap<>();
        result.put("sumMoney",sumMoney);
        result.put("courseMoney",courseMoney);
        result.put("userCount",userCount);
        result.put("twoUserCount",twoUserCount);
        result.put("oneUserCount",oneUserCount);
        return Result.success().put("data",result);
    }

    @Override
    public Double selectSumMoneyByUserIdAndTime(Long userId,Integer type,String time){
        return baseMapper.selectSumMoneyByUserIdAndTime(userId, type, time);
    }

    @Override
    public Double selectSumMoneyByTime(Integer type,String time){
        return baseMapper.selectSumMoneyByTime(type, time);
    }


    @Override
    public Result selectEarningsMoneyDetailsByTime(Integer page,Integer limit, Integer type,String time,Integer classify){
        return Result.success().put("data",new PageUtils(baseMapper.selectEarningsMoneyDetailsByTime(new Page<>(page,limit),type,time,classify)));
    }

    @Override
    public Result selectEarningsMoneyDetailsByTimeAndUserId(Integer page,Integer limit, Integer type,String time,Integer classify,Long userId,Integer timeType){
        String startTime=null;
        String endTime=null;
        if(timeType!=null){
            if(timeType==1){
                startTime = DateUtils.format(new Date(),DateUtils.DATE_PATTERN);
                endTime = DateUtils.format(new Date(),DateUtils.DATE_PATTERN);
            }else if(timeType==2){
                Calendar calendar=Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH,-1);
                startTime = DateUtils.format(calendar.getTime(),DateUtils.DATE_PATTERN);
                endTime = DateUtils.format(calendar.getTime(),DateUtils.DATE_PATTERN);
            }else if(timeType==3){
                Calendar calendar=Calendar.getInstance();
                endTime = DateUtils.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH,-6);
                startTime = DateUtils.format(calendar.getTime());
            }else if(timeType==4){
                Calendar calendar=Calendar.getInstance();
                endTime = DateUtils.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH,-30);
                startTime = DateUtils.format(calendar.getTime());
            }else if(timeType==5){
                //获取本月的第一天
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startTime = DateUtils.format(calendar.getTime());
                //获取本月的最后一天
                calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); // 下个月的第一天
                calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为下个月的第一天
                calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
                endTime = DateUtils.format(calendar.getTime());
            }else if(timeType==6){
                //获取本月的第一天
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 上个月的第一天
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startTime = DateUtils.format(calendar.getTime());
                //获取本月的最后一天
                calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为本个月的第一天
                calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
                endTime = DateUtils.format(calendar.getTime());
            }
        }
        return Result.success().put("data",new PageUtils(baseMapper.selectEarningsMoneyDetailsByTimeAndUserId(new Page<>(page,limit),type,time,
                classify,userId,startTime,endTime)));
    }



}
