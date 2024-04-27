package com.sqx.modules.invite.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.invite.entity.InviteAward;
import com.sqx.modules.invite.service.InviteAwardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "邀请奖励", tags = {"邀请奖励"})
@RequestMapping(value = "/inviteAward")
public class InviteAwardController {

    @Autowired
    private InviteAwardService inviteAwardService;

    @PostMapping("/insertInviteAward")
    @ApiOperation("添加邀请奖励")
    public Result insertInviteAward(@RequestBody InviteAward inviteAward){
        inviteAward.setCreateTime(DateUtils.format(new Date()));
        inviteAwardService.save(inviteAward);
        return Result.success();
    }

    @PostMapping("/updateInviteAward")
    @ApiOperation("修改邀请奖励")
    public Result updateInviteAward(@RequestBody InviteAward inviteAward){
        inviteAwardService.updateById(inviteAward);
        return Result.success();
    }

    @PostMapping("/deleteInviteAward")
    @ApiOperation("删除邀请奖励")
    public Result deleteInviteAward(Long inviteAwardId){
        inviteAwardService.removeById(inviteAwardId);
        return Result.success();
    }

    @GetMapping("/selectInviteAwardList")
    @ApiOperation("查询邀请奖励列表")
    public Result selectInviteAwardList(Integer page,Integer limit){
        return Result.success().put("data",inviteAwardService.page(new Page<>(page,limit),new QueryWrapper<InviteAward>().orderByAsc("invite_count")));
    }

}