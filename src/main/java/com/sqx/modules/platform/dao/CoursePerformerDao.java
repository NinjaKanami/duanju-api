package com.sqx.modules.platform.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.course.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.sqx.modules.platform.entity.CoursePerformer;

/**
 * (CoursePerformer)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-07 18:57:43
 */
@Mapper
public interface CoursePerformerDao extends BaseMapper<CoursePerformer> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CoursePerformer> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CoursePerformer> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CoursePerformer> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CoursePerformer> entities);

    /**
     * 根据演员id查询参演的短剧列表,
     *
     * @param performerId 演员id
     * @param wxShow      如果wxShow为1，则查询在微信中显示的短剧，不传则默认查询全部
     * @return List<Course> 参演的短剧列表
     */
    List<Course> selectCourseListByPerformerId(
            @Param("performerId") Long performerId,
            @Param("wxShow") Long wxShow
    );
}
