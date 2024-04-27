package com.sqx.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.pay.dao.CashOutDao;
import com.sqx.modules.pay.entity.CashOut;
import com.sqx.modules.pay.service.CashOutService;
import com.sqx.modules.sys.entity.SysUserEntity;
import com.sqx.modules.sys.service.SysUserService;
import com.sqx.modules.utils.AmountCalUtils;
import com.sqx.modules.utils.excel.ExcelData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;
import weixin.popular.support.TokenManager;

import javax.websocket.SendResult;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 提现申请记录
 */
@Service
public class CashOutServiceImpl extends ServiceImpl<CashOutDao, CashOut> implements CashOutService {

    /**
     * 提现申请记录
     */
    @Autowired
    private CashOutDao cashOutDao;
    /**
     * 通用配置
     */
    @Autowired
    private CommonInfoService commonInfoService;
    /**
     * app用户
     */
    @Autowired
    private UserService userService;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public PageUtils selectCashOutList(Integer page,Integer limit,CashOut cashOut) {
        return new PageUtils(baseMapper.selectCashOutPage(new Page<>(page,limit),cashOut));
    }


    @Override
    public ExcelData excelPayDetails(CashOut cashOut1) {
        List<CashOut> cashOutList = baseMapper.selectCashOutList(cashOut1);
        ExcelData data = new ExcelData();
        data.setName("提现列表");
        List<String> titles = new ArrayList();
        titles.add("编号");titles.add("用户类型");titles.add("用户名称");titles.add("支付宝账号");titles.add("支付宝名称");
        titles.add("提现点券");titles.add("状态");
        titles.add("拒绝原因");titles.add("申请时间");titles.add("转账/拒绝时间");titles.add("转账订单号");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for(CashOut cashOut:cashOutList){
            List<Object> row = new ArrayList();
            row.add(cashOut.getId());
            if (cashOut.getUserType()==1){
                row.add("用户");
                row.add(cashOut.getUserName());
            }else{
                row.add("分销商");
                row.add(cashOut.getSysUserName());
            }
            row.add(cashOut.getZhifubao());row.add(cashOut.getZhifubaoName());
            row.add(cashOut.getMoney());

            if(cashOut.getState()==-1){
                row.add("已拒绝");
            }else if(cashOut.getState()==0){
                row.add("待转账");
            }else{
                row.add("成功");
            }
            row.add(cashOut.getRefund()==null?"":cashOut.getRefund());
            row.add(cashOut.getCreateAt());
            row.add(StringUtils.isEmpty(cashOut.getOutAt())?"":cashOut.getOutAt());
            row.add(cashOut.getOrderNumber());
            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }


    @Override
    public CashOut selectById(Long id) {
        return cashOutDao.selectById(id);
    }

    @Override
    public int saveBody(CashOut cashOut) {
        return cashOutDao.insert(cashOut);
    }


    @Override
    public int update(CashOut cashOut) {
        return cashOutDao.updateById(cashOut);
    }


    @Override
    public void cashOutSuccess(String openId, String date, String money, String payWay, String url) {
        UserEntity userByWxId = userService.queryByWxOpenId(openId);
        if(userByWxId!=null){
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setState(String.valueOf(5));
            messageInfo.setIsSee("0");
            messageInfo.setContent("您好，您的提现转账成功，请注意查收！提现点券【" + money + "元】！支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服");
            messageInfo.setTitle("提现成功通知");
            messageInfo.setUserName(userByWxId.getUserName());
            messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            messageInfo.setCreateAt(sdf.format(now));
            messageInfoDao.insert(messageInfo);
            if (userByWxId.getClientid() != null) {
                userService.pushToSingle("提现成功通知", "您好，您的提现转账成功，请注意查收！提现点券【" + money + "元】！支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服", userByWxId.getClientid());
            }
        }


        CommonInfo three = commonInfoService.findOne(39);
        String apkey = "";
        if (three != null) {
            apkey = three.getValue();
        }
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        data.put("first", new TemplateMessageItem("您好，您的提现转账成功，请注意查收", "#d71345"));
        data.put("keyword1", new TemplateMessageItem(money + " 元", "#d71345"));
        data.put("keyword2", new TemplateMessageItem(date, "#d71345"));
        data.put("remark", new TemplateMessageItem("支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服", null));
        sendWxMessage(apkey, data, openId, url);
    }

    /**
     * 退款成功通知
     *
     * @param
     * @param date
     * @param money
     * @param url
     */
    @Override
    public void refundSuccess(UserEntity userByWxId, String date, String money, String url, String content) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState(String.valueOf(5));
        messageInfo.setIsSee("0");
        messageInfo.setContent(content);
        messageInfo.setTitle("提现失败提醒");
        messageInfo.setUserName(userByWxId.getUserName());
        messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setCreateAt(sdf.format(now));
        messageInfoDao.insert(messageInfo);
        if (userByWxId.getClientid() != null) {
            userService.pushToSingle("提现失败提醒", content, userByWxId.getClientid());
        }
        CommonInfo three = commonInfoService.findOne(77);
        String apkey = "";
        if (three != null) {
            apkey = three.getValue();
        }
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        data.put("first", new TemplateMessageItem("您好，您发起的提现失败了", "#d71345"));
        data.put("keyword1", new TemplateMessageItem(money + " 元", "#d71345"));
        data.put("keyword2", new TemplateMessageItem(date, "#d71345"));
        data.put("keyword3", new TemplateMessageItem(content, "#d71345"));
        data.put("remark", new TemplateMessageItem("请您按照失败原因修改相关信息后，重新提现！", null));
        sendWxMessage(apkey, data, userByWxId.getOpenId(), url);
    }


