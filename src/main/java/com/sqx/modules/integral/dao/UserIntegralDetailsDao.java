package com.sqx.modules.integral.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface UserIntegralDetailsDao extends BaseMapper<UserIntegralDetails> {

    UserIntegralDetails selectUserIntegralDetailsByUserId(@Param("userId") Long userId, @Param("time") Date time);

}
