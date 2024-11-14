package com.sqx.modules.course.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.entity.CourseCutInvite;
import com.sqx.modules.course.service.CourseCutInviteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (CourseCutInvite)表控制层
 *
 * @author makejava
 * @since 2024-09-26 19:54:53
 */
@Api(value = "App短剧砍剧邀请", tags = {"App短剧砍剧邀请"})
@RestController
@RequestMapping("app/courseCutInvite")
public class AppCourseCutInviteController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CourseCutInviteService courseCutInviteService;

    @Login
    @PostMapping("/insertCourseCutInvite")
    @ApiOperation("新增砍剧短剧邀请信息")
    public Result insertCourseCutInvite(@Validated @RequestParam("cutId") Long cutId,
                                        @Validated @RequestAttribute("userId") Long userId) {
        return courseCutInviteService.insertCourseCutInvite(cutId, userId);
    }

}
