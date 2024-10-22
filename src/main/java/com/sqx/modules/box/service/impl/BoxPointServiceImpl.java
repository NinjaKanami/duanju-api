package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.box.dao.BoxPointDao;
import com.sqx.modules.box.entity.Box;
import com.sqx.modules.box.entity.BoxLog;
import com.sqx.modules.box.entity.BoxPoint;
import com.sqx.modules.box.service.BoxLogService;
import com.sqx.modules.box.service.BoxPointService;
import com.sqx.modules.box.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (BoxPoint)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:34
 */
@Service("boxPointService")
public class BoxPointServiceImpl extends ServiceImpl<BoxPointDao, BoxPoint> implements BoxPointService {

    @Autowired
    private BoxService boxService;
    @Autowired
    private BoxLogService boxLogService;

    /**
     * 获得积分
     *
     * @param userId   用户
     * @param courseId 短剧
     * @param n        随机次数
     * @return 获得盲盒数量
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int getPoints(Long userId, Long courseId, int n) {
        try {
            // 参数校验
            if (userId == null || courseId == null || n <= 0) {
                log.warn("参数错误");
                return 0;
            }

            // 设置最大盲盒
            int maxReward = 3;
            // 设置最大积分
            int maxPoint = 100;
            // 获得盲盒
            int reward = 0;
            // 获得积分
            int point = 0;

            // 查询积分
            BoxPoint boxPoint = this.getOne(new QueryWrapper<BoxPoint>().eq("user_id", userId).eq("course_id", courseId));

            // 判断奖励是否大于等于最大盲盒
            if (boxPoint != null && boxPoint.getReward() >= maxReward) {
                log.warn("奖励已达上限");
                return 0;
            }

            // 没有积分 初始化
            if (boxPoint == null) {
                boxPoint = new BoxPoint();
                boxPoint.setUserId(userId);
                boxPoint.setCourseId(courseId);
                boxPoint.setPoint(0);
                boxPoint.setReward(0);
            }

            // 获得积分
            if (n == 1) {
                // 随机获得积分 1-6
                point = (int) (Math.random() * 6 + 1);
            } else {
                // 随机获得size次积分1-6的和
                for (int i = 0; i < n; i++) {
                    point += (int) (Math.random() * 6 + 1);
                }
            }
            // 获得积分log
            int getPoint = point;
            point += boxPoint.getPoint();

            // 如果最终积分小于100
            if (point < maxPoint) {
                // 更新积分
                boxPoint.setPoint(point);
            } else {
                // 积分大于最大盲盒时取除数 获得盲盒的数量
                reward = point / maxPoint;
                // 判断应得盲盒是否大于等于(最大盲盒-已获得盲盒) 并取最小值
                reward = Math.min(reward, maxReward - boxPoint.getReward());
                // 更新奖励
                boxPoint.setReward(boxPoint.getReward() + reward);

                // 积分大于最大盲盒时取余 获得剩余的积分
                point = point % maxPoint;
                // 更新积分 获得盲盒
                boxPoint.setPoint(point);

                // 查询盲盒
                Box box = boxService.getOne(new QueryWrapper<Box>().eq("user_id", userId));
                // 没有盲盒
                if (box == null) {
                    // 添加盲盒
                    box = new Box();
                    box.setUserId(userId);
                    box.setCount(0);
                }

                // 更新盲盒
                box.setCount(box.getCount() + reward);
                boxService.saveOrUpdate(box);
            }

            // 更新积分
            this.saveOrUpdate(boxPoint);

            // 更新log
            BoxPoint boxPoint1 = this.getOne(new QueryWrapper<BoxPoint>().eq("user_id", userId).eq("course_id", courseId));
            Box box1 = boxService.getOne(new QueryWrapper<Box>().eq("user_id", userId));

            BoxLog boxPointLog = new BoxLog();
            boxPointLog.setForeignId(boxPoint1.getBoxPointId());
            boxPointLog.setType(0);
            boxPointLog.setAction(0);
            boxPointLog.setNum(getPoint);
            boxLogService.save(boxPointLog);

            if (box1 != null) {
                BoxLog boxLog = new BoxLog();
                boxLog.setForeignId(box1.getBoxId());
                boxLog.setType(1);
                boxLog.setAction(0);
                boxLog.setNum(reward);
                boxLogService.save(boxLog);
            }
            return reward;
        } catch (Exception e) {
            log.error("系统异常", e);
        }
        return 0;
    }


    /**
     * 获得积分 旧box表(带courseId)设计方案
     *
     * @param userId   用户
     * @param courseId 短剧
     * @return Result
     */
    /* @Transactional(rollbackFor = Exception.class)
    @Override
    public Result getPointsOld(Long userId, Long courseId, int size) {
        try {
            if (userId == null || courseId == null) {
                return Result.error("参数错误");
            }
            if (size <= 0) {
                return Result.success();
            }

            // 设置最大盲盒
            int maxBox = 3;
            // 设置最大积分
            int maxPoint = 100;
            // 查询盲盒
            Box box = boxService.getOne(new QueryWrapper<Box>().eq("user_id", userId).eq("course_id", courseId));
            if (box != null && box.getCount() >= maxBox) {
                return Result.success("盲盒已达上限");
            }

            // 查询积分
            BoxPoint boxPoint = this.getOne(new QueryWrapper<BoxPoint>().eq("user_id", userId).eq("course_id", courseId));
            // 没有积分
            if (boxPoint == null) {
                boxPoint = new BoxPoint();
                boxPoint.setUserId(userId);
                boxPoint.setCourseId(courseId);
                boxPoint.setPoint(0);
            }

            // 获得积分
            int num = 0;
            // 获得盲盒
            int count = 0;
            if (size == 1) {
                // 随机获得积分 1-6
                num = (int) (Math.random() * 6 + 1);
            } else {
                // 随机获得size次积分1-6的和
                for (int i = 0; i < size; i++) {
                    num += (int) (Math.random() * 6 + 1);
                }
            }
            // 获得积分log
            int get = num;
            // 如果最终积分小于100
            if (boxPoint.getPoint() + num < maxPoint) {
                // 更新积分
                boxPoint.setPoint(boxPoint.getPoint() + num);
            } else {
                // 积分大于100时对100取余并保留除数 获得盲盒的数量
                count = num / maxPoint;
                count = Math.min(count, maxBox);
                // 积分大于100时对100取余 剩余的积分
                num = num % maxPoint;
                // 更新积分 获得盲盒
                boxPoint.setPoint(num);
                // 没有盲盒
                if (box == null) {
                    // 添加盲盒
                    box = new Box();
                    box.setUserId(userId);
                    box.setCourseId(courseId);
                    box.setCount(0);
                }
                // 更新盲盒
                box.setCount(box.getCount() + count);
                boxService.saveOrUpdate(box);
            }
            // 更新积分
            this.saveOrUpdate(boxPoint);

            // 更新log
            BoxPoint boxPoint1 = this.getOne(new QueryWrapper<BoxPoint>().eq("user_id", userId).eq("course_id", courseId));
            Box box1 = boxService.getOne(new QueryWrapper<Box>().eq("user_id", userId).eq("course_id", courseId));

            BoxLog boxLog = new BoxLog();
            boxLog.setForeignId(box1.getBoxId());
            boxLog.setType(1);
            boxLog.setAction(0);
            boxLog.setNum(count);
            boxLogService.save(boxLog);

            BoxLog boxPointLog = new BoxLog();
            boxPointLog.setForeignId(boxPoint1.getBoxPointId());
            boxPointLog.setType(0);
            boxPointLog.setAction(0);
            boxPointLog.setNum(get);
            boxLogService.save(boxPointLog);

        } catch (Exception e) {
            return Result.error("系统异常" + e);
        }
        return Result.success();
    } */


}
