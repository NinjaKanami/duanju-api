package com.sqx.modules.platform.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.platform.entity.CourseScore;
import com.sqx.modules.platform.service.CourseScoreService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (CourseScore)表控制层
 *
 * @author makejava
 * @since 2024-11-07 18:57:41
 */
@RestController
@RequestMapping("app/courseScore")
public class AppCourseScoreController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CourseScoreService courseScoreService;

    /**
     * 打分
     *
     * @param courseScore 实体对象
     * @return 打分结果
     */
    @PostMapping
    @Login
    public R upset(@RequestAttribute Long userId, @RequestBody CourseScore courseScore) {
        CourseScore score = this.courseScoreService.getOne(new QueryWrapper<CourseScore>()
                .eq("user_id", userId)
                .eq("course_id", courseScore.getCourseId()));
        if (score == null) {
            score = courseScore;
            score.setUserId(userId);
        } else {
            score.setScore(courseScore.getScore());
        }
        return success(this.courseScoreService.saveOrUpdate(score));
    }

}
