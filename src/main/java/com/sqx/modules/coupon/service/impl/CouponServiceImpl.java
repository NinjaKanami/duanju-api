package com.sqx.modules.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.dao.CouponDao;
import com.sqx.modules.coupon.entity.Coupon;
import com.sqx.modules.coupon.service.CouponService;
import org.springframework.stereotype.Service;


@Service
public class CouponServiceImpl extends ServiceImpl<CouponDao, Coupon> implements CouponService {

    @Override
    public Result insertCoupon(Coupon coupon) {
        baseMapper.insert(coupon);
        return Result.success("操作成功");
    }

    @Override
    public Result updateCoupon(Coupon coupon) {
        baseMapper.updateById(coupon);
        return Result.success("操作成功");
    }

    @Override
    public Result deleteCoupon(Long id) {
        baseMapper.deleteById(id);
        return Result.success("操作成功");
    }

    @Override
    public Result selectCoupon(Integer page, Integer limit, String couponName) {
        IPage<Coupon> pages = new Page<>(page, limit);
        QueryWrapper<Coupon> queryWrapper1 = new QueryWrapper<>();
        if(couponName!=null){
            queryWrapper1.eq("coupon_name",couponName);
        }
        pages=baseMapper.selectPage(pages,queryWrapper1);
        return Result.success().put("data",pages.getRecords());
    }

    @Override
    public Result selectOne(Long id) {
        return Result.success().put("data",baseMapper.selectById(id));
    }
}
