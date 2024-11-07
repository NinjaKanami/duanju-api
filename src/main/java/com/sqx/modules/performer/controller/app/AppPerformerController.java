package com.sqx.modules.performer.controller.app;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.performer.service.PerformerService;
import com.sqx.modules.performer.vo.AppPerformerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * (Box)表控制层
 *
 * @author makejava
 * @since 2024-10-17 15:49:35
 */
@Api(value = "App演员", tags = {"App演员"})
@RestController
@RequestMapping("app/performer")
public class AppPerformerController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private PerformerService performerService;

    @Login
    @PostMapping("/{performer_id}/follow")
    @ApiOperation("用户关注某个演员")
    public Result followPerformer(@RequestAttribute Long userId, @PathVariable Long performer_id) {
        if (this.performerService.userFollowPerformer(userId, performer_id)) {
            return Result.success("关注成功");
        }
        return Result.error("关注失败");
    }

    @Login
    @PostMapping("/{performer_id}/unfollow")
    @ApiOperation("用户取消关注某个演员")
    public Result unfollowPerformer(@RequestAttribute Long userId, @PathVariable Long performer_id) {
        if (this.performerService.userUnfollowPerformer(userId, performer_id)) {
            return Result.success("取关成功");
        }
        return Result.error("取关失败");
    }

    @Login
    @GetMapping("/my_follows")
    @ApiOperation("查询用户关注演员列表")
    public Result userFollowPerformers(@RequestAttribute Long userId) {
        // TODO(bootun): 需要查到演员的真实粉丝数
        List<Performer> performers = this.performerService.userFollowPerformersList(userId);
        List<AppPerformerVO> performersVOs = AppPerformerVO.fromEntityList(performers);
        return Result.success().put("data", performersVOs);
    }


}
