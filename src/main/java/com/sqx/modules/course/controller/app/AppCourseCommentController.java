package com.sqx.modules.course.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.entity.CourseComment;
import com.sqx.modules.course.service.CourseCommentService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 修改部分代码逻辑
 *
 * @author liyuan
 * @since 2021-07-16
 */
@RestController
@Api(value = "短剧评论信息", tags = {"短剧评论信息"})
@RequestMapping(value = "/app/courseComment")
public class AppCourseCommentController extends AbstractController {
    @Autowired
    private CourseCommentService courseCommentService;

    @Login
    @PostMapping("/insertCourseComment")
    @ApiOperation("添加评论")
    public Result insertCourseComment(@RequestBody CourseComment courseComment, @RequestAttribute("userId") Long userId) {
        courseComment.setUserId(userId);
        return courseCommentService.insertCourseComment(courseComment);
    }

    /**
     * 有赞时取消点赞  没赞时点赞
     *
     * @param courseCommentId
     * @param userId
     * @return
     */
    @Login
    @GetMapping("/updateGoodsNum")
    @ApiOperation("点赞评论")
    public Result updateGoodsNum(Long courseCommentId, @RequestAttribute("userId") Long userId) {
        return courseCommentService.updateGoodsNum(courseCommentId, userId);
    }

    /**
     * 查看短剧下的所有评论内容  时间  评论人  评论人图像  评论点赞次数
     *
     * @param page
     * @param limit
     * @param courseId
     * @return
     */
    @Login
    @GetMapping("/selectCourseComment")
    @ApiOperation("查看评论")
    public Result selectCourseComment(Integer page, Integer limit, Long courseId, @RequestAttribute("userId") Long userId) {
        return courseCommentService.selectCourseComment(page, limit, courseId,userId);
    }

    /**
     * 删除评论（删除评论的信息 删除评论的点赞关联 ）
     *
     * @param courseCommentId
     * @return
     */
    @Login
    @GetMapping("/deleteCourseComment")
    @ApiOperation("删除评论")
    public Result deleteCourseComment(Long courseCommentId) {
        return courseCommentService.deleteCourseComment(courseCommentId);
    }
}
