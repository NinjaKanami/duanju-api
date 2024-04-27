package com.sqx.modules.app.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */
public interface UserService extends IService<UserEntity> {

    Result authenticationSuperUser(JSONObject jsonObject, HttpServletRequest request);

    String getUserName();

    Result getNewUserRed(Long userId);


    UserEntity queryByUserName(String userName);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return
     */
    UserEntity queryByPhone(String phone);

    /**
     * 设置渠道标识
     * @param qdCode
     * @return
     */
    UserEntity queryByQdCode(String qdCode);

    /**
     * 根据小程序微信openId查询用户
     *
     * @param openId 微信小程序openId
     * @return
     */
    UserEntity queryByOpenId(String openId);

    UserEntity queryByWxId(String wxId);

    UserEntity queryByDyOpenId(String dyOpenId);

    UserEntity queryByKsOpenId(String ksOpenId);

    /**
     * 根据微信APP openId查询用户
     *
     * @param openId 微信APP openId
     * @return
     */
    UserEntity queryByWxOpenId(String openId);

    /**
     * 根据userId查询用户
     *
     * @param userId userId
     * @return
     */
    UserEntity queryByUserId(Long userId);

	UserEntity queryByInvitationCode(String invitationCode);

	/**
	 * 根据用户appleId查询用户
	 * @param appleId
	 * @return
	 */
	UserEntity queryByAppleId(String appleId);


    Result wxLogin(String code);

    /**
     * 注册或更新用户信息
     *
     * @param userInfo1 用户信息
     * @return 用户信息
     */
    Result wxRegister(UserEntity userInfo1);

    Result dyLogin(String code,String anonymous_code);

    Result dyRegister(UserEntity userInfo1);

    Result ksLogin(String code);

    Result ksRegister(UserEntity userInfo1);

    /**
     * 注册或更新用户信息
     *
     * @param appleId 苹果账号id
     * @return 用户信息
     */
    Result iosRegister(String appleId);

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @param state 验证码类型
     * @return
     */
    Result sendMsg(String phone, String state);

    Result forgetPwd(String pwd, String phone, String msg);

    /**
     * 绑定手机号
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, Integer sysPhone,String inviterCode,String qdCode);

    /**
     * @param phone
     * @param code
     * @param appleId
     * @param platform
     * @param sysPhone
     * @return
     */
    Result iosBindMobile(String phone, String code, String appleId, String platform, Integer sysPhone,String inviterCode,String qdCode);

    Result phoneLogin(String phone);

    Result bindMobile(String phone,String platform, Integer sysPhone,String inviterCode,String qdCode);


    /**
     * 换绑手机号
     *
     * @param phone  手机号
     * @param msg    验证码
     * @param userId 用户id
     * @return
     */
    Result updatePhone(String phone, String msg, Long userId);

    /**
     * 登录token
     *
     * @param user 用户信息
     * @return
     */
    Result getResult(UserEntity user);

    /**
     * app注册或h5注册
     *
     * @param phone    手机号
     * @param msg      验证按
     * @param pwd      密码
     * @param platform 来源 app  h5
     * @return
     */
    Result registerCode(String phone, String msg, String platform, Integer sysPhone,String password,
                        String inviterCode,String wxId,String qdCode);

    Result bindWxOpenPhone(Long userId, String phone, String msg);


    Result wxAppLogin(String wxOpenId, String token);


    /**
     * app或h5登录
     *
     * @param phone 手机号
     * @param pwd   密码
     * @return
     */
    Result login(String phone, String pwd);


    /**
     * 根据 code 获取openId
     *
     * @param code
     * @param userId
     * @return
     */
    Result getOpenId(String code, Long userId);


    /**
     * 根据用户id查询用户
     *
     * @param userId 用户id
     * @return
     */
    UserEntity selectUserById(Long userId);

    void pushToSingle(String title, String content, String clientId);

    PageUtils selectUserPage(Integer page, Integer limit,String phone,Integer sex,String platform,String sysPhone,Integer status,
                             Integer member, String inviterCode, String userName, String invitationCode, String startTime,
                             String endTime,String qdCode,String sysUserName,Integer vipType,String isRecommend,String isChannel, String agencyIndex);

    List<UserEntity> userListExcel(String startTime, String endTime, UserEntity userEntity);

    int queryInviterCount(String inviterCode);

    int queryUserCount(int type,String date,String platform,String qdCode);

    Double queryPayMoney(int type,String qdCode);

    IPage<Map<String, Object>> queryCourseOrder(Page<Map<String, Object>> iPage, int type, String date,Long sysUserId);

    int userMessage( String date, int type,String qdCode,Integer vipType);


    Result selectInviteUserList(Integer page,Integer limit,String userName,String phone);

    Result selectChannelUserListByRecommend(Integer page,Integer limit,Long userId,String userName,String phone);

    Result selectUserListByAgencyIndex(Integer page,Integer limit,Long userId,String invitationCode,String userName,String phone);

    Result selectUserListByInviteCode(Integer page,Integer limit,Long userId,String invitationCode,String search,
                                      Integer classify,Integer sort,String qdCode);

    Result loginByOpenId(String openId);

    Result selectUserOnLineCount(String qdCode);

    int updateUserClientIdIsNull(String clientid);

    Result insertUserRole(UserEntity userEntity);

    int selectChannelCountAndIsRecommend();

    int selectChannelCountByRecommendUserId(Long userId);

    int selectAgencyByQdCodeTime(Integer classify,String qdCode,String time);

    int selectAgencyByQdCodeTimes(Integer classify,String qdCode,String startTime,String endTime);

    int queryActiveUserCountByInviterCode(String inviterCode);

    int queryMoneyUserCountByInviterCode(String inviterCode);

    int queryAgencyUserCountByInviterCode(String inviterCode);

    int queryUserCountByInviterCode(String inviterCode);

}
