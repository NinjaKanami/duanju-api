package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.app.entity.UserVip;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserVipDao extends BaseMapper<UserVip> {

    int updateUserVipByEndTime();

}
