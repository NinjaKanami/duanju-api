package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.course.entity.CourseDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseDetailsDao extends BaseMapper<CourseDetails> {

    List<CourseDetails> findByCourseId(@Param("id") Long id,@Param("userId") Long userId);

    IPage<CourseDetails> selectCoursePageByCourseId(Page<CourseDetails> page, @Param("id") Long id,@Param("good") Integer good);

    List<CourseDetails> findByCourseIdNotUrl(@Param("id") Long id,@Param("userId") Long userId);

    List<CourseDetails> findByUserCourseId(@Param("id") Long id,@Param("userId") Long userId);


    int deleteCourseDetails(String[] ids);

    IPage<CourseDetails> selectCourseDetailsList(Page<CourseDetails> page,String randomNum);

}
