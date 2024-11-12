package com.sqx.modules.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.message.entity.MessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fang
 * @date 2020/7/9
 */
@Mapper
public interface MessageInfoDao extends BaseMapper<MessageInfo> {

    int updateSendState(@Param("userId") Long userId, @Param("state") Integer state);

    /**
     * 修改用户消息的已读状态，使用该方法来将用户id-messageId匹配起来，避免用户更新其他用户的消息状态
     *
     * @param messageId 消息ID
     * @param isSee     已读状态
     * @return 受影响的行数
     */
    int updateUserMessageIsSee(@Param("id") Long messageId, @Param("userId") String userId, @Param("isSee") String isSee);

    /**
     * 批量保存消息(原子性的,要么全部成功,要么全部失败)
     *
     * @param messageInfo 消息列表
     * @return 受影响的行数
     */
    int batchSaveBodyWithTx(@Param("list") List<MessageInfo> messageInfo);
}
