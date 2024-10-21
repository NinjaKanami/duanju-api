package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.box.dao.CollectDao;
import com.sqx.modules.box.entity.Collect;
import com.sqx.modules.box.entity.CollectLog;
import com.sqx.modules.box.entity.CollectPoint;
import com.sqx.modules.box.service.CollectLogService;
import com.sqx.modules.box.service.CollectPointService;
import com.sqx.modules.box.service.CollectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * (Collect)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:34
 */
@Service("collectService")
public class CollectServiceImpl extends ServiceImpl<CollectDao, Collect> implements CollectService {

    @Resource
    private CollectPointService collectPointService;

    @Resource
    private CollectLogService collectLogService;

    /**
     * 合成
     *
     * @param userId 用户Id
     * @param count  数量
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result synthesise(Long userId, int count) {
        try {
            // 合成需要的数量 10
            int need = 10;
            // 判断数量
            if (count <= 0) {
                return Result.error("合成失败，请输入正确的数量！");
            }
            // 判断龙鳞
            CollectPoint collectPoint = collectPointService.getOne(new QueryWrapper<CollectPoint>().eq("user_id", userId));
            if (collectPoint == null || collectPoint.getCount() < count * need) {
                return Result.error("合成失败，您的龙鳞不足！");
            }
            // 更新龙鳞
            collectPoint.setCount((collectPoint.getCount() - count * need));
            collectPointService.updateById(collectPoint);

            // 更新藏品
            Collect collect = getOne(new QueryWrapper<Collect>().eq("user_id", userId));
            if (collect == null) {
                collect = new Collect(userId, 0);
            }
            collect.setCount(collect.getCount() + count);
            save(collect);
            // 更新记录
            CollectLog collectLog = new CollectLog();
            collectLog.setUserId(userId);
            collectLog.setType(1);
            collectLog.setPlus(count);
            collectLog.setReduce(count * need);
            collectLog.setItemName("青龙");
            collectLogService.save(collectLog);
            return Result.success();
        } catch (Exception e) {
            log.error("合成失败！", e);
            return Result.error("系统繁忙，请稍后再试！");
        }
    }
}
