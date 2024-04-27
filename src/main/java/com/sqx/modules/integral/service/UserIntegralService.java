package com.sqx.modules.integral.service;

import com.sqx.modules.integral.entity.UserIntegral;

public interface UserIntegralService {

    UserIntegral selectById(Long id);

    int updateIntegral(int type, Long userId, Integer num);

}
