package com.sqx.modules.integral.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.integral.dao.UserIntegralDao;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.integral.service.UserIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class UserIntegralServiceImpl extends ServiceImpl<UserIntegralDao, UserIntegral> implements UserIntegralService {

    @Autowired
    private UserIntegralDao userIntegralDao;


    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();


    @Override
    public UserIntegral selectById(Long id) {
        UserIntegral userIntegral = userIntegralDao.selectById(id);
        if (userIntegral == null) {
            userIntegral = new UserIntegral();
            userIntegral.setUserId(id);
            userIntegral.setIntegralNum(0);
            userIntegralDao.insert(userIntegral);
        }
        return userIntegral;
    }

    @Override
    public int updateIntegral(int type, Long userId, Integer num) {
        return userIntegralDao.updateIntegral(type,userId,num);
    }

}