    @Override
    public Double sumMoney(String time, Integer flag) {
        return cashOutDao.sumMoney(time, flag);
    }

    @Override
    public Integer countMoney(String time, Integer flag) {
        return cashOutDao.countMoney(time, flag);
    }

    @Override
    public Integer stayMoney(String time, Integer flag) {
        return cashOutDao.stayMoney(time, flag);
    }

    @Override
    public void updateMayMoney(int i, Long userId, Double money) {
        cashOutDao.updateMayMoney(i,userId,money);
    }


    @Override
    public List<CashOut> selectCashOutLimit3() {
        return cashOutDao.selectCashOutLimit3();
    }

    private void sendWxMessage(String templateId, LinkedHashMap<String, TemplateMessageItem> data, String openid, String url) {
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(openid);
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(data);
        templateMessage.setUrl(url);
        TemplateMessageResult templateMessageResult = MessageAPI.messageTemplateSend(getWxToken(), templateMessage);
        if (templateMessageResult.isSuccess()) {
            new SendResult();
        } else {
            new SendResult();
        }
    }

    private String getWxToken() {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            return TokenManager.getToken(one.getValue());
        } catch (Exception e) {
            throw new RuntimeException("GET_ACCESS_TOKEN_FAIL");
        }
    }


    @Override
    @Transactional
    public Result cashMoney(Long userId, Double money,Integer classify){
        if(money==null || money<=0.00){
            return Result.error("请不要输入小于0的数字,请输入正确的提现点券！");
        }
        //最低提现点券
        CommonInfo one = commonInfoService.findOne(112);
        if(one!=null && money<Double.parseDouble(one.getValue())){
            return Result.error("输入点券不满足最低提现点券，请重新输入！");
        }
        //最高提现点券
        CommonInfo one2 = commonInfoService.findOne(153);
        if(one2!=null && money>=Double.parseDouble(one2.getValue())){
            return Result.error(-100,"输入点券过大，不能大于"+one2.getValue()+"，请重新输入！");
        }
        String value = commonInfoService.findOne(110).getValue();
        int num = Integer.parseInt(value);
        String format = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
        Integer count = baseMapper.selectCount(new QueryWrapper<CashOut>().like("create_at", format));
        if(count>num){
            return Result.error("今日最大可提现："+num);
        }

        //手续费
        CommonInfo one1 = commonInfoService.findOne(152);
        Double mul = AmountCalUtils.mul(money, Double.parseDouble(one1.getValue()));
        if(mul<0.01){
            mul=0.01;
        }
//        UserMoney userMoney=userMoneyService.selectUserMoneyByUserId(userId);
        InviteMoney inviteMoney = inviteMoneyService.selectInviteMoneyByUserId(userId);
        UserEntity userEntity = userService.selectUserById(userId);
        //提现判断点券是否足够
        Double moneySum = AmountCalUtils.add(new BigDecimal(money), new BigDecimal(mul)).doubleValue(); //点券=提现点券+手续费
        if(inviteMoney.getMoney()>=moneySum){ //用户点券足够
            //扣除可提现点券
            inviteMoneyService.updateInviteMoneyCashOut(2,moneySum,userId);
            //增加点券操作记录
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setTitle("提现："+money);
            if(classify==null || classify==1){
                userMoneyDetails.setContent("支付宝提现："+money+"，手续费："+mul+",总计："+moneySum);
            }else{
                userMoneyDetails.setContent("微信提现："+money+"，手续费："+mul+",总计："+moneySum);
            }
            userMoneyDetails.setType(2);
            userMoneyDetails.setClassify(2);
            userMoneyDetails.setMoney(new BigDecimal(moneySum));
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userMoneyDetails.setCreateTime(sdf.format(new Date()));
            userMoneyDetailsService.save(userMoneyDetails);
            CashOut cashOut = new CashOut();
            cashOut.setState(0);
            cashOut.setZhifubao(userEntity.getZhiFuBao());
            cashOut.setZhifubaoName(userEntity.getZhiFuBaoName());
            cashOut.setMoney(money.toString());
            cashOut.setCreateAt(sdf.format(new Date()));
            cashOut.setUserId(userEntity.getUserId());
            cashOut.setRate(mul);
            cashOut.setOrderNumber(String.valueOf(System.currentTimeMillis()));
            cashOut.setUserType(1);
            cashOut.setClassify(classify);
            cashOut.setWxImg(userEntity.getWxImg());
            baseMapper.insert(cashOut);
//            inviteMoneyDao.updateInviteMoneyCashOut(1,money,userId);
            return Result.success("提现成功，将在三个工作日内到账，请耐心等待！");
        }else{
            return Result.error("点券不足，请输入正确的提现点券！");
        }
    }

    @Override
    @Transactional
    public Result sysCashMoney(Long userId, Double money){
        if(money==null || money<=0.00){
            return Result.error("请不要输入小于0的数字,请输入正确的提现点券！");
        }
        //最低提现点券
        CommonInfo one = commonInfoService.findOne(112);
        if(one!=null && money<Double.parseDouble(one.getValue())){
            return Result.error("输入点券不满足最低提现点券，请重新输入！");
        }
        //最高提现点券
        CommonInfo one2 = commonInfoService.findOne(153);
        if(one2!=null && money>=Double.parseDouble(one2.getValue())){
            return Result.error(-100,"输入点券过大，不能大于"+one2.getValue()+"，请重新输入！");
        }
        //手续费
        CommonInfo one1 = commonInfoService.findOne(152);
        Double mul = AmountCalUtils.mul(money, Double.parseDouble(one1.getValue()));
        if(mul<0.01){
            mul=0.01;
        }
        UserMoney userMoney=userMoneyService.selectSysUserMoneyByUserId(userId);

        SysUserEntity sysUserEntity = sysUserService.getById(userId);
        //提现判断点券是否足够
        Double moneySum = AmountCalUtils.add(new BigDecimal(money), new BigDecimal(mul)).doubleValue(); //点券=提现点券+手续费
        if(userMoney.getMoney().doubleValue()>=moneySum){ //用户点券足够
            //扣除可提现点券
            userMoneyService.updateSysMoney(2,userId,moneySum);
            //增加点券操作记录
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setSysUserId(userId);
            userMoneyDetails.setTitle("提现："+money);
            userMoneyDetails.setContent("提现："+money+"，手续费："+mul+",总计："+moneySum);
            userMoneyDetails.setType(2);
            userMoneyDetails.setClassify(2);
            userMoneyDetails.setMoney(new BigDecimal(moneySum));
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userMoneyDetails.setCreateTime(sdf.format(new Date()));
            userMoneyDetailsService.save(userMoneyDetails);
            CashOut cashOut = new CashOut();
            cashOut.setState(0);
            cashOut.setZhifubao(sysUserEntity.getZhiFuBao());
            cashOut.setZhifubaoName(sysUserEntity.getZhiFuBaoName());
            cashOut.setMoney(money.toString());
            cashOut.setCreateAt(sdf.format(new Date()));
            cashOut.setUserId(sysUserEntity.getUserId());
            cashOut.setRate(mul);
            cashOut.setOrderNumber(String.valueOf(System.currentTimeMillis()));
            cashOut.setSysUserId(userId);
            cashOut.setUserType(2);
            baseMapper.insert(cashOut);
//            inviteMoneyDao.updateInviteMoneyCashOut(1,money,userId);
            return Result.success("提现成功，将在三个工作日内到账，请耐心等待！");
        }else{
            return Result.error("点券不足，请输入正确的提现点券！");
        }
    }

}
