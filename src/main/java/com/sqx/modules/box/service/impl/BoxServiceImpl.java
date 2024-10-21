package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.box.dao.BoxDao;
import com.sqx.modules.box.entity.*;
import com.sqx.modules.box.service.*;
import com.sqx.modules.box.vo.BoxCollection;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (Box)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:36
 */
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

    /**
     * 查询 盲盒 青龙 龙鳞 记录 最大发行 剩余数量
     *
     * @param userId 用户ID
     * @return 盲盒 青龙 龙鳞 记录 最大发行 剩余数量
     */
    @Override
    public Result selectBoxCollection(Long userId) {
        try {
            BoxCollection boxCollection = new BoxCollection();

            // 盲盒数量
            List<Box> boxList = this.list(new QueryWrapper<Box>().eq("user_id", userId));
            if (boxList != null) {
                int sum = boxList.stream()
                        .mapToInt(Box::getCount)
                        .sum();
                boxCollection.setBox(sum);
            }

            // 获得龙鳞数量
            CollectPoint collectPoint = collectPointService.getOne(new QueryWrapper<CollectPoint>().eq("user_id", userId));
            if (collectPoint != null) {
                boxCollection.setCollectPoint(collectPoint.getCount());
            }

            // 获得青龙数量
            Collect collect = collectService.getOne(new QueryWrapper<Collect>().eq("user_id", userId));
            if (collect != null) {
                boxCollection.setCollect(collect.getCount());
            }

            // 日志列表
            boxCollection.setCollectLogs(collectLogService.list(new QueryWrapper<CollectLog>().eq("user_id", userId)));

            // 最大发行数量
            CommonInfo max = commonInfoService.findOne(2003);
            if (max != null) {
                boxCollection.setCollectMax(Integer.parseInt(max.getValue()));
            }
            // 剩余发行数量
            CommonInfo remain = commonInfoService.findOne(2004);
            if (remain != null) {
                boxCollection.setCollectRemain(Integer.parseInt(remain.getValue()));
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
            int reward = 0;

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
                    if (shardMin < random && random < shard) {
                        // 中奖
                        result.add(new BoxItem(boxItem.getName(), boxItem.getImg()));
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
                                one.setCount(one.getCount() + boxItem.getValue());
                                collectPointService.updateById(one);
                            }
                            reward += boxItem.getValue();
                        }
                        break;
                    }
                }
            }
            // 更新用户剩余盲盒数量
            user.setCount(user.getCount() - count);
            updateById(user);
            // 更新记录
            CollectLog collectLog = new CollectLog();
            collectLog.setUserId(userId);
            collectLog.setType(0);
            collectLog.setPlus(reward);
            collectLog.setReduce(count);
            collectLog.setItemName("龙鳞");
            collectLogService.save(collectLog);

            return Result.success().put("data", result);
        } catch (Exception e) {
            log.error("开盒失败", e);
            return Result.error("开盒失败");
        }
    }
}
