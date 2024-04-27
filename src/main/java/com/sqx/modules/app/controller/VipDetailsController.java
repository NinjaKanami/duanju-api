package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserVip;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.app.service.VipDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;

@RestController
@Api(value = "会员管理", tags = {"会员管理"})
@RequestMapping(value = "/vipDetails")
public class VipDetailsController {

    @Autowired
    private VipDetailsService vipDetailsService;
    @Autowired
    private UserVipService userVipService;

    @PostMapping("/sendVip")
    @ApiOperation("赠送会员")
    public Result sendVip(Long userId,Integer num){
        UserVip userVip = userVipService.selectUserVipByUserId(userId);
        Calendar calendar=Calendar.getInstance();
        if(userVip!=null){
            if(userVip.getIsVip()==2){
                Date date = DateUtils.stringToDate(userVip.getEndTime(), "yyyy-MM-dd HH:mm:ss");
                calendar.setTime(date);
            }
        }else{
            userVip=new UserVip();
            userVip.setUserId(userId);
            userVip.setCreateTime(DateUtils.format(new Date()));
        }
        userVip.setVipType(1);
        userVip.setIsVip(2);
        calendar.add(Calendar.DAY_OF_MONTH,num);
        userVip.setEndTime(DateUtils.format(calendar.getTime()));
        if(userVip.getVipId()!=null){
            userVipService.updateById(userVip);
        }else{
            userVipService.save(userVip);
        }
        return Result.success();
    }

    @PostMapping("/deleteVip")
    @ApiOperation("取消会员")
    public Result deleteVip(Long userId){
        UserVip userVip = userVipService.selectUserVipByUserId(userId);
        if(userVip!=null){
            userVipService.removeById(userVip.getVipId());
        }
        return Result.success();
    }


    @ApiParam("添加会员的详情信息")
    @PostMapping("/insertVipDetails")
    public Result insertVipDetails(@RequestBody VipDetails vipDetails) {
        return vipDetailsService.insertVipDetails(vipDetails);
    }


    @ApiParam("修改会员的详情信息")
    @PostMapping("/updateVipDetails")
    public Result updateVipDetails(@RequestBody VipDetails vipDetails) {
        vipDetailsService.updateById(vipDetails);
        return Result.success();
    }

    @ApiParam("删除的详情信息")
    @PostMapping("/deleteVipDetails")
    public Result deleteVipDetails(Long id) {
        vipDetailsService.removeById(id);
        return Result.success();
    }

    @ApiParam("查询会员列表")
    @GetMapping("/selectVipDetailsList")
    public Result selectVipDetailsList(Integer page,Integer limit) {
        return Result.success().put("data",new PageUtils(vipDetailsService.page(new Page<>(page,limit))));
    }

}
