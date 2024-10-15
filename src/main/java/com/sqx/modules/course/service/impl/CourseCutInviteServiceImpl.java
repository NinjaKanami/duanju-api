package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    @Resource
    private CommonInfoService commonInfoService;

    /**
     * 获取当天的起始时间和结束时间（字符串格式）
     **/
    private String getTodayBoundary(Date currentDate, int hour, int minute, int second, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertCourseCutInvite(Long cutId, Long userId) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            CourseCut courseCut = courseCutService.getOne(new QueryWrapper<CourseCut>()
                    .eq("cut_id", cutId).eq("cut_status", 0)
                    .ge("deadline", sdf.format(System.currentTimeMillis())));

            if (courseCut == null) {
                return Result.error("砍剧邀请不存在或已结束");
            }

            if (Objects.equals(courseCut.getUserId(), userId)) {
                return Result.error("自己不能为自己助力");
            }

            int i = this.count(new QueryWrapper<CourseCutInvite>().eq("invited_user_id", userId).eq("cut_id", cutId));
            if (i >= 1) {
                return Result.error("已助力过");
            }

            // 每日可砍剧次数
            CommonInfo commonInfo = commonInfoService.findOne(1001);
            if (commonInfo == null) {
                return Result.error("系统配置信息异常");
            }
            // 获取当前时间
            Date currentDate = new Date();

            // 获取当天的起始时间和结束时间
            String todayStartStr = getTodayBoundary(currentDate, 0, 0, 0, 0);
            String todayEndStr = getTodayBoundary(currentDate, 23, 59, 59, 999);

            // 用户已砍剧次数
            int count = this.count(new QueryWrapper<CourseCutInvite>().eq("invited_user_id", userId).between("invited_time", todayStartStr, todayEndStr));
            // 判断是否超过每日砍价次数
            if (count >= Integer.parseInt(commonInfo.getValue())) {
                return Result.error("每日砍价次数已达上限");
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
        } catch (Exception e) {
            // 记录异常日志
            log.error("处理课程砍剧邀请时发生异常", e);
            return Result.error("系统异常，请联系工作人员！");
        }
    }
}
