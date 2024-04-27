package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.entity.UserVip;
import com.sqx.modules.app.response.HomeMessageResponse;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.dao.InviteMoneyDao;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.pay.dao.CashOutDao;
import com.sqx.modules.pay.service.PayDetailsService;
import com.sqx.modules.sys.entity.SysUserEntity;
import com.sqx.modules.sys.service.SysUserService;
import com.sqx.modules.utils.EasyPoi.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fang
 * @date 2020/7/30
 */
@RestController
@Api(value = "用户管理", tags = {"用户管理"})
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CashOutDao cashOutDao;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private InviteMoneyDao inviteMoneyDao;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private OrdersDao ordersDao;

    @RequestMapping(value = "/selectUserByInvitationCode", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserByInvitationCode(String invitationCode) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByInvitationCode(invitationCode);
        Long userId=userEntity.getUserId();
        //查询用户钱包
//        Double money = cashOutDao.selectMayMoney(userId);
        InviteMoney inviteMoney = inviteMoneyService.selectInviteMoneyByUserId(userId);
        Double money = inviteMoney.getMoney();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = cashOutDao.selectCashOutSum(userId,date);
        //查询邀请人数
        int count = userService.queryInviterCount(userEntity.getInvitationCode());
        UserVip userVip = userVipService.selectUserVipByUserId(userId);
        if(userVip!=null){
            userEntity.setMember(userVip.getIsVip());
            userEntity.setEndTime(userVip.getEndTime());
            userEntity.setVipType(userVip.getVipType());
        }
        map.put("userEntity", userEntity);
        map.put("money", money);
        map.put("consume", consume);
        map.put("income", income);
        map.put("count", count);
        return Result.success().put("data", map);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@ApiParam("用户id") @PathVariable Long userId) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByUserId(userId);
        //查询用户钱包
