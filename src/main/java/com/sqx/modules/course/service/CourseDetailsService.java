package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CourseDetailsService extends IService<CourseDetails> {
    Result insert(CourseDetails courseDetails);

    Result updateCourseDetails(CourseDetails courseDetails);

    Result deleteCourseDetails(String ids);

    Result selectCourseDetailsById(Long id,String token,String courseDetailsId);

    Result selectCourseDetailsByTitle(String token,String title);

    Result selectCourseDetailsList(Integer page,Integer limit,String token,String randomNum);

    Result courseDetailsListExcelIn(MultipartFile file, Long courseId) throws IOException;

}
