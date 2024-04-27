package com.sqx.modules.message.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.modules.message.dao.ActivityMessageInfoDao;
import com.sqx.modules.message.entity.ActivityMessageInfo;
import com.sqx.modules.message.service.ActivityMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 消息
 */
@Service
public class ActivityMessageServiceImpl extends ServiceImpl<ActivityMessageInfoDao, ActivityMessageInfo> implements ActivityMessageService {

    @Autowired
    private ActivityMessageInfoDao activityMessageInfoDao;

    @Override
    public List<ActivityMessageInfo> findAll() {
        return activityMessageInfoDao.selectList(null);
    }

    @Override
    public int saveBody(ActivityMessageInfo messageInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setCreateAt(sdf.format(now));
        return activityMessageInfoDao.insert(messageInfo);
    }

    @Override
    public ActivityMessageInfo findOne(long id) {
        return activityMessageInfoDao.selectById(id);
    }

    @Override
    public ActivityMessageInfo selectById(long id) {
        return activityMessageInfoDao.selectById(id);
    }

    @Override
    public int delete(long id) {
        activityMessageInfoDao.deleteById(id);
        return 1;
    }

    @Override
    public PageUtils find(String state, int page,int limit) {
        Page<ActivityMessageInfo> pages = new Page<>(page, limit);
        return new PageUtils(activityMessageInfoDao.find(pages,state));
    }

    @Override
    @Transactional
    public int updateBody(ActivityMessageInfo userInfo) {
        activityMessageInfoDao.updateById(userInfo);
        return 1;
    }

    @Override
    public PageUtils findType(Integer type, int page,int limit) {
        Page<ActivityMessageInfo> pages = new Page<>(page, limit);
        return new PageUtils(activityMessageInfoDao.findType(pages,type));
    }

    @Override
    @Transactional
    public int updateState(String state, Long id) {
        return activityMessageInfoDao.updateState(state,id);
    }

    @Override
    @Transactional
    public int updateSendState(String state, Long id) {
        return activityMessageInfoDao.updateSendState(state,id);
    }

    @Override
    public PageUtils findTypeByUserId( String type,String userId, int page,int limit) {
        Page<ActivityMessageInfo> pages = new Page<>(page, limit);
        return new PageUtils(activityMessageInfoDao.findTypeByUserId(pages,type,userId));
    }



}
