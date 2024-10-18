package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.box.dao.BoxDao;
import com.sqx.modules.box.entity.Box;
import com.sqx.modules.box.entity.Collect;
import com.sqx.modules.box.entity.CollectLog;
import com.sqx.modules.box.entity.CollectPoint;
import com.sqx.modules.box.service.BoxService;
import com.sqx.modules.box.service.CollectLogService;
import com.sqx.modules.box.service.CollectPointService;
import com.sqx.modules.box.service.CollectService;
import com.sqx.modules.box.vo.BoxCollection;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
                boxCollection.setCollectMax(Integer.parseInt(remain.getValue()));
            }

            return Result.success().put("data", boxCollection);
        } catch (Exception e) {
            log.error("查询用户盲盒藏品信息失败", e);
            return Result.error("查询失败");
        }
    }
}
