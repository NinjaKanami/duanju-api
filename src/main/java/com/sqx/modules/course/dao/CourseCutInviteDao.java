package com.sqx.modules.course.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.sqx.modules.course.entity.CourseCutInvite;

/**
 * (CourseCutInvite)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-26 19:54:53
 */
@Mapper
public interface CourseCutInviteDao extends BaseMapper<CourseCutInvite> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CourseCutInvite> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CourseCutInvite> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CourseCutInvite> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CourseCutInvite> entities);

    /**
     * 根据cutId查询邀请人信息
     *
     * @param cutId 砍剧id
     * @return java.util.List<com.sqx.modules.course.entity.CourseCutInvite>
     * @author Zhou
     **/
    List<CourseCutInvite> selectUsersByCutId(Long cutId);

}
