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
    /**
     * admin查询演员列表
     *
     * @param page    默认从0开始
     * @param limit   条数
     * @param name    演员名模糊搜索
     * @param sex     性别
     * @param company 公司模糊搜索
     * @param tag     演员类型
     * @param sort    ID排序
     * @return 演员列表
     */
    List<Performer> selectPerformersWithCondition(@Param("page") Integer page,
                                                  @Param("limit") Integer limit,
                                                  @Param("name") String name,
                                                  @Param("sex") Integer sex,
                                                  @Param("company") String company,
                                                  @Param("tag") Integer tag,
                                                  @Param("sort") String sort
    );

    /**
     * 根据用户ID查询用户关注演员列表
     *
     * @param userId 用户id
     * @return 关注演员列表
     */
    List<Performer> selectUserFollowPerformerList(@Param("userId") Long userId);

    /**
     * 根据follow数查询演员排行榜
     *
     * @param ptagId 演员类型id
     * @param sex    性别
     * @param sort   follow数(ASC/DESC)
     * @return 演员列表
     */
    List<Performer> selectPerformerRankOrderByFollower(
            @Param("ptagId") Long ptagId,
            @Param("sex") Integer sex,
            @Param("sort") String sort
    );

}
