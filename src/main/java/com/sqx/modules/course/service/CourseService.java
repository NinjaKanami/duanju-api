package com.sqx.modules.course.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.Course;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public interface CourseService extends IService<Course> {
    Result insertCourse(Course course);

    Result updateCourse(Course course);

    Result updateDelete(Long id);

    Map<String, Object> selectCourseByCourseId(Long userId, Long courseId);

    Result selectCourse(Integer page, Integer limit, Long classifyId, String title, Integer isRecommend,
                        Integer status, Long bannerId, Integer sort, String token, Integer isPrice, Integer admin, Integer over,
                        Integer wxCourse,Integer dyCourse,Integer wxShow,Integer dyShow);

    Result selectCourseById(Integer page,Integer limit,Long id,Integer good);

    Result selectCourseTitle(Integer page, Integer limit, String title, Long userId);

    Result synCourse();

    Result updateCourseDetails(String ids, BigDecimal price,String content,String titleImg);

    Result updateCourseStatus(String ids, Integer status);

    Result deleteCourseByIds(String ids);

    Result deleteCourseDetailsByIds(String ids);

    Result courseNotify(Long userId, Long courseId, Long courseDetailsId);

    Result dyVideoUpload(Long courseId);

    Result dyVideoAudit(Long courseId);

    Result dyVideoUp(Long courseId);

    Result setDyNotifyUrl(String notifyUrl);

    JSONObject notifyUrl(JSONObject jsonObject);

    Result uploadCourseDetails(Long courseDetailsId);

    Result updateDyCourse(Course course);

    Result sysWxCourse();

    Result selectWxVideoUrl(String wxCourseDetailsIds);

    Result courseListExcelIn(MultipartFile file) throws IOException;

}
