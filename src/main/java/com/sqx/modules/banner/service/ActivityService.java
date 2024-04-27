package com.sqx.modules.banner.service;


import com.sqx.modules.banner.entity.Activity;

import java.util.List;

public interface ActivityService {


    List<Activity> selectByState(String state);

    Activity selectActivityById(Long id);

    int insertActivity(Activity info);

    int updateActivity(Activity info);

    int deleteActivity(Long id);

    List<Activity> selectActivity();

    List<Activity> selectActivitys();



}
