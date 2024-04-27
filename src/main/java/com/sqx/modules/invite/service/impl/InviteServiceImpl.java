package com.sqx.modules.invite.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.dao.InviteDao;
import com.sqx.modules.invite.entity.Invite;
import com.sqx.modules.invite.service.InviteAwardService;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 邀请记录
 */
@Service
public class InviteServiceImpl extends ServiceImpl<InviteDao, Invite> implements InviteService {


    @Autowired
    private InviteDao inviteDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private InviteAwardService inviteAwardService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public PageUtils selectInviteList(int page,int limit,Integer state,Long userId){
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        if(state==null || state==-1){
            state=null;
        }
        return new PageUtils(inviteDao.selectInviteList(pages,state,userId));
    }


    @Override
    public PageUtils selectInviteUser(int page, int limit, Long userId, Integer state,Integer userType) {
        Page<Map<String, Object>> pages = new Page<>(page, limit);
        return new PageUtils(inviteDao.selectInviteUser(pages, userId, state,userType));
    }

    @Override
    public Integer selectInviteByUserIdCountNotTime(Long userId) {
        return inviteDao.selectInviteByUserIdCountNotTime(userId);
    }

    @Override
    public Integer selectInviteByUserIdCount(Long userId, Date startTime, Date endTime) {
        return inviteDao.selectInviteByUserIdCount(userId,startTime,endTime);
    }

    @Override
    public Double selectInviteByUserIdSum(Long userId, Date startTime, Date endTime) {
        return inviteDao.selectInviteByUserIdSum(userId,startTime,endTime);
    }

    @Override
    public Double sumInviteMoney(String time, Integer flag) {
        return inviteDao.sumInviteMoney(time,flag);
    }

    @Override
    public PageUtils inviteAnalysis(int page,int limit, String time, Integer flag) {
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(inviteDao.inviteAnalysis(pages,time,flag));
    }

    @Override
    public Integer selectInviteCount(Integer state,Long userId){
        if(state==null || state==-1){
            state=null;
        }
        return inviteDao.selectInviteCount(state,userId);
    }

    @Override
    public Double selectInviteSum(Integer state, Long userId) {
        if(state==null || state==-1){
            state=null;
        }
        return inviteDao.selectInviteSum(state,userId);
    }


    @Override
    public int saveBody(Long userId, UserEntity userEntity){
        String format = DateUtils.format(new Date());
        Invite invite=new Invite();
        invite.setState(0);
        invite.setMoney(0.00);
        invite.setUserId(userEntity.getUserId());
        invite.setInviteeUserId(userId);
        invite.setCreateTime(format);
        inviteDao.insert(invite);
        UserEntity user=new UserEntity();
        user.setUserId(userId);
        user.setInviterCode(userEntity.getInvitationCode());
        userService.updateById(user);
        /*
        String value = commonInfoService.findOne(813).getValue();
        if("是".equals(value)){
            //获取邀请人的邀请数量
            int inviterCount = userService.queryInviterCount(userEntity.getInvitationCode());
            InviteAward inviteAward = inviteAwardService.getOne(new QueryWrapper<InviteAward>().eq("invite_count", inviterCount));
            if(inviteAward!=null){
                if(inviteAward.getInviteMonth()==0){
                    userVipService.update(null, Wrappers.<UserVip>lambdaUpdate()
                            .set(UserVip::getIsVip,2)
                            .set(UserVip::getEndTime,null)
                            .set(UserVip::getVipType,1)
                            .eq(UserVip::getUserId,userEntity.getUserId()));
                }else{
                    UserVip userVip = userVipService.selectUserVipByUserId(userEntity.getUserId());
                    Calendar calendar=Calendar.getInstance();
                    if(userVip!=null && userVip.getIsVip()==2){
                        Date date = DateUtils.stringToDate(userVip.getEndTime(), DateUtils.DATE_TIME_PATTERN);
                        calendar.setTime(date);
                    }else{
                        userVip=new UserVip();
                        userVip.setUserId(userEntity.getUserId());
                        userVip.setCreateTime(DateUtils.format(new Date()));
                    }
                    userVip.setIsVip(2);
                    userVip.setVipType(1);
                    calendar.add(Calendar.MONTH,inviteAward.getInviteMonth());
                    userVip.setEndTime(DateUtils.format(calendar.getTime()));
                    if(userVip.getVipId()!=null){
                        userVipService.updateById(userVip);
                    }else{
                        userVipService.save(userVip);
                    }
                }
            }
        }
*/
        return 1;
    }


