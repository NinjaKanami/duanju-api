package com.sqx.modules.invite.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.invite.dao.InviteMoneyDao;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.service.PayClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service("InviteMoneyService")
public class InviteMoneyServiceImpl extends ServiceImpl<InviteMoneyDao, InviteMoney> implements InviteMoneyService {

	@Autowired
	private InviteMoneyDao inviteMoneyDao;
	@Autowired
	private UserMoneyDetailsService userMoneyDetailsService;
	@Autowired
	private UserMoneyService userMoneyService;
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private PayClassifyService payClassifyService;


	@Override
	public InviteMoney selectInviteMoneyByUserId(Long userId) {
		InviteMoney inviteMoney = inviteMoneyDao.selectInviteMoneyByUserId(userId);
		if(inviteMoney==null){
			inviteMoney=new InviteMoney();
			inviteMoney.setCashOut(0.00);
			inviteMoney.setUserId(userId);
			inviteMoney.setMoney(0.00);
			inviteMoney.setMoneySum(0.00);
			inviteMoneyDao.insert(inviteMoney);
		}
		return inviteMoney;
	}

	@Override
	public int updateInviteMoneySum(Double money, Long userId) {
		selectInviteMoneyByUserId(userId);
		return inviteMoneyDao.updateInviteMoneySum(money,userId);
	}

	@Override
	public int updateInviteMoneySumSub(Double money, Long userId) {
		selectInviteMoneyByUserId(userId);
		return inviteMoneyDao.updateInviteMoneySumSub(money,userId);
	}


	@Override
	public int updateInviteMoneyCashOut(Double money, Long userId) {
		return inviteMoneyDao.updateInviteMoneySum(money,userId);
	}

	@Override
	public int updateInviteMoneyCashOut(Integer type,Double money, Long userId) {
		return inviteMoneyDao.updateInviteMoneyCashOut(type,money,userId);
	}

	@Override
	public Result inviteMoneyConvertUserMoney(UserEntity userEntity, Long payClassifyId){
		PayClassify payClassify = payClassifyService.getById(payClassifyId);
		BigDecimal price = payClassify.getPrice();
		InviteMoney inviteMoney = selectInviteMoneyByUserId(userEntity.getUserId());
		if(inviteMoney.getMoney()<price.doubleValue()){
			return Result.error("收益不足！");
		}
		BigDecimal add = payClassify.getMoney().add(payClassify.getGiveMoney());
		inviteMoneyDao.updateInviteMoneySub(price.doubleValue(),userEntity.getUserId());
		UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
		userMoneyDetails.setUserId(userEntity.getUserId());
		userMoneyDetails.setTitle("收益转余额");
		userMoneyDetails.setContent("减少收益:"+price+",增加余额:"+add);
		userMoneyDetails.setType(2);
		userMoneyDetails.setClassify(2);
		userMoneyDetails.setMoney(price);
		userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
		userMoneyDetailsService.save(userMoneyDetails);
		userMoneyService.updateMoney(1,userEntity.getUserId(),add.doubleValue());
		userMoneyDetails = new UserMoneyDetails();
		userMoneyDetails.setUserId(userEntity.getUserId());
		userMoneyDetails.setTitle("收益转余额");
		userMoneyDetails.setContent("减少收益:"+price+",增加余额:"+add);
		userMoneyDetails.setType(1);
		userMoneyDetails.setClassify(1);
		userMoneyDetails.setMoney(add);
		userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
		userMoneyDetailsService.save(userMoneyDetails);
		//创建订单返回对象
		Orders orders = new Orders();
		//设置订单编号
		orders.setOrdersNo(getGeneralOrder());
		//设置支付点券
		orders.setPayMoney(add);
		//设置订单类型
		orders.setOrdersType(4);
		//设置支付状态
		orders.setStatus(1);
		//设置用户id
		orders.setUserId(userEntity.getUserId());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置创建时间
		orders.setCreateTime(df.format(new Date()));
		orders.setPayTime(orders.getCreateTime());
		//插入到订单表中
		ordersService.save(orders);
		return Result.success();
	}

	public String getGeneralOrder() {
		Date date = new Date();
		String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String format = sdf.format(date);
		return format + newString;
	}

}
