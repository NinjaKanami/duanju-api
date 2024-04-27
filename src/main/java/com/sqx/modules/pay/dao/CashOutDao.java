package com.sqx.modules.pay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.pay.entity.CashOut;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fang
 * @date 2020/7/8
 */
@Mapper
public interface CashOutDao extends BaseMapper<CashOut> {

    List<CashOut> selectCashOutLimit3();

    Double selectCashOutSum(@Param("userId") Long userId, @Param("time") String time);

    Double sumMoney(@Param("time") String time, @Param("flag") Integer flag);

    Integer countMoney(@Param("time") String time, @Param("flag") Integer flag);

    Integer stayMoney(@Param("time") String time, @Param("flag") Integer flag);

    void updateMayMoney(@Param("type") Integer type,@Param("userId")Long userId,@Param("money") Double money);

    Double selectMayMoney(@Param("userId") Long userId);

    IPage<CashOut> selectCashOutPage(Page<CashOut> page, CashOut cashOut);

    List<CashOut> selectCashOutList(CashOut cashOut);

}
