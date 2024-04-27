package com.sqx.modules.message.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.sys.entity.SysUserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/message")
public class MessageController extends AbstractController {

    @Autowired
    private MessageService messageService;


    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(int page, int limit, Long userId,Integer state){
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",userId);
        map.put("state",state);
        return Result.success().put("data",messageService.selectMessageList(map));
    }

    @RequestMapping(value = "/selectMessageByType", method = RequestMethod.GET)
    @ApiOperation("获取消息 type1为公告2位用户反馈 3为系统消息 4为订单信息 5为用户消息 6客服消息 ")
    @ResponseBody
    public Result selectMessageByType(int page, int limit,Integer state){
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",null);
        map.put("state",state);
        return Result.success().put("data",messageService.selectMessageList(map));
    }

    @RequestMapping(value = "/selectMessageDetails", method = RequestMethod.GET)
    @ApiOperation("获取消息详细信息")
    @ResponseBody
    public Result selectMessageDetails(Long id){
        return Result.success().put("data",messageService.selectMessageById(id));
    }

    @RequestMapping(value = "/updateMessage", method = RequestMethod.POST)
    @ApiOperation("修改消息")
    @ResponseBody
    public Result updateMessage(@RequestBody MessageInfo messageInfo){
        SysUserEntity user = getUser();
        return messageService.update(messageInfo,user);
    }


    @RequestMapping(value = "/deleteMessageById", method = RequestMethod.POST)
    @ApiOperation("删除消息")
    @ResponseBody
    public Result deleteMessageById(Long id){
        return Result.success().put("data",messageService.delete(id));
    }

    @RequestMapping(value = "/insertMessage", method = RequestMethod.POST)
    @ApiOperation("添加消息")
    @ResponseBody
    public Result insertMessage(MessageInfo messageInfo){
        return Result.success().put("data",messageService.saveBody(messageInfo));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台公告详情")
    @ResponseBody
    public Result getMessage(@PathVariable Long id) {
        return Result.success().put("data",messageService.selectMessageById(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("管理平台和用户端通用接口添加公告")
    @ResponseBody
    public Result addMessage(@RequestBody MessageInfo messageInfo) {
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("管理平台修改公告接口")
    @ResponseBody
    public Result uUpdate(@RequestBody MessageInfo messageInfo) {
        SysUserEntity user = getUser();
        messageService.update(messageInfo,user);
        return Result.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台删除公告接口")
    public Result deleteMessage(@PathVariable Long id) {
        messageService.delete(id);
        return Result.success();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation("管理平台获取全部公告接口")
    @ResponseBody
    public Result getMessageList(int page,int limit) {
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",null);
        map.put("state",null);
        map.put("type",null);
        return Result.success().put("data",messageService.selectMessageList(map));
    }

    @RequestMapping(value = "/page/{state}/{page}/{limit}", method = RequestMethod.GET)
    @ApiOperation("管理平台分页查询公告接口")
    @ResponseBody
    public Result getBodyPage(@PathVariable Integer state, @PathVariable Integer page, @PathVariable int limit) {
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("state",state);
        map.put("type",null);
        map.put("userId",null);
        return Result.success().put("data",messageService.selectMessageList(map));
    }



}