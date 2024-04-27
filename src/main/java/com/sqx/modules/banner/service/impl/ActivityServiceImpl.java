package com.sqx.modules.banner.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.banner.dao.ActivityDao;
import com.sqx.modules.banner.entity.Activity;
import com.sqx.modules.banner.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 活动推广
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityDao, Activity> implements ActivityService {


    @Autowired
    private ActivityDao activityDao;

    @Override
    public List<Activity> selectByState(String state) {
        return activityDao.selectByState(state);
    }

    @Override
    public Activity selectActivityById(Long id) {
        return activityDao.selectById(id);
    }

    @Override
    public int insertActivity(Activity activity) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        activity.setCreateAt(sdf.format(now));
        return activityDao.insert(activity);
    }

    @Override
    public int updateActivity(Activity activity) {
        return activityDao.updateById(activity);
    }

    @Override
    public int deleteActivity(Long id) {
        return activityDao.deleteById(id);
    }

    @Override
    public List<Activity> selectActivity() {
        return activityDao.selectList(new QueryWrapper<Activity>().eq("state", 1));
    }

    @Override
    public List<Activity> selectActivitys() {
        return activityDao.selectList(null);
    }


}