    /**
     * 分销
     * @param userEntity  购买的用户
     * @param classify  1支付短剧 2购买达人
     * @param price  价格
     * @return
     */
    @Override
    public Map updateInvite(UserEntity userEntity,String ordersNo, Integer classify, BigDecimal price) {
        Map<String,Object> result=new HashMap<>();
        BigDecimal memberPrice=BigDecimal.ZERO;
        BigDecimal channelMoney=BigDecimal.ZERO;
        if(classify==1){
            //购买短剧 获取用户的上级
            UserEntity shangUser = userService.queryByInvitationCode(userEntity.getInviterCode());
            if(shangUser!=null && shangUser.getAgencyIndex()!=null){
                //判断上级是不是达人 或者剧达人
                if(shangUser.getAgencyIndex()==1){
                    BigDecimal money = new BigDecimal(commonInfoService.findOne(452).getValue()).multiply(price).setScale(2, BigDecimal.ROUND_DOWN);
                    inviteMoneyService.updateInviteMoneySum(money.doubleValue(),shangUser.getUserId());
                    UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                    userMoneyDetails.setOrdersNo(ordersNo);
                    userMoneyDetails.setMoney(money);
                    userMoneyDetails.setPayMoney(price);
                    userMoneyDetails.setClassify(10);
                    userMoneyDetails.setUserId(shangUser.getUserId());
                    userMoneyDetails.setByUserId(userEntity.getUserId());
                    userMoneyDetails.setTitle("梵会员直推看剧奖励");
                    userMoneyDetails.setContent("获得奖励："+money);
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                    userMoneyDetailsService.save(userMoneyDetails);
                    result.put("memberUserId",shangUser.getUserId());
                    result.put("memberMoney",money);
                    result.put("memberType",1);
                    memberPrice=money;
                }else if(shangUser.getAgencyIndex()==2){
                    BigDecimal money = new BigDecimal(commonInfoService.findOne(453).getValue()).multiply(price).setScale(2, BigDecimal.ROUND_DOWN);
                    inviteMoneyService.updateInviteMoneySum(money.doubleValue(),shangUser.getUserId());
                    UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                    userMoneyDetails.setOrdersNo(ordersNo);
                    userMoneyDetails.setMoney(money);
                    userMoneyDetails.setPayMoney(price);
                    userMoneyDetails.setClassify(20);
                    userMoneyDetails.setUserId(shangUser.getUserId());
                    userMoneyDetails.setByUserId(userEntity.getUserId());
                    userMoneyDetails.setTitle("剧达人直推看剧奖励");
                    userMoneyDetails.setContent("获得奖励："+money);
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                    userMoneyDetailsService.save(userMoneyDetails);
                    result.put("memberUserId",shangUser.getUserId());
                    result.put("memberMoney",money);
                    result.put("memberType",2);
                    memberPrice=money;
                }
            }
            //判断用户是否有剧荐管
            if(StringUtils.isNotEmpty(userEntity.getQdCode())){
                shangUser = userService.queryByQdCode(userEntity.getQdCode());
                if(shangUser!=null && shangUser.getIsChannel()!=null && shangUser.getIsChannel()==1){
                    //如果是剧荐管 进行剧荐管分佣
                    BigDecimal money = new BigDecimal(commonInfoService.findOne(455).getValue()).multiply(price).setScale(2, BigDecimal.ROUND_DOWN);
                    inviteMoneyService.updateInviteMoneySum(money.doubleValue(),shangUser.getUserId());
                    UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                    userMoneyDetails.setOrdersNo(ordersNo);
                    userMoneyDetails.setMoney(money);
                    userMoneyDetails.setPayMoney(price);
                    userMoneyDetails.setClassify(30);
                    userMoneyDetails.setUserId(shangUser.getUserId());
                    userMoneyDetails.setByUserId(userEntity.getUserId());
                    userMoneyDetails.setTitle("剧荐管联盟看剧奖励");
                    userMoneyDetails.setContent("获得奖励："+money);
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                    userMoneyDetailsService.save(userMoneyDetails);
                    result.put("channelUserId",shangUser.getUserId());
                    result.put("channelMoney",money);
                    channelMoney=money;
                }
            }

        }else{
            UserEntity shangUser = userService.queryByInvitationCode(userEntity.getInviterCode());
            if(shangUser!=null && shangUser.getAgencyIndex()!=null){
                //判断上级是不是达人 或者剧达人
                if(shangUser.getAgencyIndex()==2){
                    BigDecimal money = new BigDecimal(commonInfoService.findOne(454).getValue()).multiply(price).setScale(2, BigDecimal.ROUND_DOWN);
                    userMoneyService.updateMoney(1,shangUser.getUserId(),money.doubleValue());
                    UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                    userMoneyDetails.setOrdersNo(ordersNo);
                    userMoneyDetails.setMoney(money);
                    userMoneyDetails.setPayMoney(price);
                    userMoneyDetails.setClassify(21);
                    userMoneyDetails.setUserId(shangUser.getUserId());
                    userMoneyDetails.setByUserId(userEntity.getUserId());
                    userMoneyDetails.setTitle("剧达人直推达人奖励");
                    userMoneyDetails.setContent("获得奖励："+money);
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                    userMoneyDetailsService.save(userMoneyDetails);
                    result.put("memberUserId",shangUser.getUserId());
                    result.put("memberMoney",money);
                    result.put("memberType",2);
                    memberPrice=money;
                }
            }
        }
        BigDecimal pingMoney=price.subtract(memberPrice).subtract(channelMoney);
        result.put("pingMoney",pingMoney);
        return result;
    }


}
