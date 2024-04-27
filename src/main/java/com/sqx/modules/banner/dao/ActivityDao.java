package com.sqx.modules.banner.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.banner.entity.Activity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author fang
 * @date 2020/7/9
 */
@Mapper
public interface ActivityDao extends BaseMapper<Activity> {


    List<Activity> selectByState(String state);

}
