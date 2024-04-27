package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {


    IPage<UserEntity> selectUserPage(@Param("page") Page<UserEntity> page, @Param("search") String search, @Param("sex") Integer sex, @Param("platform") String platform,
                                     @Param("sysPhone") String sysPhone, @Param("status") Integer status, @Param("member") Integer member,
                                     @Param("inviterCode") String inviterCode, @Param("userName") String userName,
                                     @Param("invitationCode") String invitationCode, @Param("startTime") String startTime,
                                     @Param("endTime") String endTime,@Param("qdCode") String qdCode,@Param("sysUserName") String sysUserName,Integer vipType,
                                     @Param("isRecommend") String isRecommend,@Param("isChannel") String isChannel,@Param("agencyIndex") String agencyIndex);

    List<UserEntity> userListExcel(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userEntity") UserEntity userEntity);

    int queryInviterCount(@Param("inviterCode") String inviterCode);

    int queryUserCount(@Param("type") int type, @Param("date") String date,String platform,String qdCode);

    Double queryPayMoney(@Param("type") int type, @Param("date") String date,String qdCode);

    IPage<Map<String, Object>> queryCourseOrder(Page iPage,@Param("type") int type, @Param("date") String date,Long sysUserId);

    int userMessage( String date, int type,String qdCode,Integer vipType);

    int insertUser(UserEntity userEntity);

    IPage<UserEntity> selectInviteUserList(Page<UserEntity> page,String userName,String phone);

    IPage<UserEntity> selectChannelUserListByRecommend(Page<UserEntity> page,Long userId,String userName,String phone);

    IPage<UserEntity> selectUserListByAgencyIndex(Page<UserEntity> page,Long userId,String invitationCode,String userName,String phone);

    IPage<UserEntity> selectUserListByInviteCode(Page<UserEntity> page,Long userId,String invitationCode,String search,
                                                 Integer classify,Integer sort,String qdCode);

    int selectUserOnLineCount(String qdCode);

    int updateUserClientIdIsNull(String clientid);

    int updateUserQdCodeByInviterCode(String qdCode,String inviterCode);

    int updateUserQdCodeByInviterCodeList(String qdCode,String inviterCode);

    int selectChannelCountAndIsRecommend();

    int selectChannelCountByRecommendUserId(Long userId);

    int selectAgencyByQdCodeTime(Integer classify,String qdCode,String time);

    int selectAgencyByQdCodeTimes(Integer classify,String qdCode,String startTime,String endTime);

    int selectAgencyIndexCountByQdCodeByTime(String qdCode,String startTime,String endTime,Integer classify);

    int updateUserAgencyByEndTime();

    int queryActiveUserCountByInviterCode(String inviterCode);

    int queryMoneyUserCountByInviterCode(String inviterCode);

    int queryAgencyUserCountByInviterCode(String inviterCode);

    int queryUserCountByInviterCode(String inviterCode);

    int updateUserNum();

}
