package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户会员信息", tags = {"用户会员信息"})
@RequestMapping(value = "/app/UserVip")
public class AppUserVipController extends AbstractController {
    @Autowired
    private UserVipService userVipService;
    @Login
    @GetMapping("/selectUserVip")
    @ApiOperation("查询用户会员信息")
    public Result selectUserVip(@RequestAttribute Long userId){
        return Result.success().put("data",userVipService.selectUserVipByUserId(userId));
    }



}
