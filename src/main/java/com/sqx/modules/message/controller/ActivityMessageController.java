package com.sqx.modules.message.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.message.entity.ActivityMessageInfo;
import com.sqx.modules.message.service.ActivityMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/ActivityMessage")
public class ActivityMessageController {

    @Autowired
    private ActivityMessageService activityMessageService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台公告详情")
    @ResponseBody
    public Result getMessage(@PathVariable Integer id) {
        return Result.success().put("data",activityMessageService.findOne(Long.valueOf(id)));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("管理平台和用户端通用接口添加公告")
    @ResponseBody
    public Result addMessage(@RequestBody ActivityMessageInfo messageInfo) {
        activityMessageService.saveBody(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("管理平台修改公告接口")
    @ResponseBody
    public Result uUpdate(@RequestBody ActivityMessageInfo messageInfo) {
        activityMessageService.updateBody(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台删除公告接口")
    public Result deleteMessage(@PathVariable int id) {
        activityMessageService.delete(id);
        return Result.success();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation("管理平台获取全部公告接口")
    @ResponseBody
    public Result getMessageList() {
        List<ActivityMessageInfo> all = activityMessageService.findAll();
        return Result.success().put("data",all);
    }

    @RequestMapping(value = "/page/{state}/{page}/{limit}", method = RequestMethod.GET)
    @ApiOperation("管理平台分页查询公告接口")
    @ResponseBody
    public Result getBodyPage(@PathVariable String state, @PathVariable Integer page, @PathVariable int limit) {
        return Result.success().put("data",activityMessageService.find(state, page,limit));
    }

    @RequestMapping(value = "/type/{type}/{page}/{limit}", method = RequestMethod.GET)
    @ApiOperation("管理平台通过类型获取接口 type1为公告2位用户反馈 3为系统消息 4为订单信息 5为用户消息 6客服消息")
    @ResponseBody
    public Result findType(@PathVariable Integer type, @PathVariable Integer page, @PathVariable int limit) {
        return Result.success().put("data",activityMessageService.findType(type, page,limit));
    }

    @RequestMapping(value = "/findType/{userId}/{type}/{page}/{limit}", method = RequestMethod.GET)
    @ApiOperation("用户端获取消息列表 type 4为订单信息 5为用户消息")
    @ResponseBody
    public Result findType(@PathVariable String userId, @PathVariable String type, @PathVariable Integer page, @PathVariable int limit) {
        return Result.success().put("data",activityMessageService.findTypeByUserId(type, userId,page,limit));
    }

}