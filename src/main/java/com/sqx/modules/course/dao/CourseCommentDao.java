package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.course.entity.CourseComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CourseCommentDao extends BaseMapper<CourseComment> {

    int updateCourseComment(@Param("type") Integer type, @Param("courseCommentId") Long courseCommentId);

    IPage<CourseComment> selectCourseComment(Page<CourseComment> page, @Param("courseId") Long courseId,@Param("userId") Long userId);

    /**
     * 删除评论的点赞关联
     * @param courseCommentId
     * @return
     */
    int deleteCommentGood(@Param("courseCommentId") Long courseCommentId);
    
    IPage<Map<String,Object>> selectCourseCommentByUserId(Page<Map<String,Object>> page,@Param("userId") Long userId);
    
    
}
