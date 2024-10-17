package com.sqx.modules.app.controller.app;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.AppService;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.orders.dao.OrdersDao;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * APP登录授权
 */
@RestController
@RequestMapping("/app/user")
@Api(value = "APP管理", tags = {"APP管理"})
public class AppController {

    @Autowired
    private UserService userService;
    @Autowired
    private AppService appService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @PostMapping("/authenticationSuperUser")
    @ApiOperation("剧达人兑换")
    public Result authenticationSuperUser(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        return userService.authenticationSuperUser(jsonObject, request);
    }

    @Login
    @PostMapping("/updateUserWxOpenId")
    @ApiOperation("绑定app微信")
    public Result updateUserWxOpenId(@LoginUser UserEntity userEntity, String wxOpenId) {
        UserEntity userEntity1 = userService.getOne(new QueryWrapper<UserEntity>().eq("wx_open_id", wxOpenId));
        if (userEntity1 != null) {
            return Result.error("当前微信已被其他账号绑定！");
        }
        userEntity.setWxOpenId(wxOpenId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @Login
    @PostMapping("/getNewUserRed")
    @ApiOperation("领取新用户红包")
    public Result getNewUserRed(@RequestAttribute Long userId) {
        return userService.getNewUserRed(userId);
    }

    @Login
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("用户端修改密码")
    public Result updatePwd(@LoginUser UserEntity user, String pwd, String oldPwd) {
        if (!user.getPassword().equals(DigestUtils.sha256Hex(oldPwd))) {
            return Result.error("原始密码不正确！");
        }
        if (pwd.equals(oldPwd)) {
            return Result.error("新密码不能与旧密码相同！");
        }
        user.setPassword(DigestUtils.sha256Hex(pwd));
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    @ApiOperation("用户端换绑手机号")
    @ResponseBody
    public Result updatePhone(@RequestAttribute("userId") Long userId, @RequestParam String phone, @RequestParam String msg) {
        return userService.updatePhone(phone, msg, userId);
    }

    @Login
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUserImageUrl(@RequestAttribute("userId") Long userId, String zhiFuBao, String zhiFuBaoName, String wxImg) {
        UserEntity userEntity = new UserEntity();
        userEntity.setZhiFuBao(zhiFuBao);
        userEntity.setZhiFuBaoName(zhiFuBaoName);
        userEntity.setUserId(userId);
        userEntity.setWxImg(wxImg);
        userService.updateById(userEntity);
        return Result.success();
    }


    @Login
    @RequestMapping(value = "/updateUsers", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUsers(@RequestAttribute("userId") Long userId, @RequestBody UserEntity userEntity) {
        userEntity.setUserId(userId);
        userService.updateById(userEntity);
        return Result.success();
    }


    /*@Login
    @RequestMapping(value = "/updateUsers", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUsers(@RequestAttribute("userId") Long userId,String userName,String avatar,String phone) {
        UserEntity userEntity=new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setUserName(userName);
        userEntity.setAvatar(avatar);
        userEntity.setPhone(phone);
        userService.updateById(userEntity);
        return Result.success();
    }*/


    @Login
    @RequestMapping(value = "/updateUserImageUrl", method = RequestMethod.POST)
    @ApiOperation("用户修改头像")
    @ResponseBody
    public Result updateUserImageUrl(@LoginUser UserEntity user, String avatar) {
        int i = Integer.parseInt(commonInfoService.findOne(864).getValue());
        if (user.getAvatarNum() == null || user.getAvatarNum() < i) {
            user.setAvatar(avatar);
            if (user.getAvatarNum() == null) {
                user.setAvatarNum(1);
            } else {
                user.setAvatarNum(user.getAvatarNum() + 1);
            }
            userService.updateById(user);
            return Result.success();
        }
        return Result.error("今日已更换很多次了，请明天再试！");
    }

    @Login
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    @ApiOperation("用户修改昵称")
    @ResponseBody
    public Result updateUserName(@LoginUser UserEntity user, String userName) {
        int i = Integer.parseInt(commonInfoService.findOne(865).getValue());
        if (user.getUserNameNum() == null || user.getUserNameNum() < i) {
            user.setUserName(userName);
            if (user.getUserNameNum() == null) {
                user.setUserNameNum(1);
            } else {
                user.setUserNameNum(user.getUserNameNum() + 1);
            }
            userService.updateById(user);
            return Result.success();
        }
        return Result.error("今日已更换很多次了，请明天再试！");
    }

    @Login
    @RequestMapping(value = "/selectUserById", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@LoginUser UserEntity user) {
        user.setUpdateTime(DateUtils.format(new Date()));
        userService.updateById(user);
        return Result.success().put("data", user);
    }


    @RequestMapping(value = "/selectNewApp", method = RequestMethod.GET)
    @ApiOperation("升级检测")
    @ResponseBody
    public Result selectNewApp() {
        return Result.success().put("data", appService.selectNewApp());
    }

    @GetMapping("/openId/{code:.+}/{userId}")
    @ApiOperation("根据code获取openid")
    public Result getOpenid(@PathVariable("code") String code, @PathVariable("userId") Long userId) {
        return userService.getOpenId(code, userId);
    }

    @RequestMapping(value = "/updateClientId", method = RequestMethod.GET)
    @ApiOperation("绑定ClientId")
    @ResponseBody
    public Result updateClientId(String clientId, Long userId, Integer sysPhone) {
        userService.updateUserClientIdIsNull(clientId);
        UserEntity userEntity = new UserEntity();
        userEntity.setSysPhone(sysPhone);
        userEntity.setUserId(userId);
        userEntity.setClientid(clientId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @Login
    @GetMapping("/selectChannelUserListByRecommend")
    @ApiOperation("推荐人查询自己的剧荐管")
    public Result selectChannelUserListByRecommend(@LoginUser UserEntity userEntity, Integer page, Integer limit, String userName, String phone) {
        return userService.selectChannelUserListByRecommend(page, limit, userEntity.getUserId(), userName, phone);
    }

    @Login
    @GetMapping("/selectRecommendMoneyByMonth")
    @ApiOperation("推荐人查询收益")
    public Result selectRecommendMoneyByMonth(@LoginUser UserEntity userEntity) {
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
        Double recommendMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserIdTimes(startTime, endTime, 40, userEntity.getUserId());
        return Result.success().put("data", recommendMoney == null ? 0.00 : recommendMoney);
    }

    @Login
    @GetMapping("/selectUserCountByAgency")
    @ApiOperation("获取达人的粉丝统计")
    public Result selectUserCountByAgency(@LoginUser UserEntity userEntity) {
        // 总粉丝数量
        int userSumCount = userService.queryInviterCount(userEntity.getInvitationCode());
        // 活跃粉丝
        int activeUserCount = userService.queryActiveUserCountByInviterCode(userEntity.getInvitationCode());
        // 付费粉丝
        int moneyUserCount = userService.queryMoneyUserCountByInviterCode(userEntity.getInvitationCode());
        // 达人粉丝
        int agencyUserCount = userService.queryAgencyUserCountByInviterCode(userEntity.getInvitationCode());
        // 普通粉丝
        int userCount = userService.queryUserCountByInviterCode(userEntity.getInvitationCode());
        Map<String, Integer> result = new HashMap<>();
        result.put("userSumCount", userSumCount);
        result.put("activeUserCount", activeUserCount);
        result.put("moneyUserCount", moneyUserCount);
        result.put("agencyUserCount", agencyUserCount);
        result.put("userCount", userCount);
        return Result.success().put("data", result);
    }

    @Login
    @GetMapping("/selectUserListByAgencyIndex")
    @ApiOperation("达人查询自己推荐的粉丝列表")
    public Result selectUserListByAgencyIndex(@LoginUser UserEntity userEntity, Integer page, Integer limit, String userName, String phone) {
        return userService.selectUserListByAgencyIndex(page, limit, userEntity.getUserId(), userEntity.getInvitationCode(), userName, phone);
    }

    @Login
    @GetMapping("/selectUserListByInviteCode")
    @ApiOperation("查询自己的粉丝列表")
    public Result selectUserListByInviteCode(@LoginUser UserEntity userEntity, Integer page, Integer limit, String search,
                                             Integer classify, Integer sort) {
        return userService.selectUserListByInviteCode(page, limit, userEntity.getUserId(), userEntity.getInvitationCode(), search, classify, sort, userEntity.getChannelCode());
    }

    @Login
    @PostMapping("/verifyUserIdNumber")
    @ApiOperation("验证用户身份证信息")
    public Result verifyUserIdNumber(@LoginUser UserEntity userEntity, String idNumber, String idName) throws TencentCloudSDKException {
        return userService.verifyUserIdNumber(userEntity.getPhone(), idNumber, idName);
    }


}
