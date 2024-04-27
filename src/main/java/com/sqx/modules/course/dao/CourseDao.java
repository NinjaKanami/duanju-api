package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.course.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CourseDao extends BaseMapper<Course> {

    int updateDelete(@Param("id") Long id);

    Map<String, Object> selectCourseByCourseId(Long userId,Long courseId);

    IPage<Map<String, Object>> selectCourse(Page<Map<String, Object>> pages, @Param("classifyId") Long classifyId,
                                            @Param("title") String title,@Param("isRecommend") Integer isRecommend,
                                            @Param("status") Integer status,@Param("bannerId") Long bannerId,
                                            @Param("sort") Integer sort,
                                            @Param("isPrice") Integer isPrice,@Param("over") Integer over,
                                            @Param("wxCourse") Integer wxCourse,@Param("dyCourse") Integer dyCourse,
                                            @Param("wxShow") Integer wxShow,@Param("dyShow") Integer dyShow);

    IPage<Map<String, Object>> selectCourseAdmin(Page<Map<String, Object>> pages, @Param("classifyId") Long classifyId,
                                            @Param("title") String title,@Param("isRecommend") Integer isRecommend,
                                            @Param("status") Integer status,@Param("bannerId") Long bannerId,
                                            @Param("sort") Integer sort,@Param("userId") Long userId,
                                            @Param("isPrice") Integer isPrice,@Param("over") Integer over,
                                            @Param("wxCourse") Integer wxCourse,@Param("dyCourse") Integer dyCourse,
                                            @Param("wxShow") Integer wxShow,@Param("dyShow") Integer dyShow);

    /**
     * 根据title 模糊查询短剧
     * @param pages
     * @param title
     * @return
     */
    IPage<Map<String, Object>> selectCourseTitle(Page<Map<String, Object>> pages, @Param("title")String title);

}
