package com.sqx.modules.message.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.sys.entity.SysUserEntity;

import java.util.Map;

public interface MessageService extends IService<MessageInfo> {

    int updateSendState(Long userId, Integer state);

    PageUtils selectMessageList(Map<String,Object> params);

    int saveBody(MessageInfo messageInfo);

    Result update(MessageInfo messageInfo, SysUserEntity user);

    int delete(Long id);

    MessageInfo selectMessageById(Long id);


}
