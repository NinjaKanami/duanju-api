package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseUserDao extends BaseMapper<CourseUser> {

    IPage<Course> selectLatelyCourse(Page<Course> pages, @Param("userId") Long userId);

    IPage<Course> selectCourseByCourseUser(Page<Course> pages, @Param("userId") Long userId);

    /**
     * 查询用户是否订购
     *
     * @param id
     * @param userId
     * @return
     */
    CourseUser selectCourseUser(@Param("id") Long id, @Param("userId") Long userId);

    List<CourseUser> selectCourseUserList(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 修改时间
     *
     * @param courseUser
     * @return
     */
    int updateCourseTime(@Param("courseUser") CourseUser courseUser);

    int deleteCourseUserByVipUser(Long userId);

}
