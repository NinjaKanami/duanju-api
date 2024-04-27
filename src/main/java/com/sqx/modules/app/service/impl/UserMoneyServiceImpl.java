package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.app.dao.UserMoneyDao;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.service.UserMoneyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserMoneyServiceImpl extends ServiceImpl<UserMoneyDao, UserMoney> implements UserMoneyService {

    @Override
    public void updateMoney(int i, Long userId, double money){
        baseMapper.updateMayMoney(i,userId,money);
    }

    @Override
    public void updateSysMoney(int i, Long userId, double money){
        selectSysUserMoneyByUserId(userId);
        baseMapper.updateSysMoney(i,userId,money);
    }

    @Override
    public UserMoney selectUserMoneyByUserId(Long userId){
        UserMoney userMoney = baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
        if(userMoney==null){
            userMoney=new UserMoney();
            userMoney.setUserId(userId);
            userMoney.setMoney(new BigDecimal("0.00"));
            baseMapper.insert(userMoney);
        }
        return userMoney;
    }

    @Override
    public UserMoney selectSysUserMoneyByUserId(Long userId){
        UserMoney userMoney = baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("sys_user_id", userId));
        if(userMoney==null){
            userMoney=new UserMoney();
            userMoney.setSysUserId(userId);
            userMoney.setMoney(new BigDecimal("0.00"));
            baseMapper.insert(userMoney);
        }
        return userMoney;
    }


}
