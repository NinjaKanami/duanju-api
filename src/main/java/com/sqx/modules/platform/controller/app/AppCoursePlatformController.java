package com.sqx.modules.platform.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.platform.entity.CourseArea;
import com.sqx.modules.platform.entity.CoursePlatform;
import com.sqx.modules.platform.service.CoursePlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (CoursePlatform)表控制层
 *
 * @author makejava
 * @since 2024-11-07 18:57:41
 */
@Api(value = "App短剧平台", tags = {"App短剧平台"})
@RestController
@RequestMapping("coursePlatform")
public class AppCoursePlatformController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CoursePlatformService coursePlatformService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @Login
    @ApiOperation("查询所有数据")
    @GetMapping
    public R selectAll() {
        return success(this.coursePlatformService.list(new QueryWrapper<CoursePlatform>().orderByAsc("sort")));
    }
}
