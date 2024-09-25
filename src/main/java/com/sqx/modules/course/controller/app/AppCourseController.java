package com.sqx.modules.course.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.service.CourseDetailsService;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "APP短剧信息", tags = {"APP短剧信息"})
@RequestMapping(value = "/app/course")
public class AppCourseController extends AbstractController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseDetailsService courseDetailsService;

    @GetMapping("/selectCourse")
    @ApiOperation("查询短剧信息")
    public Result selectCourse(@ApiParam("页") Integer page, @ApiParam("条") Integer limit, @ApiParam("分类id") Long classifyId,
                               @ApiParam("搜索内容") String title, Long bannerId, Integer sort, String token, @ApiParam("付费方式") Integer isPrice,
                               Integer over,Integer wxCourse,Integer dyCourse,Integer wxShow,Integer dyShow, HttpServletRequest request) {
        if(StringUtils.isEmpty(token)){
            token = request.getHeader("Token");
            if(StringUtils.isBlank(token)){
                token = request.getParameter("Token");
            }
        }
        return courseService.selectCourse(page, limit, classifyId, title,null,1,bannerId,sort,token,isPrice,
                null, over,wxCourse,dyCourse,wxShow,dyShow);
    }


    @GetMapping("/selectCourseDetailsById")
    @ApiOperation("根据id查询短剧详情")
    public Result selectCourseDetailsById(Long id,String token,String courseDetailsId){
        return courseDetailsService.selectCourseDetailsById(id,token,courseDetailsId);
    }

    @GetMapping("/selectCourseDetailsByTitle")
    @ApiOperation("根据标题查询短剧详情")
    public Result selectCourseDetailsByTitle(String token,String title){
        return courseDetailsService.selectCourseDetailsByTitle(token,title);
    }

    @GetMapping("/selectCourseDetailsList")
    @ApiOperation("查询推荐视频")
    public Result selectCourseDetailsList(Integer page,Integer limit,String token,String randomNum){
        return courseDetailsService.selectCourseDetailsList(page, limit, token,randomNum);
    }

    @Login
    @GetMapping("/selectCourseTitle")
    @ApiOperation("模糊根据短剧标题查询短剧")
    public Result selectCourseTitle(@ApiParam("页") Integer page, @ApiParam("条") Integer limit, @ApiParam("分类id") Long classifyId,
                                    @ApiParam("搜索内容") String title,Long bannerId,Integer sort,String token, Integer isPrice,Integer over,
                                    Integer wxCourse,Integer dyCourse,Integer wxShow,Integer dyShow) {
        return courseService.selectCourse(page, limit, classifyId, title,null,1,bannerId,sort,token,isPrice,
                null, over,wxCourse,dyCourse,wxShow,dyShow);
    }

    @GetMapping("/selectCourseTitles")
    @ApiOperation("模糊根据短剧标题查询短剧")
    public Result selectCourseTitles(@ApiParam("页") Integer page, @ApiParam("条") Integer limit, @ApiParam("分类id") Long classifyId,
                                     @ApiParam("搜索内容") String title,Long bannerId,Integer sort,String token, Integer isPrice,Integer over,
                                     Integer wxCourse,Integer dyCourse,Integer wxShow,Integer dyShow) {
        return courseService.selectCourse(page, limit, classifyId, title,null,1,bannerId,sort,token,isPrice,
                null, over,wxCourse,dyCourse,wxShow,dyShow);
    }

    @Login
    @PostMapping("/courseNotify")
    @ApiOperation("看广告解锁视频")
    public Result courseNotify(@RequestAttribute Long userId, Long courseId, Long courseDetailsId){
        return courseService.courseNotify(userId, courseId, courseDetailsId);
    }

    @PostMapping("/notifyUrl")
    @ApiOperation("抖音视频回调")
    public JSONObject notifyUrl(@RequestBody JSONObject jsonObject){
        return courseService.notifyUrl(jsonObject);
    }

    @PostMapping("/selectWxVideoUrl")
    @ApiOperation("查询微信短剧播放链接")
    public Result selectWxVideoUrl(@RequestBody JSONObject jsonObject){
        String wxCourseDetailsIds = jsonObject.getString("wxCourseDetailsIds");
        return courseService.selectWxVideoUrl(wxCourseDetailsIds);
    }

}
