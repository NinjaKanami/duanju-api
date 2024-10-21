package com.sqx.modules.app.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.common.utils.tencent.PhoneVerification;
import com.sqx.modules.app.dao.MsgDao;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.app.utils.JwtUtils;
import com.sqx.modules.app.utils.UserConstantInterface;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.file.utils.Md5Utils;
import com.sqx.modules.integral.dao.UserIntegralDetailsDao;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.InvitationCodeUtil;
import com.sqx.modules.utils.MD5Util;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.faceid.v20180301.models.PhoneVerificationResponse;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.api.SnsAPI;
import weixin.popular.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */

@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MsgDao msgDao;
    @Autowired
    private JwtUtils jwtUtils;
    private int number = 1;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private UserIntegralDetailsDao userIntegralDetailsDao;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    /**
     * 身份信息验证
     *
     * @param userEntity 用户
     * @param idNumber   身份证号
     * @param idName     姓名
     * @return Result
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result verifyUserIdNumber(UserEntity userEntity, String idNumber, String idName) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            if (userEntity == null || idNumber == null || idName == null) {
                return Result.error("参数错误");
            }

            String id = commonInfoService.findOne(2001).getValue();
            String key = commonInfoService.findOne(2002).getValue();
            if (id == null || key == null) {
                return Result.error("验证密钥缺失");
            }
            // PhoneVerification verification = new PhoneVerification("AKID63wgImNm4DE3jIiw1ykOwMrHcyRmyzY7", "Vr36ziHh5JORL5h9RaZO3Zw9C5BXuOya");
            PhoneVerification verification = new PhoneVerification(id, key);
            PhoneVerificationResponse validated = verification.validate(userEntity.getPhone(), idNumber, idName);
            // 0: 三要素信息一致
            // -4: 三要素信息不一致
            // 不收费结果码
            // -6: 手机号码不合法
            // -7: 身份证号码有误
            // -8: 姓名校验不通过
            // -9: 没有记录
            // -11: 验证中心服务繁忙
            // -12: 认证次数超过当日限制，请次日重试
            switch (validated.getResult()) {
                case "0":
                    userEntity.setIdNumberNo(idNumber);
                    userEntity.setIdNumberName(idName);
                    save(userEntity);
                    return Result.success("验证通过");
                case "-4":
                    return Result.error("信息不一致");
                case "-6":
                    return Result.error("手机号码不合法");
                case "-7":
                    return Result.error("身份证号码有误");
                case "-8":
                    return Result.error("姓名校验不通过");
                case "-9":
                    return Result.error("无记录");
                case "-11":
                    return Result.error("验证中心服务繁忙");
                case "-12":
                    return Result.error("认证次数超过当日限制，请次日重试");
                default:
                    return Result.error("验证失败，请重试！");
            }

        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("验证失败：" + e.getMessage());
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Result authenticationSuperUser(JSONObject jsonObject, HttpServletRequest request) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            String apiKey = request.getHeader("apiKey");
            if (StringUtils.isEmpty(apiKey)) {
                return Result.error("请求错误，请检查参数后重试！");
            } else if (StringUtils.isNotEmpty(apiKey)) {
                String value = commonInfoService.findOne(861).getValue();
                if (!apiKey.equals(value)) {
                    return Result.error("请求错误，请检查参数后重试！");
                }
            }
            String phone = jsonObject.getString("phone");
            if (StringUtils.isEmpty(phone)) {
                return Result.error("phone参数不能为空");
            }
            String channelCode = jsonObject.getString("channelCode");
            if (StringUtils.isEmpty(channelCode)) {
                return Result.error("channelCode参数不能为空");
            }
            String idempotentId = jsonObject.getString("idempotentId");
            if (StringUtils.isEmpty(idempotentId)) {
                return Result.error("idempotentId参数不能为空");
            }
            String sign = jsonObject.getString("sign");
            if (StringUtils.isEmpty(sign)) {
                return Result.error("sign参数不能为空");
            }
            String apiSecret = commonInfoService.findOne(862).getValue();
            String signs = MD5Util.md5Encrypt32Upper(phone + channelCode + apiSecret);
            if (!signs.equals(sign)) {
                return Result.error("sign参数错误！");
            }
            MessageInfo messageInfo = messageService.getOne(new QueryWrapper<MessageInfo>().eq("state", 10).eq("platform", idempotentId));
            if (messageInfo != null) {
                // 请求已经处理过了
                Map<String, String> result = new HashMap<>();
                result.put("phone", messageInfo.getTitle());
                result.put("userType", messageInfo.getContent());
                return Result.success().put("data", result);
            }
            messageInfo = new MessageInfo();
            messageInfo.setState("10");
            messageInfo.setPlatform(idempotentId);
            messageInfo.setTitle(phone);
            // 处理用户为剧达人
            UserEntity userEntity = queryByPhone(phone);
            if (userEntity != null) {
                if (userEntity.getAgencyIndex() != null && userEntity.getAgencyIndex() == 2) {
                    return Result.error(501, "当前手机号已经是达人了！");
                }
                // 获取用户的剧荐管 判断是不是传递过来的剧荐管 不是则不让激活
                if (StringUtils.isNotEmpty(userEntity.getQdCode())) {
                    if (!userEntity.getQdCode().equals(channelCode)) {
                        return Result.error("请使用其他手机号激活");
                    }
                }
                // 进行激活
                userEntity.setQdCode(channelCode);
                userEntity.setAgencyIndex(2);
                Calendar calendar = Calendar.getInstance();
                userEntity.setAgencyStartTime(DateUtils.format(calendar.getTime()));
                calendar.add(Calendar.YEAR, 2);
                userEntity.setAgencyEndTime(DateUtils.format(calendar.getTime()));
                baseMapper.updateById(userEntity);

                // 获取我的一二级用户 都改成这个剧荐官邀请的用户
                baseMapper.updateUserQdCodeByInviterCode(channelCode, userEntity.getInvitationCode());
                baseMapper.updateUserQdCodeByInviterCodeList(channelCode, userEntity.getInvitationCode());
                messageInfo.setContent("2");
            } else {
                // 新用户
                UserEntity channelUser = queryByQdCode(channelCode);
                userEntity = new UserEntity();
                userEntity.setUserName(getUserName());
                userEntity.setQdCode(channelCode);
                userEntity.setQdUserType(1);
                userEntity.setPhone(phone);
                userEntity.setPassword(DigestUtils.sha256Hex(userEntity.getPhone()));
                userEntity.setAgencyIndex(2);
                Calendar calendar = Calendar.getInstance();
                userEntity.setAgencyStartTime(DateUtils.format(calendar.getTime()));
                calendar.add(Calendar.YEAR, 2);
                userEntity.setAgencyEndTime(DateUtils.format(calendar.getTime()));
                userEntity.setInviterCode(channelUser.getInvitationCode());
                userEntity.setCreateTime(DateUtils.format(new Date()));
                userEntity.setStatus(1);
                baseMapper.insert(userEntity);
                userEntity.setInvitationCode(InvitationCodeUtil.toSerialCode(userEntity.getUserId()));
                baseMapper.updateById(userEntity);
                // 发送短信
                sendMsg(phone, "newUser");
                messageInfo.setContent("1");
            }
            messageService.save(messageInfo);
            Map<String, String> result = new HashMap<>();
            result.put("phone", messageInfo.getTitle());
            result.put("userType", messageInfo.getContent());
            return Result.success().put("data", result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("激活高级会员出错：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Override
    public String getUserName() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int digit = ThreadLocalRandom.current().nextInt(0, 10);
            sb.append(digit);
        }
        String randomNum = sb.toString();
        String userName = commonInfoService.findOne(863).getValue() + randomNum;
        UserEntity userEntity = queryByPhone(userName);
        if (userEntity == null) {
            return userName;
        } else {
            return getUserName();
        }
    }

    @Override
    public Result getNewUserRed(Long userId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            UserEntity userEntity = baseMapper.selectById(userId);
            if (userEntity.getIsNewUser() != null && userEntity.getIsNewUser() == 1) {
                return Result.error("您已经领取过了！");
            }
            userEntity.setIsNewUser(1);
            baseMapper.updateById(userEntity);
            String value = commonInfoService.findOne(837).getValue();
            if (StringUtils.isNotEmpty(value)) {
                BigDecimal money = new BigDecimal(value);
                userMoneyService.updateMoney(1, userId, money.doubleValue());
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setUserId(userId);
                userMoneyDetails.setTitle("新用户红包");
                userMoneyDetails.setContent("增加点券：" + money);
                userMoneyDetails.setType(1);
                userMoneyDetails.setClassify(1);
                userMoneyDetails.setMoney(money);
                userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);
            }
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新用户领取红包出错：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");

    }

    @Override
    public UserEntity queryByUserName(String userName) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_name", userName));
    }

    @Override
    public UserEntity queryByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
    }

    @Override
    public UserEntity queryByQdCode(String qdCode) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("channel_code", qdCode));
    }

    @Override
    public UserEntity queryByOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("open_id", openId));
    }

    @Override
    public UserEntity queryByWxId(String wxId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_id", wxId));
    }

    @Override
    public UserEntity queryByDyOpenId(String dyOpenId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("dy_open_id", dyOpenId));
    }

    @Override
    public UserEntity queryByKsOpenId(String ksOpenId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("ks_open_id", ksOpenId));
    }

    @Override
    public UserEntity queryByWxOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_open_id", openId));
    }

    @Override
    public UserEntity queryByAppleId(String appleId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("apple_id", appleId));
    }

    @Override
    public UserEntity queryByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
    }

    @Override
    public UserEntity queryByInvitationCode(String invitationCode) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", invitationCode));
    }

    @Override
    public Result updatePhone(String phone, String msg, Long userId) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        // 校验短信验证码
        if (msg1 != null) {
            msgDao.deleteById(msg1.getId());
            UserEntity userInfo = queryByPhone(phone);
            if (userInfo != null) {
                return Result.error("手机号已经被其他账号绑定");
            } else {
                UserEntity one = baseMapper.selectById(userId);
                one.setPhone(phone);
                baseMapper.updateById(one);
                return Result.success();
            }
        }
        return Result.error("验证码不正确");
    }

    @Override
    public Result iosRegister(String appleId) {
        if (StringUtils.isEmpty(appleId)) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByAppleId(appleId);
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            userInfo.setUpdateTime(date);
            baseMapper.updateById(userInfo);
            // 返回用户信息
            UserEntity user = queryByAppleId(appleId);
            return getResult(user);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }

    @Override
    public Result wxLogin(String code) {
        try {
            String appid = commonInfoService.findOne(45).getValue();
            String secret = commonInfoService.findOne(46).getValue();
            // 配置请求参数
            Map<String, String> param = new HashMap<>();
            param.put("appid", appid);
            param.put("secret", secret);
            param.put("js_code", code);
            param.put("grant_type", UserConstantInterface.WX_LOGIN_GRANT_TYPE);
            param.put("scope", "snsapi_userinfo");
            // 发送请求
            String wxResult = HttpClientUtil.doGet(UserConstantInterface.WX_LOGIN_URL, param);
            log.info(wxResult);
            JSONObject jsonObject = JSONObject.parseObject(wxResult);
            // 获取参数返回的
            String session_key = jsonObject.get("session_key").toString();
            String open_id = jsonObject.get("openid").toString();
            UserEntity userEntity = queryByOpenId(open_id);
            Map<String, String> map = new HashMap<>();
            // 封装返回小程序
            map.put("session_key", session_key);
            map.put("open_id", open_id);
            if (jsonObject.get("unionid") != null) {
                String unionid = jsonObject.get("unionid").toString();
                map.put("unionid", unionid);
            } else {
                map.put("unionid", "-1");
            }
            String value = commonInfoService.findOne(237).getValue();
            if ("是".equals(value)) {
                if (userEntity == null || StringUtils.isEmpty(userEntity.getPhone())) {
                    map.put("flag", "1");
                } else {
                    map.put("flag", "2");
                }
            } else {
                map.put("flag", "2");
            }
            return Result.success("登陆成功").put("data", map);
        } catch (Exception e) {
            System.err.println(e.toString());
            return Result.success("登录失败！");
        }
    }


    @Override
    public Result wxRegister(UserEntity userInfo1) {
        if (StringUtils.isEmpty(userInfo1.getOpenId())) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByOpenId(userInfo1.getOpenId());
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                userInfo.setPhone(userInfo1.getPhone());
            }
            userInfo.setUpdateTime(date);
            baseMapper.updateById(userInfo);
        } else {
            // 判断是否在app登陆过  手机号是否有账号
            UserEntity userByMobile = null;
            if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                userByMobile = queryByPhone(userInfo1.getPhone());
            }
            if (userByMobile != null) {
                // 有账号则绑定账号
                userByMobile.setOpenId(userInfo1.getOpenId());
                baseMapper.updateById(userByMobile);
                if (userByMobile.getStatus().equals(2)) {
                    return Result.error("账号已被封禁，请联系客服处理！");
                }
            } else {
                if (StringUtils.isEmpty(userInfo1.getInviterCode())) {
                    userInfo1.setInviterCode(commonInfoService.findOne(88).getValue());
                }
                UserEntity userEntity = queryByInvitationCode(userInfo1.getInviterCode());

                if (userEntity != null && userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
                    userInfo1.setQdCode(userEntity.getChannelCode());
                    userInfo1.setQdUserType(1);
                } else if (userEntity != null && StringUtils.isNotEmpty(userEntity.getQdCode()) && (userEntity.getQdUserType() == null || userEntity.getQdUserType() < 3)) {
                    userInfo1.setQdCode(userEntity.getQdCode());
                    if (userEntity.getQdUserType() == null) {
                        userEntity.setQdUserType(1);
                    }
                    userInfo1.setQdUserType(userEntity.getQdUserType() + 1);
                }
                // 没有则生成新账号
                userInfo1.setCreateTime(date);
                userInfo1.setPlatform("小程序");
                userInfo1.setStatus(1);
                userInfo1.setPassword(DigestUtils.sha256Hex(userInfo1.getPhone()));
                baseMapper.insert(userInfo1);
                userInfo1.setInvitationCode(InvitationCodeUtil.toSerialCode(userInfo1.getUserId()));
                baseMapper.updateById(userInfo1);
                if (userEntity != null) {
                    inviteService.saveBody(userInfo1.getUserId(), userEntity);
                }
            }
        }
        // 返回用户信息
        UserEntity user = queryByOpenId(userInfo1.getOpenId());
        return getResult(user);
    }


    @Override
    public Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, Integer sysPhone, String inviterCode, String qdCode) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getWxOpenId())) {
                return Result.error("当前手机号已经被其他微信绑定");
            }
            // 小程序登陆过
            userInfo.setWxOpenId(wxOpenId);
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userInfo.setAvatar(user.getHeadimgurl());
                }
                userInfo.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userInfo.setUserName(getUserName());
                }
            }
            baseMapper.updateById(userInfo);
        } else {
            // 小程序没有登陆过
            userInfo = new UserEntity();

            if (StringUtils.isEmpty(inviterCode)) {
                inviterCode = commonInfoService.findOne(88).getValue();
            }
            UserEntity userEntity = queryByInvitationCode(inviterCode);

            if (userEntity != null && userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
                userInfo.setQdCode(userEntity.getChannelCode());
                userInfo.setQdUserType(1);
            } else if (userEntity != null && StringUtils.isNotEmpty(userEntity.getQdCode()) && (userEntity.getQdUserType() == null || userEntity.getQdUserType() < 3)) {
                userInfo.setQdCode(userEntity.getQdCode());
                if (userEntity.getQdUserType() == null) {
                    userEntity.setQdUserType(1);
                }
                userInfo.setQdUserType(userEntity.getQdUserType() + 1);
            }

            userInfo.setInviterCode(inviterCode);
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userInfo.setAvatar(user.getHeadimgurl());
                }
                userInfo.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userInfo.setUserName(getUserName());
                }
            }
            userInfo.setWxOpenId(wxOpenId);
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(time);
            baseMapper.insert(userInfo);
            if (userEntity != null) {
                inviteService.saveBody(userInfo.getUserId(), userEntity);
            }
        }
        UserEntity userEntity = queryByWxOpenId(userInfo.getWxOpenId());
        return getResult(userEntity);
    }

    @Override
    public Result iosBindMobile(String phone, String code, String appleId, String platform, Integer sysPhone, String inviterCode, String qdCode) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getAppleId())) {
                return Result.error("当前手机号已经被其他苹果绑定");
            }
            userInfo.setAppleId(appleId);
            userInfo.setUpdateTime(simpleDateFormat.format(new Date()));
            baseMapper.updateById(userInfo);
        } else {
            userInfo = new UserEntity();
            if (StringUtils.isEmpty(inviterCode)) {
                inviterCode = commonInfoService.findOne(88).getValue();
            }
            UserEntity userEntity = queryByInvitationCode(inviterCode);
            if (userEntity != null && userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
                userInfo.setQdCode(userEntity.getChannelCode());
                userInfo.setQdUserType(1);
            } else if (userEntity != null && StringUtils.isNotEmpty(userEntity.getQdCode()) && (userEntity.getQdUserType() == null || userEntity.getQdUserType() < 3)) {
                userInfo.setQdCode(userEntity.getQdCode());
                if (userEntity.getQdUserType() == null) {
                    userEntity.setQdUserType(1);
                }
                userInfo.setQdUserType(userEntity.getQdUserType() + 1);
            }

            userInfo.setAppleId(appleId);
            userInfo.setInviterCode(inviterCode);
            userInfo.setSex(0);
            userInfo.setUserName(getUserName());
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(time);
            baseMapper.insert(userInfo);
            if (userEntity != null) {
                inviteService.saveBody(userInfo.getUserId(), userEntity);
            }
        }
        UserEntity userEntity = queryByAppleId(userInfo.getAppleId());
        return getResult(userEntity);
    }


    @Override
    public Result phoneLogin(String phone) {
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error(500, "账号已被禁用，请联系客服处理！");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(sdf.format(new Date()));
            baseMapper.updateById(userInfo);
            return getResult(userInfo);
        } else {
            return Result.error(201, "请绑定密码！");
        }
    }


    @Override
    public Result bindMobile(String phone, String platform, Integer sysPhone, String inviterCode, String qdCode) {
        UserEntity userInfo = new UserEntity();

        if (StringUtils.isEmpty(inviterCode)) {
            inviterCode = commonInfoService.findOne(88).getValue();
        }
        UserEntity userEntity = queryByInvitationCode(inviterCode);
        if (userEntity != null && userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
            userInfo.setQdCode(userEntity.getChannelCode());
            userInfo.setQdUserType(1);
        } else if (userEntity != null && StringUtils.isNotEmpty(userEntity.getQdCode()) && (userEntity.getQdUserType() == null || userEntity.getQdUserType() < 3)) {
            userInfo.setQdCode(userEntity.getQdCode());
            if (userEntity.getQdUserType() == null) {
                userEntity.setQdUserType(1);
            }
            userInfo.setQdUserType(userEntity.getQdUserType() + 1);
        }

        userInfo.setInviterCode(inviterCode);
        userInfo.setSex(0);
        userInfo.setUserName(getUserName());
        userInfo.setPhone(phone);
        userInfo.setPlatform(platform);
        userInfo.setCreateTime(DateUtils.format(new Date()));
        userInfo.setSysPhone(sysPhone);
        userInfo.setStatus(1);
        userInfo.setUpdateTime(DateUtils.format(new Date()));
        baseMapper.insert(userInfo);
        if (userEntity != null) {
            inviteService.saveBody(userInfo.getUserId(), userEntity);
        }
        return getResult(userInfo);
    }


    @Override
    public Result wxAppLogin(String wxOpenId, String token) {
        UserEntity userEntity = queryByWxOpenId(wxOpenId);
        if (userEntity != null) {
            if (userEntity.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userEntity.setAvatar(user.getHeadimgurl());
                }
                userEntity.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userEntity.setUserName(getUserName());
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userEntity.setUpdateTime(sdf.format(new Date()));
            baseMapper.updateById(userEntity);
            return getResult(userEntity);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }

    @Override
    public Result dyLogin(String code, String anonymous_code) {
        String appid = commonInfoService.findOne(805).getValue();
        String secret = commonInfoService.findOne(806).getValue();
        // 配置请求参数
        JSONObject param = new JSONObject();
        param.put("appid", appid);
        param.put("secret", secret);
        param.put("code", code);
        param.put("anonymous_code", anonymous_code);
        // 发送请求
        String wxResult = HttpClientUtil.doPostJson("https://developer.toutiao.com/api/apps/v2/jscode2session", param.toJSONString());
        log.info(wxResult);
        JSONObject jsonObject = JSONObject.parseObject(wxResult);
        String err_no = jsonObject.getString("err_no");
        if (!"0".equals(err_no)) {
            return Result.error(jsonObject.getString("err_tips"));
        }
        JSONObject data = jsonObject.getJSONObject("data");
        String openid = data.getString("openid");

        if (StringUtils.isEmpty(openid)) {
            openid = data.getString("anonymous_openid");
        }
        UserEntity userEntity = queryByDyOpenId(openid);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("dyOpenId", openid);
        String value = commonInfoService.findOne(814).getValue();
        if ("是".equals(value)) {
            if (userEntity == null || StringUtils.isEmpty(userEntity.getPhone())) {
                resultMap.put("flag", "1");
            } else {
                resultMap.put("flag", "2");
            }
        } else {
            resultMap.put("flag", "2");
        }
        return Result.success("登陆成功").put("data", resultMap);
    }

    @Override
    public Result dyRegister(UserEntity userInfo1) {
        if (StringUtils.isEmpty(userInfo1.getDyOpenId())) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByDyOpenId(userInfo1.getDyOpenId());
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            userInfo.setUpdateTime(date);
            baseMapper.updateById(userInfo);
        } else {
            // 判断是否在app登陆过  手机号是否有账号
            UserEntity userByMobile = null;
            if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                userByMobile = queryByPhone(userInfo1.getPhone());
            }
            if (userByMobile != null) {
                // 有账号则绑定账号
                userByMobile.setDyOpenId(userInfo1.getDyOpenId());
                baseMapper.updateById(userByMobile);
                if (userByMobile.getStatus().equals(2)) {
                    return Result.error("账号已被封禁，请联系客服处理！");
                }
            } else {
                if (StringUtils.isEmpty(userInfo1.getInviterCode())) {
                    userInfo1.setInviterCode(commonInfoService.findOne(88).getValue());
                }
                UserEntity userEntity = queryByInvitationCode(userInfo1.getInviterCode());
                // 没有则生成新账号
                userInfo1.setCreateTime(date);
                userInfo1.setPlatform("抖音");
                userInfo1.setStatus(1);
                if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                    userInfo1.setPassword(DigestUtils.sha256Hex(userInfo1.getPhone()));
                }
                baseMapper.insert(userInfo1);
                userInfo1.setInvitationCode(InvitationCodeUtil.toSerialCode(userInfo1.getUserId()));
                baseMapper.updateById(userInfo1);
                if (userEntity != null) {
                    inviteService.saveBody(userInfo1.getUserId(), userEntity);
                }
//                userMoneyService.selectUserMoneyByUserId(userInfo1.getUserId());
            }
        }
        // 返回用户信息
        UserEntity user = queryByDyOpenId(userInfo1.getDyOpenId());
        return getResult(user);
    }


    @Override
    public Result ksLogin(String code) {
        String appid = commonInfoService.findOne(828).getValue();
        String secret = commonInfoService.findOne(829).getValue();
        // 配置请求参数
        HashMap<String, String> param = new HashMap<>();
        param.put("app_id", appid);
        param.put("app_secret", secret);
        param.put("js_code", code);
        // 发送请求
        String ksResult = HttpClientUtil.doPost("https://open.kuaishou.com/oauth2/mp/code2session", param);
        log.info(ksResult);
        JSONObject jsonObject = JSONObject.parseObject(ksResult);
        String result = jsonObject.getString("result");
        if (!"1".equals(result)) {
            return Result.error("登录失败！");
        }

        String session_key = jsonObject.getString("session_key");
        String openid = jsonObject.getString("open_id");

        UserEntity userEntity = queryByDyOpenId(openid);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("ksOpenId", openid);
        resultMap.put("session_key", session_key);
        String value = commonInfoService.findOne(830).getValue();
        if ("是".equals(value)) {
            if (userEntity == null || StringUtils.isEmpty(userEntity.getPhone())) {
                resultMap.put("flag", "1");
            } else {
                resultMap.put("flag", "2");
            }
        } else {
            resultMap.put("flag", "2");
        }
        return Result.success("登陆成功").put("data", resultMap);
    }

    @Override
    public Result ksRegister(UserEntity userInfo1) {
        if (StringUtils.isEmpty(userInfo1.getKsOpenId())) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByKsOpenId(userInfo1.getKsOpenId());
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            userInfo.setUpdateTime(date);
            baseMapper.updateById(userInfo);
        } else {
            // 判断是否在app登陆过  手机号是否有账号
            UserEntity userByMobile = null;
            if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                userByMobile = queryByPhone(userInfo1.getPhone());
            }
            if (userByMobile != null) {
                // 有账号则绑定账号
                userByMobile.setKsOpenId(userInfo1.getKsOpenId());
                baseMapper.updateById(userByMobile);
                if (userByMobile.getStatus().equals(2)) {
                    return Result.error("账号已被封禁，请联系客服处理！");
                }
            } else {
                if (StringUtils.isEmpty(userInfo1.getInviterCode())) {
                    userInfo1.setInviterCode(commonInfoService.findOne(88).getValue());
                }
                UserEntity userEntity = queryByInvitationCode(userInfo1.getInviterCode());
                // 没有则生成新账号
                userInfo1.setCreateTime(date);
                userInfo1.setPlatform("快手");
                userInfo1.setStatus(1);
                if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                    userInfo1.setPassword(DigestUtils.sha256Hex(userInfo1.getPhone()));
                }
                baseMapper.insert(userInfo1);
                userInfo1.setInvitationCode(InvitationCodeUtil.toSerialCode(userInfo1.getUserId()));
                baseMapper.updateById(userInfo1);
                if (userEntity != null) {
                    inviteService.saveBody(userInfo1.getUserId(), userEntity);
                }
//                userMoneyService.selectUserMoneyByUserId(userInfo1.getUserId());
            }
        }
        // 返回用户信息
        UserEntity user = queryByKsOpenId(userInfo1.getKsOpenId());
        return getResult(user);
    }

    @Override
    public Result registerCode(String phone, String msg, String platform, Integer sysPhone, String password,
                               String inviterCode, String wxId, String qdCode) {
        // 校验手机号是否存在
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(password)) {
                // 密码登录
                if (StringUtils.isEmpty(userInfo.getPassword())) {
                    return Result.error("当前账号未绑定密码，请前往忘记密码中进行重置！");
                }
                if (!userInfo.getPassword().equals(DigestUtils.sha256Hex(password))) {
                    return Result.error("账号或密码不正确！");
                }
            } else if (StringUtils.isNotEmpty(msg)) {
                // 验证码登录
                Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
                if (msg1 == null) {
                    return Result.error("验证码不正确！");
                }
                msgDao.deleteById(msg1.getId());
            } else {
                return Result.error("登录失败，请刷新页面重试！");
            }
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotEmpty(wxId) && StringUtils.isNotEmpty(userInfo.getWxId()) && !wxId.equals(userInfo.getWxId())) {
                return Result.error("当前手机号已经绑定过了，请更换其他手机号！");
            }
            if (StringUtils.isNotEmpty(wxId)) {
                userInfo.setWxId(wxId);
            }
            userInfo.setUpdateTime(sdf.format(new Date()));
            baseMapper.updateById(userInfo);
            return getResult(userInfo);
        } else {
            if (StringUtils.isEmpty(msg)) {
                return Result.error("手机号未注册！");
            }
            Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
            if (msg1 == null) {
                return Result.error("验证码不正确！");
            }
            userInfo = new UserEntity();
            UserEntity userEntity = null;
            if (StringUtils.isNotEmpty(inviterCode)) {
                userEntity = queryByInvitationCode(inviterCode);
                if (userEntity == null) {
                    return Result.error("邀请码不正确！");
                }
                userInfo.setInviterCode(inviterCode);
            } else {
                userInfo.setInviterCode(commonInfoService.findOne(88).getValue());
                userEntity = queryByInvitationCode(userInfo.getInviterCode());
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            if (StringUtils.isNotEmpty(wxId)) {
                userInfo.setWxId(wxId);
            }
            if (userEntity != null && userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
                userInfo.setQdCode(userEntity.getChannelCode());
                userInfo.setQdUserType(1);
            } else if (userEntity != null && StringUtils.isNotEmpty(userEntity.getQdCode()) && (userEntity.getQdUserType() == null || userEntity.getQdUserType() < 3)) {
                userInfo.setQdCode(userEntity.getQdCode());
                if (userEntity.getQdUserType() == null) {
                    userEntity.setQdUserType(1);
                }
                userInfo.setQdUserType(userEntity.getQdUserType() + 1);
            }

            userInfo.setPhone(phone);
            userInfo.setUserName(getUserName());
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setPassword(DigestUtils.sha256Hex(password));
            userInfo.setStatus(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(sdf.format(new Date()));
            baseMapper.insert(userInfo);
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode(userInfo.getUserId()));
            baseMapper.updateById(userInfo);
            msgDao.deleteById(msg1.getId());
            if (userEntity != null) {
                inviteService.saveBody(userInfo.getUserId(), userEntity);
            }
            return getResult(userInfo);
        }
    }


    @Override
    public Result bindWxOpenPhone(Long userId, String phone, String msg) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, msg);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        UserEntity userEntity = queryByPhone(phone);
        if (userEntity != null) {
            return Result.error("当前手机号已经绑定过了，请直接登录！");
        }
        UserEntity byId = baseMapper.selectById(userId);
        byId.setPhone(phone);
        baseMapper.updateById(byId);
        return Result.success();
    }

    @Override
    public Result forgetPwd(String pwd, String phone, String msg) {
        try {
            Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, msg);
            // 校验短信验证码
            if (byPhoneAndCode == null) {
                return Result.error("验证码不正确");
            }
            UserEntity userByPhone = queryByPhone(phone);
            userByPhone.setPassword(DigestUtils.sha256Hex(pwd));
            msgDao.deleteById(byPhoneAndCode.getId());
            baseMapper.updateById(userByPhone);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("服务器内部错误");
        }
    }


    @Override
    public Result login(String phone, String pwd) {
        UserEntity userEntity = queryByPhone(phone);
        if (userEntity == null) {
            return Result.error("手机号未注册！");
        }
        if (!userEntity.getPassword().equals(DigestUtils.sha256Hex(pwd))) {
            return Result.error("密码不正确！");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("账号已被禁用，请联系客服处理！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userEntity.setUpdateTime(sdf.format(new Date()));
        baseMapper.updateById(userEntity);
        return getResult(userEntity);
    }


    @Override
    public Result getResult(UserEntity user) {
        // 生成token
        String token = jwtUtils.generateToken(user.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", user);
        return Result.success(map);
    }


    @Override
    public Result sendMsg(String phone, String state) {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        System.out.println("sendMsg code is " + code);
        SmsSingleSenderResult result = null;
        if ("bindWx".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null && StringUtils.isNotEmpty(userByPhone.getWxOpenId())) {
                return Result.error("当前手机号已被其他微信账号绑定");
            }
        } else if ("bindIos".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null && StringUtils.isNotEmpty(userByPhone.getAppleId())) {
                return Result.error("当前手机号已被其他苹果账号绑定");
            }
        } else if ("login".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null) {
                return Result.error("当前手机号已注册！");
            }
        } else if ("forget".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone == null) {
                return Result.error("手机号未注册！");
            }
        }
        CommonInfo three = commonInfoService.findOne(79);
        // 默认使用腾讯云
        if (three == null || "1".equals(three.getValue())) {
            // 腾讯云短信发送
            return sendMsgTencent(phone, state, code);
        } else if ("2".equals(three.getValue())) {
            // 阿里云短信发送
            return sendMsgAlibaba(phone, state, code);
        } else {
            return sendMsgDXB(phone, state, code);
        }
    }


    private Result sendMsgAlibaba(String phone, String state, int code) {

        CommonInfo three = commonInfoService.findOne(85);
        String accessKeyId = three.getValue();
        CommonInfo four = commonInfoService.findOne(86);
        String accessSecret = four.getValue();
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonInfo name = commonInfoService.findOne(81);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", name.getValue());
        String value;
        switch (state) {
            case "login":
                value = commonInfoService.findOne(82).getValue();
                break;
            case "forget":
                value = commonInfoService.findOne(83).getValue();
                break;
            case "bindWx":
                value = commonInfoService.findOne(84).getValue();
                break;
            case "bindIos":
                value = commonInfoService.findOne(84).getValue();
                break;
            default:
                value = commonInfoService.findOne(82).getValue();
                break;
        }
        request.putQueryParameter("TemplateCode", value);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            String data = response.getData();
            JSONObject jsonObject = JSON.parseObject(data);
            if ("OK".equals(jsonObject.get("Code"))) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.success("login");
                } else {
                    return Result.success("register");
                }
            } else {
                if (jsonObject.get("Message").toString().contains("分钟")) {
                    return Result.error("短信发送过于频繁，请一分钟后再试！");
                } else if (jsonObject.get("Message").toString().contains("小时")) {
                    return Result.error("短信发送过于频繁，请一小时后再试！");
                } else if (jsonObject.get("Message").toString().contains("天")) {
                    return Result.error("短信发送过于频繁，请明天再试！");
                }
                log.info(jsonObject.get("Message").toString());
                return Result.error("短信发送失败！");
            }
        } catch (ClientException | com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgTencent(String phone, String state, int code) {
        SmsSingleSenderResult result = null;

        CommonInfo three = commonInfoService.findOne(31);
        String clientId = three.getValue();
        CommonInfo four = commonInfoService.findOne(32);
        String clientSecret = four.getValue();
        try {
            String secretId = commonInfoService.findOne(240).getValue();
            String secretKey = commonInfoService.findOne(241).getValue();
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {phone};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setSmsSdkAppId(clientId);
            CommonInfo one = commonInfoService.findOne(81);
            req.setSignName(one.getValue());
            switch (state) {
                case "register":
                    req.setTemplateId(commonInfoService.findOne(242).getValue());
                    break;
                case "forget":
                    req.setTemplateId(commonInfoService.findOne(243).getValue());
                    break;
                case "bind":
                    req.setTemplateId(commonInfoService.findOne(244).getValue());
                    break;
                default:
                    req.setTemplateId(commonInfoService.findOne(242).getValue());
                    break;
            }
            String[] templateParamSet1 = {String.valueOf(code)};
            req.setTemplateParamSet(templateParamSet1);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            JSONObject jsonObject = JSONObject.parseObject(SendSmsResponse.toJsonString(resp));
            JSONArray sendStatusSet = jsonObject.getJSONArray("SendStatusSet");
            JSONObject jsonObject1 = sendStatusSet.getJSONObject(0);
            if ("Ok".equals(jsonObject1.getString("Code"))) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.success("login");
                } else {
                    return Result.success("register");
                }
            }
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgDXB(String phone, String state, int code) {
        CommonInfo three = commonInfoService.findOne(238);
        CommonInfo four = commonInfoService.findOne(239);
        CommonInfo name = commonInfoService.findOne(81);
        String testUsername = three.getValue(); // 在短信宝注册的用户名
        String testPassword = four.getValue(); // 在短信宝注册的密码
        String value = "";
        switch (state) {
            case "register":
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "forget":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "bind":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            default:
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
        }
        StringBuilder httpArg = new StringBuilder();
        httpArg.append("u=").append(testUsername).append("&");
        httpArg.append("p=").append(Md5Utils.md5s(testPassword)).append("&");
        httpArg.append("m=").append(phone).append("&");
        httpArg.append("c=").append(Md5Utils.encodeUrlString(value, "UTF-8"));
        String result = Md5Utils.request("https://api.smsbao.com/sms", httpArg.toString());
        log.error("短信包返回值：" + result);
        if ("0".equals(result)) {
            Msg byPhone = msgDao.findByPhone(phone);
            if (byPhone != null) {
                byPhone.setCode(String.valueOf(code));
                byPhone.setPhone(phone);
                msgDao.updateById(byPhone);
            } else {
                Msg msg = new Msg();
                msg.setCode(String.valueOf(code));
                msg.setPhone(phone);
                msgDao.insert(msg);
            }
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null) {
                return Result.success("login");
            } else {
                return Result.success("register");
            }
        } else {
//            return ResultUtil.error(6, result.errMsg);
            if ("30".equals(result)) {
                return Result.error("错误密码");
            } else if ("40".equals(result)) {
                return Result.error("账号不存在");
            } else if ("41".equals(result)) {
                return Result.error("余额不足");
            } else if ("43".equals(result)) {
                return Result.error("IP地址限制");
            } else if ("50".equals(result)) {
                return Result.error("内容含有敏感词");
            } else if ("51".equals(result)) {
                return Result.error("手机号码不正确");
            }
        }

        return Result.error("验证码发送失败");
    }


    @Override
    public Result getOpenId(String code, Long userId) {
        try {
            // 微信appid
            CommonInfo one = commonInfoService.findOne(5);
            // 微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            String openid = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code).getOpenid();
            if (StringUtils.isNotEmpty(openid)) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUserId(userId);
                userEntity.setWxId(openid);
                baseMapper.updateById(userEntity);
                return Result.success().put("data", openid);
            }
            return Result.error("获取失败");
        } catch (Exception e) {
            log.error("GET_OPENID_FAIL");
            return Result.error("获取失败,出错了！");
        }
    }

    @Override
    public UserEntity selectUserById(Long userId) {
        UserEntity userEntity = baseMapper.selectById(userId);
        if (userEntity != null) {
            UserVip userVip = userVipService.selectUserVipByUserId(userId);
            if (userVip != null) {
                userEntity.setMember(userVip.getIsVip());
                userEntity.setEndTime(userVip.getEndTime());
            }
        }
        return userEntity;
    }


    @Override
    public PageUtils selectUserPage(Integer page, Integer limit, String search, Integer sex, String platform,
                                    String sysPhone, Integer status, Integer member, String inviterCode, String userName,
                                    String invitationCode, String startTime, String endTime, String qdCode, String sysUserName, Integer vipType,
                                    String isRecommend, String isChannel, String agencyIndex) {
        Page<UserEntity> pages = new Page<>(page, limit);
        return new PageUtils(baseMapper.selectUserPage(pages, search, sex, platform, sysPhone, status, member,
                inviterCode, userName, invitationCode, startTime, endTime, qdCode, sysUserName, vipType, isRecommend, isChannel, agencyIndex));
    }

    @Override
    public List<UserEntity> userListExcel(String startTime, String endTime, UserEntity userEntity) {
        return baseMapper.userListExcel(startTime, endTime, userEntity);
    }


    @Override
    public int queryInviterCount(String inviterCode) {
        return baseMapper.queryInviterCount(inviterCode);
    }

    @Override
    public int queryUserCount(int type, String date, String platform, String qdCode) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        if (date == null || date == "") {
            date = simpleDateFormat.format(new Date());
        }
        return baseMapper.queryUserCount(type, date, platform, qdCode);
    }

    @Override
    public Double queryPayMoney(int type, String qdCode) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String date = simpleDateFormat.format(new Date());
        return baseMapper.queryPayMoney(type, date, qdCode);
    }

    @Override
    public IPage<Map<String, Object>> queryCourseOrder(Page<Map<String, Object>> iPage, int type, String date, Long sysUserId) {
        return baseMapper.queryCourseOrder(iPage, type, date, sysUserId);
    }

    @Override
    public int userMessage(String date, int type, String qdCode, Integer vipType) {
        return baseMapper.userMessage(date, type, qdCode, vipType);
    }


    @Override
    public void pushToSingle(String title, String content, String clientId) {
        try {
            if (StringUtils.isNotEmpty(clientId)) {
                UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("clientid", clientId));
                GtApiConfiguration apiConfiguration = new GtApiConfiguration();
                // 填写应用配置
                apiConfiguration.setAppId(commonInfoService.findOne(61).getValue());
                apiConfiguration.setAppKey(commonInfoService.findOne(60).getValue());
                apiConfiguration.setMasterSecret(commonInfoService.findOne(62).getValue());
                // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
                apiConfiguration.setDomain("https://restapi.getui.com/v2/");
                // 实例化ApiHelper对象，用于创建接口对象
                ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
                // 创建对象，建议复用。目前有PushApi、StatisticApi、UserApi
                PushApi pushApi = apiHelper.creatApi(PushApi.class);
                // 根据cid进行单推
                PushDTO<Audience> pushDTO = new PushDTO<Audience>();
                // 设置推送参数
                pushDTO.setRequestId(System.currentTimeMillis() + "");
                PushMessage pushMessage = new PushMessage();
                if (userEntity == null || userEntity.getSysPhone() == null || userEntity.getSysPhone() == 1) {
                    // 安卓推送
                    GTNotification notification = new GTNotification();
                    pushDTO.setPushMessage(pushMessage);
                    // 配置通知栏图标
                    notification.setLogo(commonInfoService.findOne(19).getValue() + "/logo.png"); // 配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
                    // 配置通知栏网络图标
                    notification.setLogoUrl(commonInfoService.findOne(19).getValue() + "/logo.png");
                    notification.setTitle(title);
                    notification.setBody(content);
                    notification.setClickType("startapp");
                    notification.setUrl(commonInfoService.findOne(19).getValue());
                    notification.setChannelLevel("3");
                    pushMessage.setNotification(notification);
                } else {
                    pushMessage.setTransmission(title);
                    pushDTO.setPushMessage(pushMessage);
                    PushChannel pushChannel = new PushChannel();
                    IosDTO iosDTO = new IosDTO();
                    Aps aps = new Aps();
                    Alert alert = new Alert();
                    alert.setTitle(title);
                    alert.setBody(content);
                    aps.setAlert(alert);
                    aps.setSound("default");
                    iosDTO.setAps(aps);
                    pushChannel.setIos(iosDTO);
                    pushDTO.setPushChannel(pushChannel);
                }
                // 设置接收人信息
                Audience audience = new Audience();
                audience.addCid(clientId);
                pushDTO.setAudience(audience);
                // 进行cid单推
                ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
                if (apiResult.isSuccess()) {
                    // success
                    log.info("消息推送成功：" + apiResult.getData());
                } else {
                    // failed
                    log.error("消息推送失败：code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息推送异常：" + e.getMessage(), e);
        }
    }

    @Override
    public Result selectInviteUserList(Integer page, Integer limit, String userName, String phone) {
        return Result.success().put("data", baseMapper.selectInviteUserList(new Page<>(page, limit), userName, phone));
    }


    @Override
    public Result selectChannelUserListByRecommend(Integer page, Integer limit, Long userId, String userName, String phone) {
        return Result.success().put("data", baseMapper.selectChannelUserListByRecommend(new Page<>(page, limit), userId, userName, phone));
    }

    @Override
    public Result selectUserListByAgencyIndex(Integer page, Integer limit, Long userId, String invitationCode, String userName, String phone) {
        return Result.success().put("data", baseMapper.selectUserListByAgencyIndex(new Page<>(page, limit), userId, invitationCode, userName, phone));
    }

    @Override
    public Result selectUserListByInviteCode(Integer page, Integer limit, Long userId, String invitationCode, String search,
                                             Integer classify, Integer sort, String qdCode) {
        return Result.success().put("data", baseMapper.selectUserListByInviteCode(new Page<>(page, limit), userId, invitationCode, search,
                classify, sort, qdCode));
    }


    @Override
    public Result loginByOpenId(String openId) {
        UserEntity userEntity = queryByWxId(openId);
        if (userEntity == null) {
            return Result.error(-200, "未注册！");
        }
        String token = jwtUtils.generateToken(userEntity.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", userEntity);
        return Result.success(map);
    }

    @Override
    public Result selectUserOnLineCount(String qdCode) {
        return Result.success().put("data", baseMapper.selectUserOnLineCount(qdCode));
    }

    @Override
    public int updateUserClientIdIsNull(String clientid) {
        return baseMapper.updateUserClientIdIsNull(clientid);
    }

    @Override
    public Result insertUserRole(UserEntity userEntity) {
        UserEntity oldUser = queryByPhone(userEntity.getPhone());
        if (oldUser != null) {
            if (userEntity.getIsRecommend() != null && userEntity.getIsRecommend() == 1) {
                if (oldUser.getIsRecommend() != null && oldUser.getIsRecommend() == 1) {
                    return Result.error("当前用户已经推荐人了");
                } else if (oldUser.getIsChannel() != null && oldUser.getIsChannel() == 1) {
                    return Result.error("当前用户已经剧荐管了,不能成为推荐人");
                }
            }

            if (userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
                if (oldUser.getIsChannel() != null && oldUser.getIsChannel() == 1) {
                    return Result.error("当前用户已经剧荐管了");
                } else if (oldUser.getIsRecommend() != null && oldUser.getIsRecommend() == 1) {
                    return Result.error("当前用户已经推荐人了,不能成为剧荐管");
                }
                if (StringUtils.isNotEmpty(oldUser.getQdCode())) {
                    return Result.error("该用户手机号已在联盟中，不能升级剧荐官,请更换手机号！");
                }
                UserEntity userEntity1 = queryByInvitationCode(userEntity.getInviterCode());
                userEntity.setInviterCode(null);
                userEntity.setRecommendUserId(userEntity1.getUserId());
            }

            if (StringUtils.isNotEmpty(userEntity.getChannelCode())) {
                UserEntity userEntity1 = queryByQdCode(userEntity.getChannelCode());
                if (userEntity1 != null) {
                    return Result.error("该渠道码已经使用过了！");
                }
            }

            if (StringUtils.isEmpty(userEntity.getInviterCode())) {
                userEntity.setInviterCode(oldUser.getInviterCode());
            }
            userEntity.setUserId(oldUser.getUserId());
            baseMapper.updateById(userEntity);

        } else {

            if (StringUtils.isNotEmpty(userEntity.getChannelCode())) {
                UserEntity userEntity1 = queryByQdCode(userEntity.getChannelCode());
                if (userEntity1 != null) {
                    return Result.error("该渠道码已经使用过了！");
                }
            }
            if (userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
                UserEntity userEntity1 = queryByInvitationCode(userEntity.getInviterCode());
                userEntity.setInviterCode(null);
                userEntity.setRecommendUserId(userEntity1.getUserId());
            }
            userEntity.setInviterCode(commonInfoService.findOne(88).getValue());
            userEntity.setCreateTime(DateUtils.format(new Date()));
            userEntity.setStatus(1);
            userEntity.setPassword(DigestUtils.sha256Hex(userEntity.getPhone()));
            baseMapper.insert(userEntity);
            userEntity.setInvitationCode(InvitationCodeUtil.toSerialCode(userEntity.getUserId()));
            baseMapper.updateById(userEntity);
        }

        if (userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
            UserEntity userEntity1 = selectUserById(userEntity.getRecommendUserId());
            if (userEntity1 != null) {
                // 增加积分
                userIntegralService.updateIntegral(1, userEntity1.getUserId(), 1);
                UserIntegralDetails userIntegralDetails = new UserIntegralDetails();
                userIntegralDetails.setClassify(1);
                userIntegralDetails.setContent("邀请剧荐管：" + userEntity.getUserName());
                userIntegralDetails.setCreateTime(DateUtils.format(new Date()));
                userIntegralDetails.setNum(1);
                userIntegralDetails.setType(1);
                userIntegralDetails.setUserId(userEntity1.getUserId());
                userIntegralDetailsDao.insert(userIntegralDetails);
            }
        }

        return Result.success();
    }

    @Override
    public int selectChannelCountAndIsRecommend() {
        return baseMapper.selectChannelCountAndIsRecommend();
    }

    @Override
    public int selectChannelCountByRecommendUserId(Long userId) {
        return baseMapper.selectChannelCountByRecommendUserId(userId);
    }

    @Override
    public int selectAgencyByQdCodeTime(Integer classify, String qdCode, String time) {
        return baseMapper.selectAgencyByQdCodeTime(classify, qdCode, time);
    }

    @Override
    public int selectAgencyByQdCodeTimes(Integer classify, String qdCode, String startTime, String endTime) {
        return baseMapper.selectAgencyByQdCodeTimes(classify, qdCode, startTime, endTime);
    }

    @Override
    public int queryActiveUserCountByInviterCode(String inviterCode) {
        return baseMapper.queryActiveUserCountByInviterCode(inviterCode);
    }

    @Override
    public int queryMoneyUserCountByInviterCode(String inviterCode) {
        return baseMapper.queryMoneyUserCountByInviterCode(inviterCode);
    }

    @Override
    public int queryAgencyUserCountByInviterCode(String inviterCode) {
        return baseMapper.queryAgencyUserCountByInviterCode(inviterCode);
    }

    @Override
    public int queryUserCountByInviterCode(String inviterCode) {
        return baseMapper.queryUserCountByInviterCode(inviterCode);
    }


    @Scheduled(cron = "0 */1 * * * ?")
    public void updateUserAgencyByEndTime() {
        baseMapper.updateUserAgencyByEndTime();
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void updateUserNum() {
        baseMapper.updateUserNum();
    }


}
