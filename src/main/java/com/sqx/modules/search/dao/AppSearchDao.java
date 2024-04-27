package com.sqx.modules.search.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.search.entity.Search;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface AppSearchDao extends BaseMapper<Search> {

    /**
     * 经常搜索的名称
     *
     * @return
     */
    List<String> selectAppSearchNum();

    /**
     * 删除用户的搜索记录
     */
    int deleteAppSearch(Long userId);
}
