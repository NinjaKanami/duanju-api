package com.sqx.modules.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.message.entity.ActivityMessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author fang
 * @date 2020/7/9
 */
@Mapper
public interface ActivityMessageInfoDao extends BaseMapper<ActivityMessageInfo> {

    IPage<ActivityMessageInfo> find(Page<ActivityMessageInfo> page,@Param("state") String state);

    IPage<ActivityMessageInfo> findType(Page<ActivityMessageInfo> page,@Param("type") Integer type);

    IPage<ActivityMessageInfo> findTypeByUserId(Page<ActivityMessageInfo> page,@Param("type")String type,@Param("userId") String userId);

    Integer updateState(@Param("state") String state, @Param("id") Long id);

    Integer updateSendState(@Param("sendState") String sendState, @Param("id") Long id);


}
