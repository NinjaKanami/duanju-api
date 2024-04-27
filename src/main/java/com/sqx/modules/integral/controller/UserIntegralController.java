package com.sqx.modules.integral.controller;

import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.integral.service.UserIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(value = "用户积分", tags = {"用户积分"})
@RequestMapping(value = "/integral")
public class UserIntegralController {

    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private UserIntegralDetailsService userIntegralDetailsService;

    @RequestMapping(value = "/selectByUserId", method = RequestMethod.GET)
    @ApiOperation("查看用户积分")
    @ResponseBody
    public Result selectByUserId(Long userId) {
        return Result.success().put("data", userIntegralService.selectById(userId));
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ApiOperation("查看用户积分详细列表信息")
    @ResponseBody
    public Result selectUserIntegralDetailsByUserId(int page, int limit,Long userId) {
        return Result.success().put("data", userIntegralDetailsService.selectUserIntegralDetailsByUserId(page, limit, userId));
    }

    @PostMapping("/updateUserIntegral")
    @ApiOperation("修改积分")
    public Result updateUserIntegral(Long userId, Integer integral,Integer type) {
        userIntegralService.updateIntegral(type,userId,integral);
        UserIntegralDetails userIntegralDetails = new UserIntegralDetails();
        userIntegralDetails.setClassify(2);
        if(type==1){
            userIntegralDetails.setContent("系统增加积分："+integral);
        }else{
            userIntegralDetails.setContent("系统减少积分："+integral);
        }

        userIntegralDetails.setCreateTime(DateUtils.format(new Date()));
        userIntegralDetails.setNum(integral);
        userIntegralDetails.setType(type);
        userIntegralDetails.setUserId(userId);
        userIntegralDetailsService.save(userIntegralDetails);
        return Result.success();
    }

}
