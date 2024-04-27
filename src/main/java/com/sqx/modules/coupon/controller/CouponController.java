package com.sqx.modules.coupon.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.Coupon;
import com.sqx.modules.coupon.service.CouponService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "邀请码", tags = {"邀请码"})
@RequestMapping(value = "/coupon")
public class CouponController extends AbstractController {
    @Autowired
    private CouponService couponService;

    @PostMapping("/insertCoupon")
    @ApiOperation("新增优惠券")
    public Result insertInviter(@RequestBody Coupon coupon){
        return couponService.insertCoupon(coupon);
    }
    @PostMapping("/updateCoupon")
    @ApiOperation("修改优惠券")
    public Result updateCoupon(@RequestBody Coupon coupon){
        return couponService.updateCoupon(coupon);
    }
    @GetMapping("/deleteCoupon")
    @ApiOperation("删除优惠券")
    public Result deleteCoupon(Long id){
        return couponService.deleteCoupon(id);
    }
    @GetMapping("/selectCoupon")
    @ApiOperation("优惠券列表")
    public Result selectCoupon(Integer page, Integer limit, String couponName){
        return couponService.selectCoupon(page,limit,couponName);
    }
    @GetMapping("/selectOne")
    @ApiOperation("优惠券列表")
    public Result selectOne(Long id){
        return couponService.selectOne(id);
    }
}
