package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.app.dao.UserVipDao;
import com.sqx.modules.app.entity.UserVip;
import com.sqx.modules.app.service.UserVipService;
import org.springframework.stereotype.Service;
@Service
public class UserVipServiceImpl extends ServiceImpl<UserVipDao, UserVip> implements UserVipService {

    @Override
    public UserVip selectUserVipByUserId(Long userId) {
        QueryWrapper<UserVip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return baseMapper.selectOne(queryWrapper);
    }

//    @Scheduled(cron="0 */1 * * * ?")
//    public void getEndVip() {
//        baseMapper.updateUserVipByEndTime();
//    }

}
