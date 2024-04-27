package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.app.entity.UserMoney;

public interface UserMoneyService extends IService<UserMoney> {

    UserMoney selectUserMoneyByUserId(Long userId);

    UserMoney selectSysUserMoneyByUserId(Long userId);

    void updateMoney(int i, Long userId, double money);

    void updateSysMoney(int i, Long userId, double money);

}
