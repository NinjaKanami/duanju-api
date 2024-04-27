package com.sqx.modules.pay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.pay.entity.PayDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author fang
 * @date 2020/7/1
 */
@Mapper
public interface PayDetailsDao extends BaseMapper<PayDetails> {

    PayDetails selectById(@Param("id") Long id);

    PayDetails selectByRemark(@Param("remark") String remark);

    PayDetails selectByOrderId(@Param("orderId") String orderId);

    int updateState(@Param("id") Long id, @Param("state") Integer state, @Param("time") String time, @Param("tradeNo") String tradeNo);

    IPage<Map<String, Object>> selectPayDetails(@Param("page") Page<Map<String, Object>> page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userId") Long userId, @Param("state") Integer state, @Param("userName") String userName, String orderId);

    Double selectSumPay(@Param("createTime") String createTime, @Param("endTime") String endTime, @Param("userId") Long userId);

    Double selectSumMember(@Param("time") String time, @Param("flag") Integer flag);

    IPage<Map<String, Object>> payMemberAnalysis(Page<Map<String, Object>> page, @Param("time") String time, @Param("flag") Integer flag);

    Double selectSumPayByState(@Param("time") String time, @Param("flag") Integer flag, @Param("state") Integer state);

    Double selectSumPayByClassify(@Param("time") String time, @Param("flag") Integer flag, @Param("classify") Integer classify,@Param("payClassify") Integer payClassify);

    IPage<Map<String, Object>> selectUserMemberList(Page<Map<String, Object>> page, @Param("phone") String phone);

    int selectPayCount(Long userId);

    Double instantselectSumPay(@Param("date") String date, @Param("userId") Long userId);


}
