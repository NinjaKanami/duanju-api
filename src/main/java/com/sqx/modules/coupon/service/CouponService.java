package com.sqx.modules.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.Coupon;

public interface CouponService extends IService<Coupon> {
    Result insertCoupon(Coupon coupon);
    Result updateCoupon(Coupon coupon);
    Result deleteCoupon(Long id);
    Result selectCoupon(Integer page, Integer limit,String couponName);
    Result selectOne(Long id);
}
