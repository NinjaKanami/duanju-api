package com.sqx.modules.app.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.app.dao.MsgDao;
import com.sqx.modules.app.entity.Msg;
import com.sqx.modules.app.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("MsgService")
public class MsgServiceImpl extends ServiceImpl<MsgDao, Msg> implements MsgService {

	@Autowired
	private MsgDao msgDao;

	@Override
	public Msg findByPhone(String phone){
		return msgDao.findByPhone(phone);
	}

	@Override
	public Msg findByPhoneAndCode(String phone, String msg){
		return msgDao.findByPhoneAndCode(phone,msg);
	}



}
