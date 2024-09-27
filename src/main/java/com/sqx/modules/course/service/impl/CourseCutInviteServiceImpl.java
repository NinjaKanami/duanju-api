package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.course.dao.CourseCutInviteDao;
import com.sqx.modules.course.entity.CourseCollect;
import com.sqx.modules.course.entity.CourseCut;
import com.sqx.modules.course.entity.CourseCutInvite;
import com.sqx.modules.course.entity.CourseUser;
import com.sqx.modules.course.service.CourseCollectService;
import com.sqx.modules.course.service.CourseCutInviteService;
import com.sqx.modules.course.service.CourseCutService;
import com.sqx.modules.course.service.CourseUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Objects;


/**
 * (CourseCutInvite)表服务实现类
 *
 * @author makejava
 * @since 2024-09-26 19:54:53
 */
@Service("courseCutInviteService")
public class CourseCutInviteServiceImpl extends ServiceImpl<CourseCutInviteDao, CourseCutInvite> implements CourseCutInviteService {

    @Resource
    private CourseCutService courseCutService;

    @Resource
    private CourseCollectService courseCollectService;

    @Resource
    private CourseUserService courseUserService;

    @Resource
    private UserMoneyService userMoneyService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Result insertCourseCutInvite(Long cutId, Long userId) {

        CourseCut courseCut = courseCutService.getOne(new QueryWrapper<CourseCut>()
                .eq("cut_id", cutId).eq("cut_status", 0)
                .ge("deadline", sdf.format(System.currentTimeMillis())));

        if (courseCut == null) {
            return Result.error("砍剧邀请不存在或已结束");
        }

        if (Objects.equals(courseCut.getUserId(), userId)) {
            return Result.error("自己不能为自己助力");
        }

        CourseCutInvite courseCutInvite = new CourseCutInvite();
        courseCutInvite.setCutId(cutId);
        courseCutInvite.setInvitedUserId(userId);
        courseCutInvite.setInvitedTime(sdf.format(System.currentTimeMillis()));

        // 插入砍剧信息并返回
        if (this.save(courseCutInvite)) {
            /* //已邀请人数小于应请人数，则更新砍价人数&状态 因为查的是状态为0 不用判断
            if(courseCut.getInvitedCount()<courseCut.getInviteCount()){ */
            // 更新砍价人数
            courseCut.setInvitedCount(courseCut.getInvitedCount() + 1);
            // 判断是否砍价完成
            if (Objects.equals(courseCut.getInvitedCount(), courseCut.getInviteCount())) {
                // 人数全部邀请完成，更新状态1
                courseCut.setCutStatus(1);
                // 新增短剧收藏-砍剧记录
                CourseCollect courseCollect = new CourseCollect();
                courseCollect.setCourseId(courseCut.getCourseId());
                courseCollect.setUserId(courseCut.getUserId());
                courseCollect.setClassify(6);
                courseCollect.setCreateTime(sdf.format(System.currentTimeMillis()));
                courseCollect.setUpdateTime(sdf.format(System.currentTimeMillis()));
                courseCollectService.save(courseCollect);
                // 新增用户短剧-整集记录
                CourseUser courseUser = new CourseUser();
                courseUser.setCourseId(courseCut.getCourseId());
                courseUser.setUserId(courseCut.getUserId());
                courseUser.setClassify(1);
                courseUser.setCreateTime(sdf.format(System.currentTimeMillis()));
                courseUser.setUpdateTime(sdf.format(System.currentTimeMillis()));
                courseUserService.save(courseUser);
            }
            courseCutService.updateById(courseCut);
            /* } */
            // 如果奖励点券不为空，则更新用户余额
            if (courseCut.getRewardMoney() != null) {
                userMoneyService.updateMoney(1, userId, courseCut.getRewardMoney());
            }
            return Result.success();
        } else {
            return Result.error("系统繁忙，请稍后再试！");
        }

    }
}