//        Double money = cashOutDao.selectMayMoney(userId);
        InviteMoney inviteMoney = inviteMoneyService.selectInviteMoneyByUserId(userId);
        Double money = inviteMoney.getMoney();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = cashOutDao.selectCashOutSum(userId,date);
        //查询邀请人数
        int count = userService.queryInviterCount(userEntity.getInvitationCode());
        UserVip userVip = userVipService.selectUserVipByUserId(userId);
        if(userVip!=null){
            userEntity.setMember(userVip.getIsVip());
            userEntity.setEndTime(userVip.getEndTime());
            userEntity.setVipType(userVip.getVipType());
        }
        map.put("userEntity", userEntity);
        map.put("money", money);
        map.put("consume", consume);
        map.put("income", income);
        map.put("count", count);
        return Result.success().put("data", map);
    }

    @RequestMapping(value = "/selectUserList", method = RequestMethod.GET)
    @ApiOperation("查询所有用户列表")
    @ResponseBody
    public Result selectUserList(Integer page, Integer limit,String phone,Integer sex,String platform,
                                 String sysPhone,Integer status, Integer member, String inviterCode,
                                 String userName, String invitationCode, String startTime, String endTime,
                                 String qdCode,String sysUserName,Integer vipType,String isRecommend,String isChannel,
                                 String agencyIndex) {
        return Result.success().put("data", userService.selectUserPage(page, limit, phone, sex, platform, sysPhone, status, member,
                inviterCode, userName, invitationCode, startTime, endTime,qdCode,sysUserName,vipType,isRecommend,isChannel,agencyIndex));
    }

    @GetMapping("/userListExcel")
    public void userListExcel(UserEntity userEntity, String startTime, String endTime, HttpServletResponse response) throws IOException {
        List<UserEntity> list = userService.userListExcel(startTime, endTime, userEntity);
        ExcelUtils.exportExcel(list, "用户表", "用户Sheet", UserEntity.class, "用户表", response);
    }


    @RequestMapping(value = "/deleteUserByUserId/{userId}", method = RequestMethod.POST)
    @ApiOperation("删除用户")
    @ResponseBody
    public Result deleteUserByUserId(@PathVariable("userId") Long userId) {
        userService.removeById(userId);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserByUserId", method = RequestMethod.POST)
    @ApiOperation("修改用户")
    @ResponseBody
    public Result updateUserByUserId(@RequestBody UserEntity userEntity) {
        if(StringUtils.isNotEmpty(userEntity.getPhone())){
            UserEntity phoneUser = userService.queryByPhone(userEntity.getPhone());
            if(phoneUser!=null && !phoneUser.getUserId().equals(userEntity.getUserId())){
                return Result.error("手机号已被其他用户绑定！");
            }
        }
        if(StringUtils.isNotEmpty(userEntity.getQdCode())){
            SysUserEntity sysUserEntity = sysUserService.getOne(new QueryWrapper<SysUserEntity>().eq("qd_code", userEntity.getQdCode()));
            if(sysUserEntity==null){
                return Result.error("渠道码不正确！");
            }
        }
        userService.updateById(userEntity);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserStatusByUserId", method = RequestMethod.GET)
    @ApiOperation("禁用或启用用户")
    @ResponseBody
    public Result updateUserByUserId(Long userId) {
        UserEntity byId = userService.getById(userId);
        if (byId.getStatus().equals(1)) {
            byId.setStatus(2);
        } else {
            byId.setStatus(1);
        }
        userService.updateById(byId);
        return Result.success();
    }


    /**
     * 获取openid
     *
     * @param code 微信code
     * @return openid
     */
    @GetMapping("/openId/{code:.+}/{userId}")
    @ApiOperation("根据code获取openid")
    public Result getOpenid(@PathVariable("code") String code, @PathVariable("userId") Long userId) {
        return userService.getOpenId(code, userId);
    }

    /**
     * 信息分析
     *
     * @return
     */
    @GetMapping("/homeMessage")
    @ApiOperation("信息分析")
    public Result homeMessage(Long sysUserId) {
        String qdCode=null;
        if(sysUserId!=null){
            qdCode=sysUserService.getById(sysUserId).getQdCode();
        }
        HomeMessageResponse homeMessageResponse = new HomeMessageResponse();
        //   0查总   1查天  2查月  3查年
        //设置总用户人数
        homeMessageResponse.setTotalUsers(userService.queryUserCount(0, null,null,qdCode));
        //设置今日新增
        homeMessageResponse.setNewToday(userService.queryUserCount(1, null,null,qdCode));
        //设置本月新增
        homeMessageResponse.setNewMonth(userService.queryUserCount(2, null,null,qdCode));
        //设置本年新增
        homeMessageResponse.setNewYear(userService.queryUserCount(3, null,null,qdCode));
        //设置总收入
        homeMessageResponse.setTotalRevenue(userService.queryPayMoney(0,qdCode));
        //设置今日收入
        homeMessageResponse.setTodayRevenue(userService.queryPayMoney(1,qdCode));
        //设置本月收入
        homeMessageResponse.setMonthRevenue(userService.queryPayMoney(2,qdCode));
        //设置本年收入
        homeMessageResponse.setYearRevenue(userService.queryPayMoney(3,qdCode));
        //查询指定日期下的短剧购买的 量
        return Result.success().put("data", homeMessageResponse);
    }

    /**
     * 短剧分析
     *
     * @return
     */
    @GetMapping("/courseMessage")
    @ApiOperation("短剧分析")
    public Result courseMessage(Long page, Long limit, String date, int type,Long sysUserId) {
        Page<Map<String, Object>> iPage = new Page<>(page, limit);
        IPage<Map<String, Object>> mapIPage = userService.queryCourseOrder(iPage, type, date,sysUserId);
        return Result.success().put("data", new PageUtils(mapIPage));
    }

    /**
     * 用户分析
     */
    @GetMapping("/userMessage")
    @ApiOperation("用户分析")
    public Result userMessage(String date, int type,Long sysUserId) {
        String qdCode=null;
        if(sysUserId!=null){
            qdCode=sysUserService.getById(sysUserId).getQdCode();
        }
        int sumUserCount = userService.queryUserCount(type, date,null,qdCode);
        int h5Count = userService.queryUserCount(type, date,"h5",qdCode);
        int appCount = userService.queryUserCount(type, date,"app",qdCode);
        int wxCount = userService.queryUserCount(type, date,"小程序",qdCode);
        int dyCount = userService.queryUserCount(type, date,"抖音",qdCode);
        int giveMemberCount = userService.userMessage(date, type,qdCode,1);
        int moneyMemberCount = userService.userMessage(date, type,qdCode,2);
        int memberCount = userService.userMessage(date, type,qdCode,null);
        int userCount = sumUserCount-memberCount;
        Map<String,Integer> result=new HashMap<>();
        result.put("sumUserCount",sumUserCount);
        result.put("h5Count",h5Count);
        result.put("appCount",appCount);
        result.put("wxCount",wxCount);
        result.put("dyCount",dyCount);
        result.put("memberCount",memberCount);
        result.put("giveMemberCount",giveMemberCount);
        result.put("moneyMemberCount",moneyMemberCount);
        result.put("userCount",userCount);
        return Result.success().put("data", result);
    }


    @PostMapping("addCannotMoney/{userId}/{money}")
    @ApiOperation("添加点券")
    public Result addCannotMoney(@PathVariable("userId") Long userId, @PathVariable("money") Double money) {
        userMoneyService.updateMoney(1, userId, money);
        //inviteMoneyDao.updateInviteMoneySum(money,userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setTitle("[增加点券]平台增加点券：" + money);
        userMoneyDetails.setContent("[增加点券]平台增加点券：" + money);
        userMoneyDetails.setType(1);
        userMoneyDetails.setClassify(1);
        userMoneyDetails.setMoney(new BigDecimal(money));
        userMoneyDetails.setCreateTime(sdf.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        return Result.success();
    }

    @PostMapping("subCannotMoney/{userId}/{money}")
    @ApiOperation("减少点券")
    public Result subCannotMoney(@PathVariable("userId") Long userId, @PathVariable("money") Double money) {
        userMoneyService.updateMoney(2, userId, money);
        //inviteMoneyDao.updateInviteMoneySumSub(money,userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setTitle("[减少点券]平台减少点券：" + money);
        userMoneyDetails.setContent("平台减少点券：" + money);
        userMoneyDetails.setType(1);
        userMoneyDetails.setClassify(1);
        userMoneyDetails.setMoney(new BigDecimal(money));
        userMoneyDetails.setCreateTime(sdf.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        return Result.success();
    }

    @PostMapping("/updateSysUserMoney")
    @ApiOperation("修改点券")
    public Result updateSysUserMoney(Long userId, Double money,Integer type) {
        userMoneyService.updateSysMoney(type, userId, money);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setSysUserId(userId);
        if(type==1){
            userMoneyDetails.setTitle("[增加点券]平台增加点券：" + money);
            userMoneyDetails.setContent("[增加点券]平台增加点券：" + money);
        }else{
            userMoneyDetails.setTitle("[减少点券]平台减少点券：" + money);
            userMoneyDetails.setContent("[减少点券]平台减少点券：" + money);
        }
        userMoneyDetails.setType(type);
        userMoneyDetails.setClassify(1);
        userMoneyDetails.setMoney(new BigDecimal(money));
        userMoneyDetails.setCreateTime(sdf.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        return Result.success();
    }

    @GetMapping("/selectInviteUserList")
    @ApiOperation("邀请用户排行榜")
    public Result selectInviteUserList(Integer page,Integer limit,String phone,String userName){
        return userService.selectInviteUserList(page, limit, userName, phone);
    }

    @GetMapping("/selectUserOnLineCount")
    @ApiOperation("统计当前在线人数")
    public Result selectUserCount(Long sysUserId){
        String qdCode=null;
        if(sysUserId!=null){
            qdCode=sysUserService.getById(sysUserId).getQdCode();
        }
        return userService.selectUserOnLineCount(qdCode);
    }

    @PostMapping("/insertUserRole")
    @ApiOperation("设置用户角色(剧荐管或推荐人)")
    public Result insertUserRole(@RequestBody UserEntity userEntity){
        return userService.insertUserRole(userEntity);
    }

    @GetMapping("/selectUserCountStatisticsByTime")
    @ApiOperation("用户统计")
    public Result selectUserCountStatisticsByTime(String startTime,String endTime){
        List<Integer> userCountList=new ArrayList<>();
        List<String> year=new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(parse);
        while (true){
            String dateTime = simpleDateFormat.format(calendar.getTime());
            int i = userService.queryUserCount(1, dateTime,null,null);
            userCountList.add(i);
            year.add(dateTime);
            if(dateTime.equals(endTime)){
                break;
            }
            calendar.add(Calendar.DATE,1);
        }
        Map<String,Object> result=new HashMap<>();
        result.put("userCountList",userCountList);
        result.put("year",year);
        return Result.success().put("data",result);
    }



    @GetMapping("/selectChannelUserListByRecommend")
    @ApiOperation("推荐人查询自己的剧荐管")
    public Result selectChannelUserListByRecommend(Long userId, Integer page, Integer limit, String userName, String phone){
        UserEntity userEntity = userService.selectUserById(userId);
        return userService.selectChannelUserListByRecommend(page,limit,userEntity.getUserId(),userName,phone);
    }


    @GetMapping("/selectRecommendMoneyByMonth")
    @ApiOperation("推荐人查询收益")
    public Result selectRecommendMoneyByMonth(Long userId){
        //获取本月的第一天
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startTime = DateUtils.format(calendar.getTime());
        //获取本月的最后一天
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); // 下个月的第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为下个月的第一天
        calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
        String endTime = DateUtils.format(calendar.getTime());
        Double recommendMoney=userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTimes(startTime,endTime,40,userId);
        return Result.success().put("data",recommendMoney==null?0.00:recommendMoney);
    }


    @GetMapping("/selectUserCountByAgency")
    @ApiOperation("获取达人的粉丝统计")
    public Result selectUserCountByAgency(Long userId){
        UserEntity userEntity = userService.selectUserById(userId);
        //总粉丝数量
        int userSumCount = userService.queryInviterCount(userEntity.getInvitationCode());
        //活跃粉丝
        int activeUserCount = userService.queryActiveUserCountByInviterCode(userEntity.getInvitationCode());
        //付费粉丝
        int moneyUserCount = userService.queryMoneyUserCountByInviterCode(userEntity.getInvitationCode());
        //达人粉丝
        int agencyUserCount = userService.queryAgencyUserCountByInviterCode(userEntity.getInvitationCode());
        //普通粉丝
        int userCount = userService.queryUserCountByInviterCode(userEntity.getInvitationCode());
        //剧达人分红

        //获取本月的第一天
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startTime = DateUtils.format(calendar.getTime());
        //获取本月的最后一天
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); // 下个月的第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为下个月的第一天
        calendar.add(Calendar.DAY_OF_MONTH, -1); // 减去一天
        String endTime = DateUtils.format(calendar.getTime());
        Double memberMoney=userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTimes(startTime,endTime,22,userId);
        Map<String,Object> result=new HashMap<>();
        result.put("userSumCount",userSumCount);
        result.put("activeUserCount",activeUserCount);
        result.put("moneyUserCount",moneyUserCount);
        result.put("agencyUserCount",agencyUserCount);
        result.put("userCount",userCount);
        result.put("memberMoney",memberMoney);
        return Result.success().put("data",result);
    }


    @GetMapping("/selectUserListByAgencyIndex")
    @ApiOperation("达人查询自己推荐的粉丝列表")
    public Result selectUserListByAgencyIndex(Long userId,Integer page,Integer limit,String userName,String phone){
        UserEntity userEntity = userService.selectUserById(userId);
        return userService.selectUserListByAgencyIndex(page,limit,userEntity.getUserId(),userEntity.getInvitationCode(),userName,phone);
    }


    @GetMapping("/selectUserListByInviteCode")
    @ApiOperation("查询自己的粉丝列表")
    public Result selectUserListByInviteCode(Long userId,Integer page,Integer limit,String search,
                                             Integer classify,Integer sort){
        UserEntity userEntity = userService.selectUserById(userId);
        return userService.selectUserListByInviteCode(page,limit,userEntity.getUserId(),userEntity.getInvitationCode(),search,classify,sort,userEntity.getChannelCode());
    }

}