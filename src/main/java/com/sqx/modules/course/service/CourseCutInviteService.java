package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseCutInvite;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * (CourseCutInvite)表服务接口
 *
 * @author makejava
 * @since 2024-09-26 19:54:53
 */
public interface CourseCutInviteService extends IService<CourseCutInvite> {

    Result insertCourseCutInvite(Long cutId,Long userId);
}
