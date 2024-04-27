package com.sqx.modules.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.message.entity.MessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author fang
 * @date 2020/7/9
 */
@Mapper
public interface MessageInfoDao extends BaseMapper<MessageInfo> {

    int updateSendState(@Param("userId") Long userId, @Param("state") Integer state);

}
