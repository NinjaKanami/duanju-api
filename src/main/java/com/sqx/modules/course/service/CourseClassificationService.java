package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseClassification;
import com.sqx.modules.course.response.ClassificationResponse;
import com.sqx.modules.course.response.CurriculumResponse;

import java.util.List;
import java.util.Map;

public interface CourseClassificationService extends IService<CourseClassification> {
    Result insertCourseClassification(CourseClassification course);

    Result updateCourseClassification(CourseClassification course);

    Result updateDelete(Long id);

    Result selectCourseClassification(Integer page, Integer limit, String classificationName);

    CourseClassification selectCourseClassificationById(Long id);

    List<Map<String, Object>> selectClassification();

    /**
     *查询短剧的分类信息
     * @return
     */
    List<ClassificationResponse> queryClassification();

}
