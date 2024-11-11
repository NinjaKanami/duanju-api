package com.sqx.modules.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.message.constant.MessageConstant;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageInfoDao, MessageInfo> implements MessageService {

    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;

    @Override
    public int updateSendState(Long userId, Integer state) {
        return messageInfoDao.updateSendState(userId, state);
    }

    @Override
    public PageUtils selectMessageList(Map<String, Object> params) {
        Long userId = (Long) params.get("userId");
        Integer state = (Integer) params.get("state");
        Integer type = (Integer) params.get("type");
        IPage<MessageInfo> page = this.page(
                new Query<MessageInfo>().getPage(params),
                new QueryWrapper<MessageInfo>()
                        .eq(userId != null, "user_id", userId)
                        .eq(state != null, "state", state)
                        .eq(type != null, "type", type).orderByDesc("create_at")
        );
        List<MessageInfo> records = page.getRecords();
        for (MessageInfo messageInfo : records) {
            if (messageInfo.getUserId() != null) {
                messageInfo.setUserEntity(userService.selectUserById(Long.parseLong(messageInfo.getUserId())));
            }
        }
        return new PageUtils(page);
    }

    @Override
    public int saveBody(MessageInfo messageInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setCreateAt(sdf.format(now));
        return messageInfoDao.insert(messageInfo);
    }

    @Override
    public Result update(MessageInfo messageInfo, SysUserEntity user) {
        if ("20".equals(messageInfo.getState())) {
            MessageInfo oldMessageInfo = messageInfoDao.selectById(messageInfo.getId());
            if ("1".equals(oldMessageInfo.getIsSee())) {
                return Result.error("当月已经发放了，无法修改");
            }
            String format = DateUtils.format(new Date());
            String content = messageInfo.getContent();
            messageInfo.setContent(user.getUsername() + "：在“" + format + "”修改平台利润为：" + content);
            messageInfo.setCreateAt(format);
            CommonInfo one = commonInfoService.findOne(883);
            one.setValue(content);
            one.setMax(messageInfo.getSendTime() + ",0");
            commonInfoService.updateById(one);
        }

        messageInfoDao.updateById(messageInfo);
        return Result.success();
    }

    @Override
    public int delete(Long id) {
        return messageInfoDao.deleteById(id);
    }

    @Override
    public MessageInfo selectMessageById(Long id) {
        return messageInfoDao.selectById(id);
    }

    @Override
    public int batchSaveBody(List<MessageInfo> messageInfo) {
        int totalCount = 0;
        for (MessageInfo msg : messageInfo) {
            if (messageInfoDao.insert(msg) > 0) {
                totalCount++;
            }
        }
        return totalCount;
    }

    @Override
    public int batchSaveBodyWithTx(List<MessageInfo> messageInfo) {
        return messageInfoDao.batchSaveBodyWithTx(messageInfo);
    }

    /**
     * 更新消息状态为已读，该方法将 userId 和 messageId 对应起来，避免出现
     * 用户更新其他用户消息的情况
     *
     * @param userId    用户id
     * @param messageId 消息id
     * @return 更新数量
     */
    @Override
    public int userReadMessage(Long userId, Long messageId) {
        return messageInfoDao.updateUserMessageIsSee(messageId, String.valueOf(userId), String.valueOf(MessageConstant.IsSeeYes));
    }


}
