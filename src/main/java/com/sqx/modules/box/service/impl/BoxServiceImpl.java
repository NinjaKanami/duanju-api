package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.box.dao.BoxDao;
import com.sqx.modules.box.entity.*;
import com.sqx.modules.box.service.*;
import com.sqx.modules.box.util.DataSync;
import com.sqx.modules.box.vo.BoxCollection;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * (Box)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:36
 */
@Slf4j
@Service("boxService")
public class BoxServiceImpl extends ServiceImpl<BoxDao, Box> implements BoxService {

    @Resource
    private BoxItemService boxItemService;
    @Resource
    private CollectService collectService;
    @Resource
    private CollectPointService collectPointService;
    @Resource
    private CollectLogService collectLogService;
    @Resource
    private CommonInfoService commonInfoService;
    @Resource
    private UserService userService;
    @Resource
    private DataSync dataSync;
    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 查询 盲盒 青龙 龙鳞 记录 最大发行 剩余数量
     *
     * @param userId 用户ID
     * @return 盲盒 青龙 龙鳞 记录 最大发行 剩余数量
     */
    @Override
    public Result selectBoxCollection(Long userId) {
        try {

            if (userId == null) {
                return Result.error("查询失败，用户ID为空！");
            }
            UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
            if (user == null) {
                return Result.error("用户不存在");
            }
            if (user.getPhone() == null) {
                return Result.error("请先绑定手机号");
            }

            BoxCollection boxCollection = new BoxCollection();

            // 盲盒数量
            Box box = this.getOne(new QueryWrapper<Box>().eq("user_id", userId));
            if (box != null) {
                boxCollection.setBox(box.getCount());
            }
            /* List<Box> boxList = this.list(new QueryWrapper<Box>().eq("user_id", userId));
            if (boxList != null) {
                int sum = boxList.stream()
                        .mapToInt(Box::getCount)
                        .sum();
                boxCollection.setBox(sum);
            } */

            // 获取同步数据
            BoxCollection userCollection = dataSync.getUserCollection(user.getPhone());
            boxCollection.setRegistered(userCollection.isRegistered());
            // 获得龙鳞数量
            CollectPoint collectPoint = collectPointService.getOne(new QueryWrapper<CollectPoint>().eq("user_id", userId));
            if (collectPoint != null) {
                boxCollection.setCollectPoint(collectPoint.getCount());
            } else {
                boxCollection.setCollectPoint(BigDecimal.ZERO);
            }
            boxCollection.setCollectPoint(boxCollection.getCollectPoint().add(userCollection.getCollectPoint()));

            // 获得青龙数量
            Collect collect = collectService.getOne(new QueryWrapper<Collect>().eq("user_id", userId));
            if (collect != null) {
                boxCollection.setCollect(collect.getCount());
            } else {
                boxCollection.setCollect(0);
            }
            boxCollection.setCollect(boxCollection.getCollect() + userCollection.getCollect());

            // 日志列表
            boxCollection.setCollectLogs(collectLogService.list(new QueryWrapper<CollectLog>().eq("user_id", userId)));

            // 最大发行数量
            CommonInfo max = commonInfoService.findOne(2003);
            if (max != null) {
                // boxCollection.setCollectMax(Integer.parseInt(max.getValue()));
                boxCollection.setCollectPointMax(new BigDecimal(max.getValue()));
            }
            // 剩余发行数量
            CommonInfo remain = commonInfoService.findOne(2004);
            if (remain != null) {
                // boxCollection.setCollectRemain(Integer.parseInt(remain.getValue()));
                boxCollection.setCollectPointRemain(new BigDecimal(remain.getValue()));
            }

            return Result.success().put("data", boxCollection);
        } catch (Exception e) {
            log.error("查询用户盲盒藏品信息失败", e);
            return Result.error("查询失败");
        }

    }

