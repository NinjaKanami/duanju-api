package com.sqx.modules.performer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.performer.entity.Performer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (Performer)表数据库访问层
 *
 * @author bootun
 * @since 2024-11-06
 */
@Mapper
public interface PerformerDao extends BaseMapper<Performer> {
    List<Performer> selectPerformersWithCondition(@Param("page") Integer page,
                                                  @Param("limit") Integer limit,
                                                  @Param("name") String name,
                                                  @Param("sex") Integer sex,
                                                  @Param("company") String company,
                                                  @Param("tag") Integer tag,
                                                  @Param("sort") String sort);

    List<Performer> selectUserFollowPerformerList(@Param("userId") Long userId);

}
