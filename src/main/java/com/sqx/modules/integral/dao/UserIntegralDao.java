package com.sqx.modules.integral.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.integral.entity.UserIntegral;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserIntegralDao extends BaseMapper<UserIntegral> {

    int updateIntegral(@Param("type") int type, @Param("userId") Long userId, @Param("num") Integer num);

}