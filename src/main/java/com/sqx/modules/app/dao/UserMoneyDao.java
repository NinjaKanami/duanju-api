package com.sqx.modules.app.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.app.entity.UserMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMoneyDao extends BaseMapper<UserMoney> {

    void updateMayMoney(@Param("type") Integer type, @Param("userId")Long userId, @Param("money") Double money);

    void updateSysMoney(@Param("type") Integer type, @Param("sysUserId")Long sysUserId, @Param("money") Double money);

}
