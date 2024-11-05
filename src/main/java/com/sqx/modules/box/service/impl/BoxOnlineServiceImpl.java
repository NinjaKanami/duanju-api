package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.box.dao.BoxOnlineDao;
import com.sqx.modules.box.entity.Box;
import com.sqx.modules.box.entity.BoxLog;
import com.sqx.modules.box.entity.BoxOnline;
import com.sqx.modules.box.service.BoxLogService;
import com.sqx.modules.box.service.BoxOnlineService;
import com.sqx.modules.box.service.BoxService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * (BoxOnline)表服务实现类
 *
 * @author makejava
 * @since 2024-10-23 13:08:05
 */
@Slf4j
@Service("boxOnlineService")
public class BoxOnlineServiceImpl extends ServiceImpl<BoxOnlineDao, BoxOnline> implements BoxOnlineService {

    @Autowired
    private BoxService boxService;
    @Autowired
    private BoxLogService boxLogService;
    @Autowired
    private CommonInfoService commonInfoService;

    /**
     * 更新在线时间
     *
     * @param userId 用户
     * @param minute 时间
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateOnline(Long userId, int minute) {
        try {

            // 最大盲盒
            CommonInfo commonInfo = commonInfoService.findOne(2006);
            int maxReward = Integer.parseInt(commonInfo.getValue());

            // 随机数 5-10
            int random = (int) ((Math.random() * 6) + 5);

            // 当前时间
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String now = LocalDateTime.now().format(formatter);

            // 查询是否已存在
            BoxOnline boxOnline = getOne(new QueryWrapper<BoxOnline>().eq("user_id", userId));
            if (boxOnline == null) {
                boxOnline = new BoxOnline();
                boxOnline.setUserId(userId);
                boxOnline.setMinute(0);
                boxOnline.setNextMinute(random);
                boxOnline.setReward(0);
                boxOnline.setCreateTime(now);
                boxOnline.setUpdateTime(now);
            }
            Integer localMinute = boxOnline.getMinute();

            // 更新日期与当前日期不同时 重置获得数量与时间
            if (!boxOnline.getUpdateTime().substring(0, 10).equals(now.substring(0, 10))) {
                boxOnline.setReward(0);
                boxOnline.setMinute(0);
            }

            // 获得已达上限
            if (boxOnline.getReward() >= maxReward) {
                return Result.error("已达上限");
            }

            // 更新分钟
            boxOnline.setMinute(boxOnline.getMinute() + minute);
            // 未到下次时间
            if (boxOnline.getMinute() < boxOnline.getNextMinute()) {
                if (boxOnline.getBoxOnlineId() != null) {
                    boolean b = update(boxOnline, new QueryWrapper<BoxOnline>().eq("box_online_id", boxOnline.getBoxOnlineId()).eq("minute", localMinute));
                    if (!b) {
                        throw new Exception("数据不存在，更新失败");
                    }
                } else {
                    save(boxOnline);
                }
                return Result.error("未到时间");
            }

            // 更新
            boxOnline.setMinute(boxOnline.getMinute() - boxOnline.getNextMinute());
            boxOnline.setReward(boxOnline.getReward() + 1);
            boxOnline.setNextMinute(random);
            if (boxOnline.getBoxOnlineId() != null) {
                boolean b = update(boxOnline, new QueryWrapper<BoxOnline>().eq("box_online_id", boxOnline.getBoxOnlineId()).eq("minute", localMinute));
                if (!b) {
                    throw new Exception("数据不存在，更新失败");
                }
            } else {
                save(boxOnline);
            }

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
            box.setCount(box.getCount() + 1);
            boxService.saveOrUpdate(box);

            // 更新log
            boxOnline = getOne(new QueryWrapper<BoxOnline>().eq("user_id", userId));
            BoxLog boxPointLog = new BoxLog();
            boxPointLog.setForeignId(boxOnline.getBoxOnlineId());
            boxPointLog.setType(3);
            boxPointLog.setAction(0);
            boxPointLog.setNum(1);
            boxLogService.save(boxPointLog);

            return Result.success();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新在线时间失败:" + e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("更新在线时间失败:" + e);
        }
    }
}
