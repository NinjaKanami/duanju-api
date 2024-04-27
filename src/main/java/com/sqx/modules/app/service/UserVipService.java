package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.app.entity.UserVip;

public interface UserVipService extends IService<UserVip> {

    UserVip selectUserVipByUserId(Long userId);

}
