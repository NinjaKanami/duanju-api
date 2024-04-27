package com.sqx.modules.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.app.entity.Msg;

/**
 * 验证码
 *
 */
public interface MsgService extends IService<Msg> {

	Msg findByPhone(String phone);

	Msg findByPhoneAndCode(String phone, String msg);

}
