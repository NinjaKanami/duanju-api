package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.RedisUtils;
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
import com.sqx.modules.utils.MD5Util;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 更新在线时间
     *
     * @param userId 用户
     * @param minute 时间
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateOnline(Long userId, int minute, String encrypt) {
        // 当前秒 密钥
        long epochSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        String newStr = MD5Util.encoderByMd5(epochSecond + "online") + ":" + epochSecond;
        try {
            String s = redisUtils.get("online_" + userId);
            if (s != null) {
                return Result.error("请求频繁,请稍后再试!").put("encrypt", newStr);
            }
            redisUtils.set("online_" + userId, userId, 30);

            if (StringUtil.isNullOrEmpty(encrypt)) {
                return Result.error("请求参数错误").put("encrypt", newStr);
            }

            // 验证参数
            String[] split = encrypt.split(":");
            if (split.length != 2) {
                return Result.error("请求参数错误").put("encrypt", newStr);
            }
            boolean checkMD5 = MD5Util.checkMD5(split[1] + "online", split[0]);
            if (!checkMD5) {
                return Result.error("请求参数错误").put("encrypt", newStr);
            }

            // 两分钟过期
            if (epochSecond > Long.parseLong(split[1]) + 120L) {
                return Result.error("请求参数错误").put("encrypt", newStr);
            }

            // 最快请求的时间 默认两倍速  过去的时间+经过的时间
            long l1 = Long.parseLong(split[1]) + (minute * 30L);

            // 判断时间是否合理
            if (l1 > epochSecond) {
                return Result.error("请求时间不合规").put("encrypt", newStr);
            }


            // 最大盲盒
            CommonInfo commonInfo = commonInfoService.findOne(2006);
            int maxReward = Integer.parseInt(commonInfo.getValue());

            // 设置积分范围
            CommonInfo range = commonInfoService.findOne(2010);
            String[] split1 = range.getValue().split("-");
            if (split1.length != 2) {
                log.warn("所需在线时间配置错误，请联系管理人员");
                return Result.error("请求参数错误").put("encrypt", newStr);
            }
            int range1 = Integer.parseInt(split1[0]);
            int range2 = Integer.parseInt(split1[1]);

            // 随机数 range1-range2
            int random = (int) ((Math.random() * (range2 - range1) + 1) + range1);

            // 随机数 5-10
            // int random = (int) ((Math.random() * 6) + 5);

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
                return Result.error("已达上限").put("encrypt", newStr);
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
                return Result.error("未到时间").put("encrypt", newStr);
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

            return Result.success().put("encrypt", newStr);

        } catch (Exception e) {
            log.error("更新在线时间失败:" + e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("更新在线时间失败:" + e).put("encrypt", newStr);
        }
    }
}
