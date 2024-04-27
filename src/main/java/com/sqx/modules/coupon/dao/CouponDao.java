package com.sqx.modules.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.coupon.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponDao extends BaseMapper<Coupon> {
}
