package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.course.service.CourseUserService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Slf4j
@RestController
@Api(value = "短剧信息", tags = {"短剧信息"})
@RequestMapping(value = "/course")
public class CourseController extends AbstractController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseUserService courseUserService;


    @GetMapping("/selectCourse")
    @ApiOperation("查询短剧信息")
    public Result selectCourse(@ApiParam("页") Integer page, @ApiParam("条") Integer limit,
                               @ApiParam("分类id") Long classifyId, @ApiParam("搜索内容") String title,
                               Integer isRecommend, Integer status, Long bannerId, Integer sort,
                               Integer isPrice, Integer over, Integer wxCourse, Integer dyCourse,
                               Integer wxShow, Integer dyShow, Integer isCut, Integer priceType,
                               Integer isExternal) {
        return courseService.selectCourse(page, limit, classifyId, title, isRecommend, status, bannerId, sort,
                null, isPrice, 1, over, wxCourse, dyCourse, wxShow, dyShow, isCut, priceType, isExternal);
    }

    @PostMapping("/insertCourse")
    @ApiOperation("添加短剧信息")
    public Result insertCourse(@RequestBody Course course) {
        return courseService.insertCourse(course);
    }

    @PostMapping("/updateCourse")
    @ApiOperation("修改短剧信息")
    public Result updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }

    @GetMapping("/updateDelete")
    @ApiOperation("假删除")
    public Result updateDelete(Long id) {
        return courseService.updateDelete(id);
    }

    @GetMapping("/selectCourseById")
    @ApiOperation("根据id查询短剧详细信息")
    public Result selectCourseById(Integer page, Integer limit, Long id, Integer good) {
        return courseService.selectCourseById(page, limit, id, good);
    }


    @GetMapping("/selectCourseUserbyid")
    @ApiOperation("我的短剧")
    public Result selectCourseUser(Integer page, Integer limit, Long userId) {
        return courseUserService.selectCourseUser(page, limit, userId);
    }

    @GetMapping("/updateCourse")
    @ApiOperation("修改状态")
    public Result updateCourse(Long courseId) {
        Course byId = courseService.getById(courseId);
        if (byId != null) {
            if (byId.getStatus().equals(1)) {
                byId.setStatus(2);
            } else {
                byId.setStatus(1);
            }
            courseService.updateById(byId);
        }
        return Result.success();
    }

    @GetMapping("/synCourse")
    @ApiOperation("采集视频")
    public Result synCourse() {
        return courseService.synCourse();
    }

    @PostMapping("/updateCourseDetails")
    @ApiOperation("批量修改集")
    public Result updateCourseDetails(String ids, BigDecimal price, String content, String titleImg) {
        return courseService.updateCourseDetails(ids, price, content, titleImg);
    }


    @PostMapping("/updateCourseStatus")
    @ApiOperation("批量上下架剧")
    public Result updateCourseStatus(String ids, Integer status) {
        return courseService.updateCourseStatus(ids, status);
    }

    @PostMapping("/deleteCourseByIds")
    @ApiOperation("批量删除剧")
    public Result deleteCourseByIds(String ids) {
        return courseService.deleteCourseByIds(ids);
    }

    @PostMapping("/deleteCourseDetailsByIds")
    @ApiOperation("批量删除集")
    public Result deleteCourseDetailsByIds(String ids) {
        return courseService.deleteCourseDetailsByIds(ids);
    }

    @PostMapping("/dyVideoUpload")
    @ApiOperation("抖音短剧上传")
    public Result dyVideoUpload(Long courseId) {
        return courseService.dyVideoUpload(courseId);
    }

    @PostMapping("/dyVideoAudit")
    @ApiOperation("抖音短剧送审")
    public Result dyVideoAudit(Long courseId) {
        return courseService.dyVideoAudit(courseId);
    }

    @PostMapping("/dyVideoUp")
    @ApiOperation("抖音短剧上线")
    public Result dyVideoUp(Long courseId) {
        return courseService.dyVideoUp(courseId);
    }

    @PostMapping("/setDyNotifyUrl")
    @ApiOperation("设置抖音视频回调地址")
    public Result setDyNotifyUrl(String url) {
        return courseService.setDyNotifyUrl(url);
    }

    @PostMapping("/uploadCourseDetails")
    @ApiOperation("单个集上传")
    public Result uploadCourseDetails(Long courseDetailsId) {
        return courseService.uploadCourseDetails(courseDetailsId);
    }

    @PostMapping("/updateDyCourse")
    @ApiOperation("修改抖音短剧")
    public Result updateDyCourse(@RequestBody Course course) {
        return courseService.updateDyCourse(course);
    }

    @GetMapping("/sysWxCourse")
    @ApiOperation("同步微信已提交审核的短剧")
    public Result sysWxCourse() {
        return courseService.sysWxCourse();
    }

    /**
     * 剧导入列表--导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "剧导入列表--导入")
    @PostMapping(value = "/courseListExcelIn")
    public Result courseListExcelIn(@ApiParam(name = "file", value = "excel文件") @RequestPart MultipartFile file) throws Exception {
        try {
            if (file == null) {
                return Result.error("文件不能为空！");
            }
            return courseService.courseListExcelIn(file);
        } catch (Exception e) {
            log.error("剧导入列表--导入异常：", e);
        }

        // 返回结果
        return Result.error("导入失败");
    }

}
