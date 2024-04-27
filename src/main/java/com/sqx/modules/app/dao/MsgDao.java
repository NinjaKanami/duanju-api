package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.app.entity.Msg;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 *
 */
@Mapper
public interface MsgDao extends BaseMapper<Msg> {

    Msg findByPhone(String phone);

    Msg findByPhoneAndCode(String phone, String msg);



}
