package com.sqx.modules.message.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;

    @Login
    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectMessageByUserId(int page, int limit,@RequestAttribute("userId") Long userId,Integer state){
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",userId);
        map.put("state",state);
        PageUtils pageUtils = messageService.selectMessageList(map);
        // NOTE(bootun): 2024/11/06日需求更新, 只有点进某个消息才更新这条消息的已读状态
        // messageService.updateSendState(userId,state);
        return Result.success().put("data", pageUtils);
    }

    @Login
    @RequestMapping(value = "/selectMessageCount", method = RequestMethod.GET)
    @ApiOperation("查询未读消息总数")
    @ResponseBody
    public Result selectMessageCount(@RequestAttribute("userId") Long userId){
        int count = messageService.count(new QueryWrapper<MessageInfo>().eq("user_id", userId).in("state", 4, 5).eq("is_see", 0));
        return Result.success().put("data",count);
    }

    @RequestMapping(value = "/selectMessage", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectMessage(int page, int limit, Integer state) {
        // WARNING: 该接口允许任何用户查询所有消息(包括不是自己的消息)
        return Result.error(HttpStatus.FORBIDDEN.value(), "系统繁忙，请稍后再试");
        // Map<String,Object> map=new HashMap<>();
        // map.put("page",page);
        // map.put("limit",limit);
        // map.put("state",state);
        // return Result.success().put("data",messageService.selectMessageList(map));
    }

    @Login
    @PostMapping("/insertMessage")
    @ApiOperation("添加投诉")
    public Result insertMessage(@RequestBody MessageInfo messageInfo) {
        // WARNING: 该接口允许任何用户创建任意类型的消息
        // messageService.saveBody(messageInfo);
        return Result.error(HttpStatus.FORBIDDEN.value(), "系统繁忙，请稍后再试");
    }

    @Login
    @PostMapping("/{id}/read")
    @ApiOperation("将用户消息标记为已读")
    @ResponseBody
    public Result userReadMessage(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        if (messageService.userReadMessage(userId, id) > 0) {
            return Result.success();
        }
        return Result.error("消息不存在");
    }


}