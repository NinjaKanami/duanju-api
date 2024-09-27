package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseCut;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * (CourseCut)表服务接口
 *
 * @author makejava
 * @since 2024-09-26 19:54:35
 */
public interface CourseCutService extends IService<CourseCut> {

    Result selectCourseCut(Long courseId,Long userId);

    Result insertCourseCut(Long courseId,Long userId);

}