    /**
     * 开盒
     *
     * @param userId 用户Id
     * @param count  数量
     * @return 开盒结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result openBox(Long userId, int count) {
        try {
            // 剩余发行数量
            CommonInfo remain = commonInfoService.findOne(2004);
            if (remain == null) {
                return Result.error("龙鳞暂无发行数量");
            }
            String localRemain = remain.getValue();
            BigDecimal remainValue = new BigDecimal(remain.getValue());
            if (remainValue.compareTo(BigDecimal.valueOf(0)) < 0) {
                return Result.error("龙鳞发行数量不足");
            }

            Box user = getOne(new QueryWrapper<Box>().eq("user_id", userId));
            if (user == null) {
                return Result.error("用户未获得盲盒");
            }
            if (count <= 0 || count > user.getCount()) {
                return Result.error("数量不合规");
            }

            List<BoxItem> list = boxItemService.list(new QueryWrapper<BoxItem>().eq("is_random", 1).ne("remains", 0));
            List<BoxItem> result = new ArrayList<BoxItem>();
            if (list.isEmpty()) {
                return Result.error("奖品未配置");
            }
            // 总份额
            long sum = list.stream().mapToLong(BoxItem::getShare).sum();
            // 获得龙鳞数
            BigDecimal reward = BigDecimal.valueOf(0);

            // 循环开盒
            for (int i = 0; i < count; i++) {
                // 随机数
                long random = (long) (Math.random() * sum);
                long shard = 0;
                // 次数
                for (BoxItem boxItem : list) {
                    long shardMin = shard;
                    shard += boxItem.getShare();
                    // 随机数落在范围
                    if (shardMin <= random && random < shard) {
                        // 中奖
                        result.add(new BoxItem(boxItem.getName(), boxItem.getImg(), boxItem.getValue()));
                        // 累加中奖次数
                        boxItem.setHit(boxItem.getHit() == null ? 1 : boxItem.getHit() + 1);
                        // 剩余数量
                        boxItem.setRemains(boxItem.getRemains() == -1 ? -1 : boxItem.getRemains() - 1);
                        boxItemService.updateById(boxItem);

                        // 若是积分奖品，则添加积分
                        if (boxItem.getIsPoint() == 1) {
                            CollectPoint one = collectPointService.getOne(new QueryWrapper<CollectPoint>().eq("user_id", userId));
                            if (one == null) {
                                collectPointService.save(new CollectPoint(userId, boxItem.getValue()));
                            } else {
                                BigDecimal localCount = one.getCount();
                                one.setCount(one.getCount().add(boxItem.getValue()));
                                boolean b = collectPointService.update(one, new QueryWrapper<CollectPoint>()
                                        .eq("collect_point_id", one.getCollectPointId())
                                        .eq("count", localCount));
                                if (!b) {
                                    throw new RuntimeException("更新龙鳞数量失败");
                                }
                            }
                            reward = reward.add(boxItem.getValue());
                        }
                        break;
                    }
                }
            }

            // 龙鳞
            if (reward.compareTo(BigDecimal.valueOf(0)) > 0) {
                // 更新龙鳞发行数量
                remainValue = remainValue.subtract(reward);
                if (remainValue.compareTo(BigDecimal.valueOf(0)) < 0) {
                    throw new RuntimeException("龙鳞发行数量不足");
                }
                remain.setValue(String.valueOf(remainValue));
                boolean b = commonInfoService.update(remain, new QueryWrapper<CommonInfo>()
                        .eq("id", remain.getId())
                        .eq("value", localRemain));
                if (!b) {
                    throw new RuntimeException("活动火爆中，请稍后再试");
                }
            }

            // 更新用户剩余盲盒数量
            user.setCount(user.getCount() - count);
            boolean b = update(user, new UpdateWrapper<Box>().
                    eq("box_id", user.getBoxId())
                    .eq("count", user.getCount() + count));
            if (!b) {
                throw new RuntimeException("更新用户剩余盲盒数量失败");
            }

            // 更新记录
            CollectLog collectLog = new CollectLog();
            collectLog.setUserId(userId);
            collectLog.setType(0);
            collectLog.setPlus(reward);
            collectLog.setReduce(count);
            collectLog.setItemName("龙鳞");
            collectLog.setIsSync(0);
            collectLogService.save(collectLog);

            return Result.success().put("data", result);
        } catch (Exception e) {
            log.error("开盒失败", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("开盒失败：" + e.getMessage());
        }
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void resetRemain() {
        // 最大重试次数
        final int MAX_RETRIES = 3;
        // 重试间隔时间（毫秒）
        final long RETRY_DELAY_MS = 5000;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                // 剩余发行数量
                CommonInfo max = commonInfoService.findOne(2003);
                CommonInfo remain = commonInfoService.findOne(2004);
                if (max != null && remain != null) {
                    if (max.getValue() != null && remain.getValue() != null) {
                        remain.setValue(max.getValue());
                        boolean b = commonInfoService.updateById(remain);
                        if (b) {
                            log.info("resetRemain success");
                            return;
                        } else {
                            log.warn("resetRemain failed on attempt {}", attempt);
                        }
                    } else {
                        log.error("max or remain value is null");
                        return;
                    }
                } else {
                    log.error("max or remain is null");
                    return;
                }
            } catch (Exception e) {
                log.error("resetRemain error on attempt {}: {}", attempt, e.getMessage(), e);
            }

            if (attempt < MAX_RETRIES) {
                try {
                    log.info("Retrying resetRemain in {} ms", RETRY_DELAY_MS);
                    TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    log.error("Interrupted during retry wait", ie);
                    Thread.currentThread().interrupt(); // 重新设置中断标志
                }
            }
        }

        log.error("resetRemain failed after {} attempts", MAX_RETRIES);
    }


//    @Scheduled(cron = "0/3 * * * * *")
    @Scheduled(cron = "0/30 * * * * *")
    public void syncUserCollectionJob() {
        // 查询未同步的记录
        List<CollectLog> list = collectLogService.selectSyncCollectLog();
        for (CollectLog collectLog : list) {
            transactionTemplate.execute(status -> {
                try {
                    // 同步用户积分
                    boolean b = dataSync.syncUserCollection(collectLog.getPhone(), collectLog.getPlus(), 1, collectLog.getCollectLogId().toString());
                    if (!b) {
                        log.warn("同步用户积分失败!ID:{}", collectLog.getCollectLogId());
                        boolean update = collectLogService.update(collectLog, new UpdateWrapper<CollectLog>()
                                .eq("collect_log_id", collectLog.getCollectLogId())
                                .eq("is_sync", collectLog.getIsSync()));
                        if (!update) {
                            log.warn("更新用户积分log失败(不存在符合条件的数据，可能已被其他线程更新):{}", collectLog.getCollectLogId());
                            throw new RuntimeException("更新用户积分log失败(不存在符合条件的数据，可能已被其他线程更新)");
                        }
                        return null; // 提前终止事务处理
                    }

                    // 削减短剧平台的积分
                    CollectPoint one = collectPointService.getOne(new QueryWrapper<CollectPoint>().eq("user_id", collectLog.getUserId()));
                    if (one != null) {
                        BigDecimal localCount = one.getCount();
                        one.setCount(localCount.subtract(collectLog.getPlus()));
                        boolean b1 = collectPointService.update(one, new QueryWrapper<CollectPoint>()
                                .eq("collect_point_id", one.getCollectPointId())
                                .eq("count", localCount));
                        if (!b1) {
                            log.warn("更新用户积分失败(不存在符合条件的数据，可能已被其他线程更新):{}", one.getCollectPointId());
                            throw new RuntimeException("更新用户积分失败(不存在符合条件的数据，可能已被其他线程更新)");
                        }
                    } else {
                        log.warn("用户积分不存在:{}", collectLog.getUserId());
                        return null; // 提前终止事务处理
                    }

                    // 更新记录
                    collectLog.setIsSync(1);
                    boolean update = collectLogService.update(collectLog, new UpdateWrapper<CollectLog>()
                            .eq("collect_log_id", collectLog.getCollectLogId())
                            .ne("is_sync", 1));
                    if (!update) {
                        log.warn("更新用户积分log失败(不存在符合条件的数据，可能已被其他线程更新):{}", collectLog.getCollectLogId());
                        throw new RuntimeException("更新用户积分log失败(不存在符合条件的数据，可能已被其他线程更新)");
                    }

                    log.info("同步用户积分成功!ID:{}", collectLog.getCollectLogId());
                    return null; // 提前终止事务处理
                } catch (Exception e) {
                    log.warn("同步用户积分异常", e);
                    status.setRollbackOnly(); // 标记事务回滚
                    return null; // 事务处理结束
                }
            });
        }
    }
}
