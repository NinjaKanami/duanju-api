package com.sqx.modules.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.*;
import com.sqx.modules.box.service.BoxPointService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.dao.CourseUserDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.course.entity.CourseUser;
import com.sqx.modules.course.service.CourseDetailsService;
import com.sqx.modules.course.service.CourseUserService;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.controller.app.AliPayController;
import com.sqx.modules.pay.service.DyService;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.utils.AliPayOrderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements OrdersService {
    @Autowired
    private BoxPointService boxPointService;
    @Autowired
    private WxService wxService;
    @Autowired
    private AliPayController aliPayController;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseUserDao courseUserDao;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private CourseUserService courseUserService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private VipDetailsService vipDetailsService;
    @Autowired
    private CourseDetailsService courseDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private DyService dyService;
    @Autowired
    private MessageInfoDao messageInfoDao;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);


    @Override
    public Orders selectOrdersByCourseIdAndUserId(Long userId, Long courseId) {
        return baseMapper.selectOrdersByCourseIdAndUserId(userId, courseId);
    }


    @Override
    public Result insertOrders(Orders orders) {
        // 如果订单的种类是短剧
        if (orders.getOrdersType() == 1 || orders.getOrdersType() == 11) {
            // 将短剧加入到我的列表
            CourseUser courseUser = new CourseUser();
            // 设置短剧id
            courseUser.setCourseId(orders.getCourseId());
            courseUser.setCourseDetailsId(orders.getCourseDetailsId());
            if (courseUser.getCourseDetailsId() != null) {
                courseUser.setClassify(2);
            } else {
                courseUser.setClassify(1);
            }
            // 设置用户id
            courseUser.setUserId(orders.getUserId());
            // 设置订单id
            courseUser.setOrderId(orders.getOrdersId());
            // 加入我的列表
            courseUserService.insertCourseUser(courseUser);
            return Result.success("短剧订单处理完成！");
        } else {
            UserEntity userEntity = userService.selectUserById(orders.getUserId());
            userEntity.setAgencyIndex(1);
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.YEAR, 2);
            userEntity.setAgencyEndTime(DateUtils.format(instance.getTime()));
            userEntity.setAgencyStartTime(DateUtils.format(new Date()));
            userService.updateById(userEntity);
        }
        return Result.success("订单处理完成！");
    }

    /**
     * 生成商品订单信息
     *
     * @param courseId
     * @param userId
     * @return
     */
    @Override
    public Result insertCourseOrders(Long courseId, Long courseDetailsId, Long userId) {
        log.info("生成商品订单信息接口入参为：{},{}", courseId, userId);
        reentrantReadWriteLock.writeLock().lock();
        try {
            /*CourseUser courseUser1 = courseUserDao.selectCourseUser(courseId, userId);
            if(courseUser1!=null){
                return Result.error("您已经购买过了，请不要重复点击！");
            }*/
            // 返回的类型
            Map<String, Object> result = new HashMap<>();
            // 查询会员信息
            UserVip userVip = userVipService.selectUserVipByUserId(userId);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 订单模板对象
            Orders orders = new Orders();
            if (courseId != null) {
                // 根据短剧id去查询短剧相关信息 来填充订单模板
                QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("is_delete", 0);
                // 短剧必须是未删除的
                queryWrapper.eq("course_id", courseId);
                Course course = courseDao.selectOne(queryWrapper);
                if (course == null) {
                    return Result.error("系统繁忙，请刷新后重试！");
                }
                // 设置订单编号
                orders.setOrdersNo(AliPayOrderUtil.createOrderId());
                // 设置用户id
                orders.setUserId(userId);
                // 设置短剧id
                orders.setCourseId(courseId);
                // 判断是单集购买还是全集购买
                if (course.getPriceType() == null) {
                    return Result.error("未设置价格种类");
                } else if (course.getPriceType() == 0) {
                    orders.setCourseDetailsId(courseDetailsId);
                    if (courseDetailsId != null) {
                        CourseDetails courseDetails = courseDetailsService.getById(courseDetailsId);
                        orders.setPayMoney(courseDetails.getPrice());
                    } else {
                        orders.setPayMoney(course.getPrice());
                    }
                } else if (course.getPriceType() == 1) {
                    // 设置为全集购买价格
                    orders.setPayMoney(course.getPrice());
                } else {
                    return Result.error("未知价格种类");
                }
                // 设置支付状态
                orders.setStatus(0);
                // 设置订单创建时间
                orders.setCreateTime(df.format(new Date()));

                // 设置订单种类
                if (course.getIsPrice() == 1) {
                    // 短剧
                    orders.setOrdersType(1);
                } else if (course.getIsPrice() == 3) {
                    // 云短剧
                    orders.setOrdersType(11);
                } else {
                    return Result.error("未设置收费品类");
                }

                // 不是会员或会员过期直接生成订单直接生成订单
                int count = baseMapper.insertOrders(orders);
                result.put("flag", 2);
                result.put("orders", orders);
                if (count > 0) {
                    return Result.success("生成订单成功！").put("data", result);
                } else {
                    return Result.error("生成订单失败！");
                }
            } else {
                return Result.error("短剧的id不能为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成商品订单错误！！！" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    /**
     * 生成会员订单
     *
     * @param vipDetailsId 会员详情id
     * @param userId
     * @return
     */
    @Override
    public Result insertVipOrders(Long vipDetailsId, Long userId) {
        VipDetails vipDetails = vipDetailsService.getById(vipDetailsId);
        // 创建订单返回对象
        Orders orders = new Orders();
        // 设置订单编号
        orders.setOrdersNo(AliPayOrderUtil.createOrderId());
        // 设置支付点券
        orders.setPayMoney(vipDetails.getMoney());
        // 设置订单类型
        orders.setOrdersType(2);
        // 设置要开通的会员类型
        orders.setVipNameType(vipDetails.getVipNameType());
        // 设置支付状态
        orders.setStatus(0);
        // 设置用户id
        orders.setUserId(userId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 设置创建时间
        orders.setCreateTime(df.format(new Date()));
        // 插入到订单表中
        baseMapper.insertOrders(orders);
        return Result.success().put("data", orders);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result payMoney(Long ordersId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            Orders orders = baseMapper.selectById(ordersId);
            if (orders == null || !orders.getStatus().equals(0)) {
                return Result.error("订单错误，请刷新后重试！");
            }
            int size = 0;
            if (orders.getOrdersType() == 1 || orders.getOrdersType() == 11) {
                int count = 0;
                if (orders.getCourseDetailsId() != null) {
                    // 单集购买
                    count = courseUserDao.selectCount(new QueryWrapper<CourseUser>()
                            .eq("user_id", orders.getUserId())
                            .eq("classify", 2)
                            .eq("course_id", orders.getCourseId())
                            .eq(orders.getCourseDetailsId() != null, "course_details_id", orders.getCourseDetailsId()));
                    size = 1;
                } else {
                    // 全集购买
                    count = courseUserDao.selectCount(new QueryWrapper<CourseUser>()
                            .eq("user_id", orders.getUserId())
                            .eq("classify", 1)
                            .eq("course_id", orders.getCourseId()));
                    // 总集数
                    size = courseDetailsService.count(new QueryWrapper<CourseDetails>().eq("course_id", orders.getCourseId()));
                }
                if (count > 0) {
                    return Result.error("您已购买，请不要重复点击");
                }
            }
            UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(orders.getUserId());
            if (userMoney.getMoney().doubleValue() < orders.getPayMoney().doubleValue()) {
                return Result.error("账户余额不足！");
            }
            if (userMoney.getMoney().doubleValue() >= orders.getPayMoney().doubleValue()) {
                UserEntity userEntity = userService.selectUserById(orders.getUserId());
                userMoneyService.updateMoney(2, orders.getUserId(), orders.getPayMoney().doubleValue());
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setMoney(orders.getPayMoney());
                userMoneyDetails.setUserId(orders.getUserId());
                userMoneyDetails.setContent("点券支付订单");
                userMoneyDetails.setTitle("下单成功，订单号：" + orders.getOrdersNo());
                userMoneyDetails.setType(2);
                userMoneyDetails.setClassify(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);
                orders.setPayWay(6);
                orders.setStatus(1);
                orders.setPayTime(DateUtils.format(new Date()));

                /*Map map = inviteService.updateInvite(userEntity,orders.getOrdersNo(), orders.getOrdersType(), orders.getPayMoney());
                Object memberUserId = map.get("memberUserId");
                if(memberUserId!=null){
                    orders.setMemberUserId(Long.parseLong(String.valueOf(memberUserId)));
                    orders.setMemberMoney(new BigDecimal(String.valueOf(map.get("memberMoney"))));
                    orders.setMemberType(Integer.parseInt(String.valueOf(map.get("memberType"))));
                }
                Object channelUserId = map.get("channelUserId");
                if(channelUserId!=null){
                    orders.setChannelUserId(Long.parseLong(String.valueOf(channelUserId)));
                    orders.setChannelMoney(new BigDecimal(String.valueOf(map.get("channelMoney"))));
                }
                Object pingMoney = map.get("pingMoney");
                if(pingMoney!=null){
                    orders.setPingMoney(new BigDecimal(String.valueOf(map.get("pingMoney"))));
                }*/
                baseMapper.updateById(orders);
                insertOrders(orders);
                // 获取积分
                // 非云短剧无法获得积分
                if (orders.getOrdersType() == 11) {
                    Result result = boxPointService.getPoints(orders.getUserId(), orders.getCourseId(), size);
                    if (!result.get("code").equals(0)) {
                        throw new Exception(result.get("msg").toString());
                    }
                }
                return Result.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("点券支付订单异常：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙");
    }


    @Override
    public Result payOrdersInviteMoney(Long ordersId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            Orders orders = baseMapper.selectById(ordersId);
            if (orders == null || !orders.getStatus().equals(0)) {
                return Result.error("订单错误，请刷新后重试！");
            }
            if (orders.getOrdersType() == 1 || orders.getOrdersType() == 11) {
                int count = 0;
                if (orders.getCourseDetailsId() != null) {
                    count = courseUserDao.selectCount(new QueryWrapper<CourseUser>()
                            .eq("user_id", orders.getUserId())
                            .eq("classify", 2)
                            .eq("course_id", orders.getCourseId())
                            .eq(orders.getCourseDetailsId() != null, "course_details_id", orders.getCourseDetailsId()));
                } else {
                    count = courseUserDao.selectCount(new QueryWrapper<CourseUser>()
                            .eq("user_id", orders.getUserId())
                            .eq("classify", 1)
                            .eq("course_id", orders.getCourseId()));
                }
                if (count > 0) {
                    return Result.error("您已购买，请不要重复点击");
                }
            }
            InviteMoney inviteMoney = inviteMoneyService.selectInviteMoneyByUserId(orders.getUserId());
            if (inviteMoney.getMoney() < orders.getPayMoney().doubleValue()) {
                return Result.error("余额不足！");
            }
            UserEntity userEntity = userService.selectUserById(orders.getUserId());
            inviteMoneyService.updateInviteMoneySumSub(orders.getPayMoney().doubleValue(), orders.getUserId());
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setMoney(orders.getPayMoney());
            userMoneyDetails.setUserId(orders.getUserId());
            userMoneyDetails.setContent("收益支付订单");
            userMoneyDetails.setTitle("下单成功，订单号：" + orders.getOrdersNo());
            userMoneyDetails.setType(2);
            userMoneyDetails.setClassify(2);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
            userMoneyDetailsService.save(userMoneyDetails);
            orders.setPayWay(6);
            orders.setStatus(1);
            orders.setPayTime(DateUtils.format(new Date()));

            /*Map map = inviteService.updateInvite(userEntity,orders.getOrdersNo(), orders.getOrdersType(), orders.getPayMoney());
            Object memberUserId = map.get("memberUserId");
            if(memberUserId!=null){
                orders.setMemberUserId(Long.parseLong(String.valueOf(memberUserId)));
                orders.setMemberMoney(new BigDecimal(String.valueOf(map.get("memberMoney"))));
                orders.setMemberType(Integer.parseInt(String.valueOf(map.get("memberType"))));
            }
            Object channelUserId = map.get("channelUserId");
            if(channelUserId!=null){
                orders.setChannelUserId(Long.parseLong(String.valueOf(channelUserId)));
                orders.setChannelMoney(new BigDecimal(String.valueOf(map.get("channelMoney"))));
            }
            Object pingMoney = map.get("pingMoney");
            if(pingMoney!=null){
                orders.setPingMoney(new BigDecimal(String.valueOf(map.get("pingMoney"))));
            }*/
            baseMapper.updateById(orders);
            insertOrders(orders);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("点券支付订单异常：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙");
    }

    @Override
    public Result refundOrder(Long ordersId, String refundContent) {
        Orders bean = baseMapper.selectById(ordersId);
        if (!bean.getStatus().equals(1)) {
            return Result.error("订单未支付或已退款！");
        }
        bean.setRefundContent(refundContent);
        if (bean.getPayWay() == 1 || bean.getPayWay() == 2 || bean.getPayWay() == 3) {
            boolean refund = wxService.refund(bean);
            if (!refund) {
                return Result.error("退款失败！");
            }
        } else if (bean.getPayWay() == 4 || bean.getPayWay() == 5) {
            String code = aliPayController.alipayRefund(bean);
            if (StringUtils.isNotBlank(code)) {
                JSONObject jsonObject = JSON.parseObject(code);
                JSONObject alipay_trade_refund_response = jsonObject.getJSONObject("alipay_trade_refund_response");
                String code1 = alipay_trade_refund_response.getString("code");
                if (!"10000".equals(code1)) {
                    return Result.error("退款失败！");
                }
            } else {
                return Result.error("退款失败！");
            }
        } else if (bean.getPayWay() == 7) {
            boolean refund = dyService.refund(bean);
            if (!refund) {
                return Result.error("退款失败！");
            }
        } else {
            userMoneyService.updateMoney(1, bean.getUserId(), bean.getPayMoney().doubleValue());
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setMoney(bean.getPayMoney());
            userMoneyDetails.setUserId(bean.getUserId());
            userMoneyDetails.setContent("订单:" + bean.getOrdersNo());
            userMoneyDetails.setTitle("订单退款：" + bean.getPayMoney());
            userMoneyDetails.setType(1);
            userMoneyDetails.setClassify(1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
            userMoneyDetailsService.save(userMoneyDetails);
        }
        bean.setStatus(2);
        baseMapper.updateById(bean);
        if (bean.getOrdersType() == 1 || bean.getOrdersType() == 11) {
            CourseUser courseUser = courseUserDao.selectOne(new QueryWrapper<CourseUser>().eq("order_id", bean.getOrdersId()));
            if (courseUser != null) {
                courseUserDao.deleteById(courseUser.getCourseUserId());
            }
        } else {
            UserVip userVip = userVipService.selectUserVipByUserId(bean.getUserId());
            if (userVip != null) {
                userVipService.removeById(userVip.getVipId());
            }
            courseUserDao.deleteCourseUserByVipUser(bean.getUserId());
        }
        return Result.success("退款成功！");
    }

    @Override
    public Result selectOrders(Integer page, Integer limit, String ordersNo, Integer status, Long userId, Long courseId,
                               Integer flag, String time, String userName, Integer ordersType, String startTime, String endTime,
                               Long sysUserId, String qdCode, String sysUserName) {
        Page<Orders> pages = new Page<>(page, limit);
        return Result.success().put("data", new PageUtils(baseMapper.selectOrdersByOrdersNo(pages, ordersNo, status, userId, courseId,
                flag, time, userName, ordersType, startTime, endTime, sysUserId, qdCode, sysUserName)));
    }

    @Override
    public Result selectOrderByUserId(Integer page, Integer limit, Long userId) {
        IPage<Orders> orderPage = new Page<>(page, limit);
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.in("orders_type", 1, 11);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");
        IPage<Orders> iPage = baseMapper.selectPage(orderPage, queryWrapper);
        List<Orders> bean = iPage.getRecords();
        if (bean != null && bean.size() > 0) {
            for (int i = 0; bean.size() > i; i++) {
                bean.get(i).setCourse(courseDao.selectById(bean.get(i).getCourseId()));
            }
        }
        return Result.success().put("data", iPage);
    }

    @Override
    public Result selectAllOrderByUserId(Integer page, Integer limit, Long userId) {
        IPage<Orders> orderPage = new Page<>(page, limit);
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");
        IPage<Orders> iPage = baseMapper.selectPage(orderPage, queryWrapper);
        List<Orders> bean = iPage.getRecords();
        if (bean != null && bean.size() > 0) {
            for (int i = 0; bean.size() > i; i++) {
                Orders orders = bean.get(i);
                if (orders.getOrdersType() == 1 || orders.getOrdersType() == 11) {
                    orders.setCourse(courseDao.selectById(bean.get(i).getCourseId()));
                }
            }
        }
        return Result.success().put("data", iPage);
    }

    @Override
    public Result deleteOrders(String ids) {
        String[] split = ids.split(",");
        baseMapper.deleteOrders(split);
        return Result.success();
    }


    @Override
    public Orders selectOrderById(Long orderId) {
        return baseMapper.selectById(orderId);
    }

    @Override
    public Orders selectOrderByOrdersNo(String ordersNo) {
        return baseMapper.selectOne(new QueryWrapper<Orders>().eq("orders_no", ordersNo));
    }

    @Override
    public Double statisticsIncomeMoney(String time, Integer flag, Integer ordersType) {
        return baseMapper.statisticsIncomeMoney(time, flag, ordersType);
    }

    @Override
    public Double statisticsPingMoney(String time, Integer flag, Integer ordersType) {
        return baseMapper.statisticsPingMoney(time, flag, ordersType);
    }


    @Override
    public Result selectOrdersMoneyList(Integer page, Integer limit, Integer flag, String time, Integer ordersType) {
        return Result.success().put("data", baseMapper.selectOrdersMoneyList(new Page<>(page, limit), flag, time, ordersType));
    }


    @Override
    public Integer selectOrdersCount(Integer status, Integer ordersType, Integer flag, String time, Long sysUserId) {
        return baseMapper.selectOrdersCount(status, ordersType, flag, time, sysUserId);
    }

    @Override
    public Double selectOrdersMoney(Integer status, Integer ordersType, Integer flag, String time, Long courseId, Long sysUserId) {
        return baseMapper.selectOrdersMoney(status, ordersType, flag, time, courseId, sysUserId);
    }

    @Override
    public Double selectFenXiaoMoney(Integer type, Long sysUserId, Integer flag, String time) {
        return baseMapper.selectFenXiaoMoney(type, sysUserId, flag, time);
    }

    @Override
    public Integer selectOrdersCountStatisticsByYear(Integer flag, String time, Integer status) {
        return baseMapper.selectOrdersCountStatisticsByYear(flag, time, status);
    }


    /**
     * 定时器 每个月15凌晨进行执行一次
     */
    //@Scheduled(cron="0 0 0 15 * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron="0 */1 * * * ?")
    public void fenXiao() {
        // 需求 1
        // 平台利润的25%（后台配置），平分给推荐人，推荐人每邀请一个剧荐管积为1分，邀请的剧荐管越多，积分越高
        // 推荐人分红每月15号自动发放至对应钱包内
        // 分的利润/总积分*推荐人个人积分=推荐人分红
        // 需求2
        // 平台利润的25%（后台配置），平分给剧荐管
        // 分的利润/剧荐管人数=剧荐管分红


        // 先获取上个月的平台利润
        // Calendar calendar=Calendar.getInstance();
        // calendar.add(Calendar.MONTH,-1);
//        Double pingMoney = baseMapper.selectOrdersPingMoneyByMonth(DateUtils.format(calendar.getTime()));
        CommonInfo commonInfo = commonInfoService.findOne(883);
        String max = commonInfo.getMax();
        String[] split = max.split(",");
        String isOK = split[1];
        Calendar instance = Calendar.getInstance();
        String nowTime = DateUtils.format(instance.getTime(), "dd");
        int nowDay = Integer.parseInt(nowTime);
        int day = Integer.parseInt(split[0]);
        if ("0".equals(isOK) && nowDay == day) {
            commonInfo.setMax(split[0] + ",1");
            commonInfoService.updateBody(commonInfo);
            // 获取当月的修改记录
            MessageInfo messageInfo = messageInfoDao.selectOne(new QueryWrapper<MessageInfo>().eq("state", 20)
                    .like("create_at", DateUtils.format(new Date(), "yyyy-MM")));
            if (messageInfo != null) {
                messageInfo.setIsSee("1");
                messageInfo.setSendTime(DateUtils.format(new Date()));
                messageInfoDao.updateById(messageInfo);
            }


            Double pingMoney = Double.parseDouble(commonInfo.getValue());
            // 判断平台有利润的情况下进行分佣
            if (pingMoney > 0) {
                // 计算分配佣金
                BigDecimal sumMoney = BigDecimal.valueOf(pingMoney).multiply(new BigDecimal(commonInfoService.findOne(866).getValue()));
                // 实现需求 1
                // 获取所有的推荐人
                List<UserEntity> list = userService.list(new QueryWrapper<UserEntity>().eq("status", 1).eq("is_recommend", 1));
                // 获取到所有有推荐人的剧荐管总数
                int i = userService.selectChannelCountAndIsRecommend();
                BigDecimal money = sumMoney.divide(BigDecimal.valueOf(i), 2, BigDecimal.ROUND_DOWN);
                for (UserEntity userEntity : list) {
                    // 获取自己邀请的剧荐管人数
                    int channelCount = userService.selectChannelCountByRecommendUserId(userEntity.getUserId());
                    if (channelCount > 0) {
                        BigDecimal recommendMoney = money.multiply(BigDecimal.valueOf(channelCount));
                        inviteMoneyService.updateInviteMoneySum(recommendMoney.doubleValue(), userEntity.getUserId());
                        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                        userMoneyDetails.setMoney(recommendMoney);
                        userMoneyDetails.setClassify(40);
                        userMoneyDetails.setUserId(userEntity.getUserId());
                        userMoneyDetails.setTitle("推荐人每月分红奖励");
                        userMoneyDetails.setContent("获得奖励：" + recommendMoney);
                        userMoneyDetails.setType(1);
                        userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                        userMoneyDetailsService.save(userMoneyDetails);
                    }
                }


                // 实现需求2
                // 获取所有剧荐管
                list = userService.list(new QueryWrapper<UserEntity>().eq("status", 1).eq("is_channel", 1));
                // 计算总的分红
                sumMoney = BigDecimal.valueOf(pingMoney).multiply(new BigDecimal(commonInfoService.findOne(867).getValue()));
                // 计算每个剧荐管的分红
                money = sumMoney.divide(BigDecimal.valueOf(list.size()), 2, BigDecimal.ROUND_DOWN);
                for (UserEntity userEntity : list) {
                    inviteMoneyService.updateInviteMoneySum(money.doubleValue(), userEntity.getUserId());
                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                    userMoneyDetails.setMoney(money);
                    userMoneyDetails.setClassify(31);
                    userMoneyDetails.setUserId(userEntity.getUserId());
                    userMoneyDetails.setTitle("剧荐管每月分红奖励");
                    userMoneyDetails.setContent("获得奖励：" + money);
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                    userMoneyDetailsService.save(userMoneyDetails);
                }


                // 实现需求3
                // 获取所有剧达人
                list = userService.list(new QueryWrapper<UserEntity>().eq("status", 1).eq("agency_index", 2));
                // 计算总的分红
                sumMoney = BigDecimal.valueOf(pingMoney).multiply(new BigDecimal(commonInfoService.findOne(456).getValue()));
                // 计算每个剧荐管的分红
                money = sumMoney.divide(BigDecimal.valueOf(list.size()), 2, BigDecimal.ROUND_DOWN);
                for (UserEntity userEntity : list) {
                    inviteMoneyService.updateInviteMoneySum(money.doubleValue(), userEntity.getUserId());
                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                    userMoneyDetails.setMoney(money);
                    userMoneyDetails.setClassify(22);
                    userMoneyDetails.setUserId(userEntity.getUserId());
                    userMoneyDetails.setTitle("剧达人每月分红奖励");
                    userMoneyDetails.setContent("获得奖励：" + money);
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                    userMoneyDetailsService.save(userMoneyDetails);
                }

            }

        }

    }

    @Override
    public Result selectAgencyMoneyByTime(UserEntity userEntity, Integer type) {
        // type 1 今天 2昨天 3近七天 4近一个月 5本月 6上月
        // 总收益
        Double sumMoney = 0.0;
        // 订单量
        Integer ordersCount = 0;
        List<Double> moneyList = new ArrayList<>();
        List<String> day = new ArrayList<>();
        List<Integer> ordersCountList = new ArrayList<>();
        if (type == 1) {
            String time = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
            day.add(time);
            Double money = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(time, 21, userEntity.getUserId());
            moneyList.add(money);
            sumMoney = money;
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserId(time, 21, userEntity.getUserId());
            ordersCountList.add(ordersCount);
        } else if (type == 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            day.add(time);
            Double money = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(time, 21, userEntity.getUserId());
            moneyList.add(money);
            sumMoney = money;
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserId(time, 21, userEntity.getUserId());
            ordersCountList.add(ordersCount);
        } else if (type == 3) {
            Calendar calendar = Calendar.getInstance();
            String endTime = DateUtils.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -6);
            String startTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 21, userEntity.getUserId());
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserIdTime(startTime, endTime, 21, userEntity.getUserId());
            for (int i = 6; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);
                moneyList.add(money);
                sumMoney += money;
                int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, 21, userEntity.getUserId());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, 21, userEntity.getUserId());
            ordersCountList.add(ordersCounts);
        } else if (type == 4) {
            Calendar calendar = Calendar.getInstance();
            String endTime = DateUtils.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            String startTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 21, userEntity.getUserId());
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserIdTime(startTime, endTime, 21, userEntity.getUserId());
            for (int i = 30; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);
                moneyList.add(money);
                sumMoney += money;
                int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, 21, userEntity.getUserId());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, 21, userEntity.getUserId());
            ordersCountList.add(ordersCounts);
        } else if (type == 5) {
            // 获取本月的第一天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startTime = DateUtils.format(calendar.getTime());
            // 获取本月的最后一天
            calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); // 下个月的第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为下个月的第一天
            calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
            String endTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 21, userEntity.getUserId());
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserIdTime(startTime, endTime, 21, userEntity.getUserId());
            YearMonth yearMonth = YearMonth.now(); // 获取当前年月
            int daysInMonth = yearMonth.lengthOfMonth(); // 获取本月的总天数
            daysInMonth--;
            for (int i = daysInMonth; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);
                moneyList.add(money);
                sumMoney += money;
                int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, 21, userEntity.getUserId());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, 21, userEntity.getUserId());
            ordersCountList.add(ordersCounts);
        } else if (type == 6) {
            // 获取本月的第一天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 上个月的第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startTime = DateUtils.format(calendar.getTime());
            // 获取本月的最后一天
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为本个月的第一天
            calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
            String endTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 21, userEntity.getUserId());
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserIdTime(startTime, endTime, 21, userEntity.getUserId());
            YearMonth yearMonth = YearMonth.now(); // 获取当前年月
            // 获取上个月的YearMonth对象
            YearMonth previousMonth = yearMonth.minusMonths(1);
            int daysInMonth = previousMonth.lengthOfMonth(); // 获取本月的总天数
            daysInMonth--;
            for (int i = daysInMonth; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);
                moneyList.add(money);
                sumMoney += money;
                int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, 21, userEntity.getUserId());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, 21, userEntity.getUserId());
            ordersCountList.add(ordersCounts);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("dayList", day);
        result.put("moneyList", moneyList);
        result.put("ordersCountList", ordersCountList);
        result.put("sumMoney", sumMoney);
        result.put("ordersCount", ordersCount);
        return Result.success().put("data", result);
    }

    @Override
    public Result selectCourseMoneyByTime(UserEntity userEntity, Integer type) {
        Integer classify = 20;
        if (userEntity.getAgencyIndex() != null && userEntity.getAgencyIndex() == 1) {
            classify = 10;
        }
        // type 1 今天 2昨天 3近七天 4近一个月 5本月 6上月
        // 总收益
        Double sumMoney = 0.0;
        // 订单量
        Integer ordersCount = 0;
        List<Double> moneyList = new ArrayList<>();
        List<String> day = new ArrayList<>();
        List<Integer> ordersCountList = new ArrayList<>();
        if (type == 1) {
            String time = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
            day.add(time);
            Double money = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(time, classify, userEntity.getUserId());
            moneyList.add(money);
            sumMoney = money;
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserId(time, classify, userEntity.getUserId());
            ordersCountList.add(ordersCount);
        } else if (type == 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            day.add(time);
            Double money = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(time, classify, userEntity.getUserId());
            moneyList.add(money);
            sumMoney = money;
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserId(time, classify, userEntity.getUserId());
            ordersCountList.add(ordersCount);
        } else if (type == 3) {
            Calendar calendar = Calendar.getInstance();
            String endTime = DateUtils.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -6);
            String startTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, classify, userEntity.getUserId());
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserIdTime(startTime, endTime, classify, userEntity.getUserId());
            for (int i = 6; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);
                moneyList.add(money);
                sumMoney += money;
                int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, classify, userEntity.getUserId());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, classify, userEntity.getUserId());
            ordersCountList.add(ordersCounts);
        } else if (type == 4) {
            Calendar calendar = Calendar.getInstance();
            String endTime = DateUtils.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            String startTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, classify, userEntity.getUserId());
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserIdTime(startTime, endTime, classify, userEntity.getUserId());
            for (int i = 30; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);
                moneyList.add(money);
                sumMoney += money;
                int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, classify, userEntity.getUserId());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, classify, userEntity.getUserId());
            ordersCountList.add(ordersCounts);
        } else if (type == 5) {
            // 获取本月的第一天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startTime = DateUtils.format(calendar.getTime());
            // 获取本月的最后一天
            calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); // 下个月的第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为下个月的第一天
            calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
            String endTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, classify, userEntity.getUserId());
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserIdTime(startTime, endTime, classify, userEntity.getUserId());
            YearMonth yearMonth = YearMonth.now(); // 获取当前年月
            int daysInMonth = yearMonth.lengthOfMonth(); // 获取本月的总天数
            daysInMonth--;
            for (int i = daysInMonth; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);
                moneyList.add(money);
                sumMoney += money;
                int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, classify, userEntity.getUserId());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, classify, userEntity.getUserId());
            ordersCountList.add(ordersCounts);
        } else if (type == 6) {
            // 获取本月的第一天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 上个月的第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startTime = DateUtils.format(calendar.getTime());
            // 获取本月的最后一天
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为本个月的第一天
            calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
            String endTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, classify, userEntity.getUserId());
            ordersCount = userMoneyDetailsService.selectCountByClassifyAndUserIdTime(startTime, endTime, classify, userEntity.getUserId());
            YearMonth yearMonth = YearMonth.now(); // 获取当前年月
            // 获取上个月的YearMonth对象
            YearMonth previousMonth = yearMonth.minusMonths(1);
            int daysInMonth = previousMonth.lengthOfMonth(); // 获取本月的总天数
            for (int i = daysInMonth; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);
                moneyList.add(money);
                sumMoney += money;
                int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, classify, userEntity.getUserId());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            int ordersCounts = userMoneyDetailsService.selectCountByClassifyAndUserId(time, classify, userEntity.getUserId());
            ordersCountList.add(ordersCounts);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("dayList", day);
        result.put("moneyList", moneyList);
        result.put("ordersCountList", ordersCountList);
        result.put("sumMoney", sumMoney);
        result.put("ordersCount", ordersCount);
        return Result.success().put("data", result);
    }

    @Override
    public Result selectChannelMoneyByTime(UserEntity userEntity, Integer type) {
        // type 1 今天 2昨天 3近七天 4近一个月 5本月 6上月
        // 总收益
        Double sumMoney = 0.0;
        // 购买达人收益
        Double agencyMoney = 0.0;
        // 联盟分红
        Double userMoney = 0.0;
        // 剧达人
        int twoAgencyCount = 0;
        // 梵会员
        int oneAgencyCount = 0;
        // 订单总数
        int ordersCount = 0;
        List<Double> moneyList = new ArrayList<>();
        List<String> day = new ArrayList<>();
        List<Integer> ordersCountList = new ArrayList<>();
        if (type == 1) {
            String time = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
            day.add(time);
            sumMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(time, 31, userEntity.getUserId());
            // 联盟分红
            userMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(time, 30, userEntity.getUserId());
            sumMoney += userMoney;
            moneyList.add(sumMoney);
            // 购买达人
            agencyMoney = baseMapper.selectOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
            ordersCount = baseMapper.selectCountOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
            ordersCountList.add(ordersCount);
            oneAgencyCount = userService.selectAgencyByQdCodeTime(2, userEntity.getChannelCode(), time);
            twoAgencyCount = userService.selectAgencyByQdCodeTime(3, userEntity.getChannelCode(), time);
        } else if (type == 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            day.add(time);
            sumMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(time, 31, userEntity.getUserId());
            // 联盟分红
            userMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(time, 30, userEntity.getUserId());
            sumMoney += userMoney;
            moneyList.add(sumMoney);
            // 购买达人
            agencyMoney = baseMapper.selectOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
            ordersCount = baseMapper.selectCountOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
            ordersCountList.add(ordersCount);
            oneAgencyCount = userService.selectAgencyByQdCodeTime(2, userEntity.getChannelCode(), time);
            twoAgencyCount = userService.selectAgencyByQdCodeTime(3, userEntity.getChannelCode(), time);
        } else if (type == 3) {
            Calendar calendar = Calendar.getInstance();
            String endTime = DateUtils.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -6);
            String startTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 31, userEntity.getUserId());
            // 联盟分红
            List<Map<String, String>> userMoneyLists = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 30, userEntity.getUserId());
            // 购买达人
            agencyMoney = baseMapper.selectOrdersAgencyMoneyByTimes(startTime, endTime, userEntity.getChannelCode());
            ordersCount = baseMapper.selectCountOrdersAgencyMoneyByTimes(startTime, endTime, userEntity.getChannelCode());
            oneAgencyCount = userService.selectAgencyByQdCodeTimes(2, userEntity.getChannelCode(), startTime, endTime);
            twoAgencyCount = userService.selectAgencyByQdCodeTimes(3, userEntity.getChannelCode(), startTime, endTime);
            for (int i = 6; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);

                Double moneys = 0.00;
                if (userMoneyLists != null) {
                    Optional<String> moneyOptional = userMoneyLists.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        moneys = Double.parseDouble(moneyOptional.get());
                    }
                }
                money += moneys;
                moneyList.add(money);
                sumMoney += money;
                userMoney += moneys;
                int ordersCounts = baseMapper.selectCountOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            Double moneys = 0.00;
            if (userMoneyLists != null) {
                Optional<String> moneyOptional = userMoneyLists.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    moneys = Double.parseDouble(moneyOptional.get());
                }
            }
            money += moneys;
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            userMoney += moneys;
            int ordersCounts = baseMapper.selectCountOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
            ordersCountList.add(ordersCounts);
        } else if (type == 4) {
            Calendar calendar = Calendar.getInstance();
            String endTime = DateUtils.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            String startTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 31, userEntity.getUserId());
            // 联盟分红
            List<Map<String, String>> userMoneyLists = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 30, userEntity.getUserId());
            // 购买达人
            agencyMoney = baseMapper.selectOrdersAgencyMoneyByTimes(startTime, endTime, userEntity.getChannelCode());
            ordersCount = baseMapper.selectCountOrdersAgencyMoneyByTimes(startTime, endTime, userEntity.getChannelCode());
            oneAgencyCount = userService.selectAgencyByQdCodeTimes(2, userEntity.getChannelCode(), startTime, endTime);
            twoAgencyCount = userService.selectAgencyByQdCodeTimes(3, userEntity.getChannelCode(), startTime, endTime);
            for (int i = 30; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);

                Double moneys = 0.00;
                if (userMoneyLists != null) {
                    Optional<String> moneyOptional = userMoneyLists.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        moneys = Double.parseDouble(moneyOptional.get());
                    }
                }
                money += moneys;
                moneyList.add(money);
                sumMoney += money;
                userMoney += moneys;
                int ordersCounts = baseMapper.selectCountOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            Double moneys = 0.00;
            if (userMoneyLists != null) {
                Optional<String> moneyOptional = userMoneyLists.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    moneys = Double.parseDouble(moneyOptional.get());
                }
            }
            money += moneys;
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            userMoney += moneys;
            int ordersCounts = baseMapper.selectCountOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
            ordersCountList.add(ordersCounts);
        } else if (type == 5) {
            // 获取本月的第一天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startTime = DateUtils.format(calendar.getTime());
            // 获取本月的最后一天
            calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); // 下个月的第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为下个月的第一天
            calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
            String endTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 31, userEntity.getUserId());
            // 联盟分红
            List<Map<String, String>> userMoneyLists = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 30, userEntity.getUserId());
            // 购买达人
            agencyMoney = baseMapper.selectOrdersAgencyMoneyByTimes(startTime, endTime, userEntity.getChannelCode());
            ordersCount = baseMapper.selectCountOrdersAgencyMoneyByTimes(startTime, endTime, userEntity.getChannelCode());
            oneAgencyCount = userService.selectAgencyByQdCodeTimes(2, userEntity.getChannelCode(), startTime, endTime);
            twoAgencyCount = userService.selectAgencyByQdCodeTimes(3, userEntity.getChannelCode(), startTime, endTime);
            YearMonth yearMonth = YearMonth.now(); // 获取当前年月
            int daysInMonth = yearMonth.lengthOfMonth(); // 获取本月的总天数
            daysInMonth--;
            for (int i = daysInMonth; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);

                Double moneys = 0.00;
                if (userMoneyLists != null) {
                    Optional<String> moneyOptional = userMoneyLists.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        moneys = Double.parseDouble(moneyOptional.get());
                    }
                }
                money += moneys;
                moneyList.add(money);
                sumMoney += money;
                userMoney += moneys;
                int ordersCounts = baseMapper.selectCountOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            Double moneys = 0.00;
            if (userMoneyLists != null) {
                Optional<String> moneyOptional = userMoneyLists.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    moneys = Double.parseDouble(moneyOptional.get());
                }
            }
            money += moneys;
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            userMoney += moneys;
            int ordersCounts = baseMapper.selectCountOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
            ordersCountList.add(ordersCounts);
        } else if (type == 6) {
            // 获取本月的第一天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 上个月的第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startTime = DateUtils.format(calendar.getTime());
            // 获取本月的最后一天
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为本个月的第一天
            calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
            String endTime = DateUtils.format(calendar.getTime());
            List<Map<String, String>> userMoneyList = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 31, userEntity.getUserId());
            // 联盟分红
            List<Map<String, String>> userMoneyLists = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTime(startTime, endTime, 30, userEntity.getUserId());
            // 购买达人
            agencyMoney = baseMapper.selectOrdersAgencyMoneyByTimes(startTime, endTime, userEntity.getChannelCode());
            ordersCount = baseMapper.selectCountOrdersAgencyMoneyByTimes(startTime, endTime, userEntity.getChannelCode());
            oneAgencyCount = userService.selectAgencyByQdCodeTimes(2, userEntity.getChannelCode(), startTime, endTime);
            twoAgencyCount = userService.selectAgencyByQdCodeTimes(3, userEntity.getChannelCode(), startTime, endTime);
            YearMonth yearMonth = YearMonth.now(); // 获取当前年月
            // 获取上个月的YearMonth对象
            YearMonth previousMonth = yearMonth.minusMonths(1);
            int daysInMonth = previousMonth.lengthOfMonth(); // 获取本月的总天数
            daysInMonth--;
            for (int i = daysInMonth; i > 0; i--) {
                calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                Double money = 0.00;
                if (userMoneyList != null) {
                    Optional<String> moneyOptional = userMoneyList.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        money = Double.parseDouble(moneyOptional.get());
                    }
                }
                day.add(time);

                Double moneys = 0.00;
                if (userMoneyLists != null) {
                    Optional<String> moneyOptional = userMoneyLists.stream()
                            .filter(map -> map.get("dateTime").equals(time))
                            .map(map -> {
                                BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                                return m.toString(); // 调用toString方法将BigDecimal转换为String  
                            })
                            .findFirst();
                    if (moneyOptional.isPresent()) {
                        moneys = Double.parseDouble(moneyOptional.get());
                    }
                }
                money += moneys;
                moneyList.add(money);
                sumMoney += money;
                userMoney += moneys;
                int ordersCounts = baseMapper.selectCountOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
                ordersCountList.add(ordersCounts);
            }
            calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.stringToDate(endTime, DateUtils.DATE_TIME_PATTERN));
            String time = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            Double money = 0.00;
            if (userMoneyList != null) {
                Optional<String> moneyOptional = userMoneyList.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    money = Double.parseDouble(moneyOptional.get());
                }
            }
            Double moneys = 0.00;
            if (userMoneyLists != null) {
                Optional<String> moneyOptional = userMoneyLists.stream()
                        .filter(map -> map.get("dateTime").equals(time))
                        .map(map -> {
                            BigDecimal m = new BigDecimal(map.get("money")); // 显式转换或确保它是BigDecimal
                            return m.toString(); // 调用toString方法将BigDecimal转换为String
                        })
                        .findFirst();
                if (moneyOptional.isPresent()) {
                    moneys = Double.parseDouble(moneyOptional.get());
                }
            }
            money += moneys;
            day.add(time);
            moneyList.add(money);
            sumMoney += money;
            userMoney += moneys;
            int ordersCounts = baseMapper.selectCountOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
            ordersCountList.add(ordersCounts);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("dayList", day);
        result.put("moneyList", moneyList);
        result.put("ordersCountList", ordersCountList);
        result.put("sumMoney", sumMoney);
        result.put("userMoney", userMoney);
        result.put("agencyMoney", agencyMoney);
        result.put("ordersCount", ordersCount);
        result.put("oneAgencyCount", oneAgencyCount);
        result.put("twoAgencyCount", twoAgencyCount);
        return Result.success().put("data", result);
    }

    @Override
    public Result selectChannelMoneyByDatetime(UserEntity userEntity, String time) {
        Double userMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(time, 30, userEntity.getUserId());
        // 购买达人
        Double agencyMoney = baseMapper.selectOrdersAgencyMoneyByTime(time, userEntity.getChannelCode());
        Map<String, Object> result = new HashMap<>();
        result.put("userMoney", userMoney);
        result.put("agencyMoney", agencyMoney);
        return Result.success().put("data", result);
    }


}
