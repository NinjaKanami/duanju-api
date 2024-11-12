package com.sqx.modules.message.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

public interface MessageService extends IService<MessageInfo> {

    int updateSendState(Long userId, Integer state);

    PageUtils selectMessageList(Map<String, Object> params);

    int saveBody(MessageInfo messageInfo);

    Result update(MessageInfo messageInfo, SysUserEntity user);

    int delete(Long id);

    MessageInfo selectMessageById(Long id);

    /**
     * 批量保存通知消息(使用该接口进行插入时，如果中间某个插入出了问题，会继续执行)
     */
    int batchSaveBody(List<MessageInfo> messageInfo);

    /**
     * 原子性的批量保存通知消息，要么全部成功，要么全部失败
     */
    int batchSaveBodyWithTx(List<MessageInfo> messageInfo);

    int userReadMessage(Long userId, Long messageId);

}
