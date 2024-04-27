package com.sqx.modules.integral.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.integral.entity.UserIntegralDetails;

public interface UserIntegralDetailsService extends IService<UserIntegralDetails> {

    IPage selectUserIntegralDetailsByUserId(int page, int limit, Long userId);

    Result signIn(Long userId);

    Result selectIntegralDay(Long userId);

    Result creditsExchange(Long userId,Integer integral);
}
