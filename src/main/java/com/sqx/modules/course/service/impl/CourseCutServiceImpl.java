package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.dao.CourseCutDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseCut;
import com.sqx.modules.course.entity.CourseCutInvite;
import com.sqx.modules.course.service.CourseCutInviteService;
import com.sqx.modules.course.service.CourseCutService;
import com.sqx.modules.course.service.CourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * (CourseCut)表服务实现类
 *
 * @author makejava
 * @since 2024-09-26 19:54:35
 */
@Service("courseCutService")
public class CourseCutServiceImpl extends ServiceImpl<CourseCutDao, CourseCut> implements CourseCutService {

    @Resource
    private CourseService courseService;
    @Resource
    private CourseCutInviteService courseCutInviteService;

    @Override
    public Result selectCourseCut(Long cutId, Long userId) {

        if (cutId == null || userId == null) {
            return Result.error("缺少必要的参数");
        }

        // 查询砍剧信息
        CourseCut courseCut = this.getOne(new QueryWrapper<CourseCut>().eq("cut_id", cutId).eq("user_id", userId));

        if (courseCut == null) {
            return Result.error("砍剧信息不存在");
        }

        // 查询砍剧邀请信息
        List<CourseCutInvite> courseCutInviteList = courseCutInviteService.list(new QueryWrapper<CourseCutInvite>().eq("cut_id", cutId));

        courseCut.setCourseCutInviteList(courseCutInviteList);

        return Result.success().put("data", courseCut);
    }

    @Override
    public Result insertCourseCut(Long courseId, Long userId) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (courseId == null || userId == null) {
                return Result.error("缺少必要的参数");
            }

            // 判断短剧是否存在
            Course course = courseService.getById(courseId);
            if (course == null) {
                return Result.error("短剧不存在");
            }

            // 判断短剧的参数是否符合砍剧
            if (course.getIsCut() == 0) {
                return Result.error("砍剧信息未配置");
            } else if (course.getInviteTarget() == 0) {
                return Result.error("砍剧人数未配置");
            } else if (course.getCutTimeLimit() == 0) {
                return Result.error("砍剧时间未配置");
            }/* else if (course.getRewardMoney() == 0){
            return Result.error("砍剧金额未配置");
            } */

            // 判断短剧是否正在砍或已砍过
            List<CourseCut> courseCutList = this.list(new QueryWrapper<CourseCut>()
                    .eq("course_id", courseId).eq("user_id", userId).eq("cut_status", 1)
                    .or()
                    .eq("course_id", courseId).eq("user_id", userId)
                    .eq("cut_status", 0).ge("deadline", sdf.format(System.currentTimeMillis()))
            );
            if (!courseCutList.isEmpty()) {
                return Result.error("短剧正在砍或已砍过");
            }

            CourseCut courseCut = new CourseCut();
            courseCut.setCourseId(courseId);
            courseCut.setUserId(userId);
            courseCut.setCutStatus(0);
            courseCut.setInviteCount(course.getInviteTarget());
            courseCut.setInvitedCount(0);
            courseCut.setRewardMoney(course.getRewardMoney());

            // 当前时间
            Date now = new Date();
            String nowString = sdf.format(now);

            // 设置截止时间
            long deadlineMillis = now.getTime() + course.getCutTimeLimit() * 3600 * 1000;
            Date deadline = new Date(deadlineMillis);
            String deadlineString = sdf.format(deadline);

            courseCut.setDeadline(deadlineString);
            courseCut.setCreateTime(nowString);
            courseCut.setUpdateTime(nowString);

            // 插入砍剧信息并返回
            if (this.save(courseCut)) {
                CourseCut cut = this.getOne(new QueryWrapper<CourseCut>()
                        .eq("course_id", courseId).eq("user_id", userId)
                        .eq("cut_status", 0).ge("deadline", sdf.format(System.currentTimeMillis())));
                return Result.success().put("data", cut);
            } else {
                return Result.error("系统繁忙，请稍后再试！");
            }
        } catch (Exception e) {
            // 记录异常日志
            log.error("处理课程砍剧时发生异常", e);
            return Result.error("系统异常，请联系工作人员！");
        }

    }

}
