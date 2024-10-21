package com.sqx.modules.course.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.utils.JwtUtils;
import com.sqx.modules.course.dao.CourseDetailsDao;
import com.sqx.modules.course.entity.CourseClassification;
import com.sqx.modules.course.entity.CourseCollect;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.course.service.CourseClassificationService;
import com.sqx.modules.course.service.CourseCollectService;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 教育首页面展示
 *
 * @author liyuan
 * @since 2021-07-15
 */
@RestController
@Api(value = "App短剧分类信息", tags = {"App短剧分类信息"})
@RequestMapping(value = "/app/courseClassification")
@Slf4j
public class AppClassificationController extends AbstractController {
    @Autowired
    private CourseClassificationService courseClassificationService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseDetailsDao courseDetailsDao;
    @Autowired
    private CourseCollectService courseCollectService;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/selectClassification")
    @ApiOperation("查询短剧信息")
    public Result selectClassification() {
        return Result.success().put("data", courseClassificationService.selectClassification());
    }

    /**
     * 查询短剧的分类信息 （未删除）
     *
     * @return
     */
    @RequestMapping(value = "/queryClassification", method = RequestMethod.GET)
    public Result queryClassification(HttpServletRequest request) {
        String token = request.getHeader("token");
        try {
            Long userId = null;
            if (StringUtils.isNotEmpty(token)) {
                if (StringUtils.isNotEmpty(token)) {
                    Claims claims = jwtUtils.getClaimByToken(token);
                    if (claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())) {
                        userId = Long.parseLong(claims.getSubject());
                    }
                }
            }
            List<CourseClassification> classificationList = courseClassificationService.
                    list(new QueryWrapper<CourseClassification>().eq("is_delete", 0).orderByAsc("sort"));
            List<CourseClassification> classificationListRes = new ArrayList<>();
            for (CourseClassification courseClassification : classificationList) {
                if (courseClassification.getCourseId() != null) {
                    courseClassification.setCourse(courseService.selectCourseByCourseId(userId, courseClassification.getCourseId()));
                    CourseDetails courseDetails = courseDetailsDao.selectOne(new QueryWrapper<CourseDetails>().eq("course_id", courseClassification.getCourseId()).last(" order by create_time asc limit 1"));
                    courseClassification.setCourseDetails(courseDetails);
                    int isCollect = 0;
                    if (courseDetails != null && userId != null) {
                        isCollect = courseCollectService.count(new QueryWrapper<CourseCollect>().eq("user_id", userId).eq("classify", 1).eq("course_id", courseDetails.getCourseId()));
                        courseDetails.setIsCollect(isCollect);
                    }
                    if (courseDetails != null && StringUtils.isNotEmpty(courseDetails.getWxCourseDetailsId())) {
                        // 微信内
                        String url = "https://api.weixin.qq.com/wxa/sec/vod/getmedialink?access_token=" + SenInfoCheckUtil.getMpToken();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("media_id", courseDetails.getWxCourseDetailsId());
                        jsonObject.put("t", (System.currentTimeMillis() / 1000) + 7200);
                        String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
                        JSONObject jsonObject1 = JSONObject.parseObject(s);
                        String errcode = jsonObject1.getString("errcode");
                        if (!"0".equals(errcode)) {
                            log.warn("获取微信播放链接失败：" + jsonObject1.getString("errmsg") + ",courseDetailsId:" + courseDetails.getCourseDetailsId());
                        }else{
                            JSONObject media_info = jsonObject1.getJSONObject("media_info");
                            String mp4_url = media_info.getString("mp4_url");
                            courseDetails.setWxUrl(mp4_url);
                        }
                    }
                    classificationListRes.add(courseClassification);
                }
            }
            return Result.success().put("data", classificationListRes);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统发生异常！");
            return Result.error("系统发生异常！");
        }
    }


}
