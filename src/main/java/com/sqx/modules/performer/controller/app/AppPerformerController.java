package com.sqx.modules.performer.controller.app;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.performer.dao.PerformerUserDao;
import com.sqx.modules.performer.entity.PTag;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.performer.service.PTagService;
import com.sqx.modules.performer.service.PerformerService;
import com.sqx.modules.performer.vo.AppPTagVO;
import com.sqx.modules.performer.vo.AppPerformerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Resource
    private PTagService pTagService;


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
        List<Performer> performers = this.performerService.userFollowPerformersList(userId);
        List<AppPerformerVO> performersVOs = AppPerformerVO.fromEntityList(performers);
        return Result.success().put("data", performersVOs);
    }

    //@Login
    @GetMapping("/rank")
    @ApiOperation("查询演员排行榜, " + "ptagId筛选指定标签类型，" + "order为0是降序(默认,follower从多到少)，order是1则是升序(follower从少到多), " + "sex为1是查询男性，sex为2是查询女性，不填则查询全部性别")
    public Result performersRank(Page<Performer> page,
                                 @RequestParam(required = false) Long ptagId,
                                 @RequestParam(required = false) Integer sex,
                                 @RequestParam(required = false) Integer order) {
        String orderBy = "DESC";
        if (order != null && order == 1) {
            orderBy = "ASC";
        }
        Page<Performer> performerPage = this.performerService.selectPerformerRankOrderByFollower(page, ptagId, sex, orderBy);
        // List<AppPerformerVO> performersVOs = AppPerformerVO.fromEntityList(performerPage.getRecords());
        Page<AppPerformerVO> appPerformerVOPage = new Page<>(performerPage.getCurrent(), performerPage.getSize(), performerPage.getTotal());
        appPerformerVOPage.setRecords(AppPerformerVO.fromEntityList(performerPage.getRecords()));
        return Result.success().put("data", appPerformerVOPage);
    }

    //@Login
    @GetMapping("/{performerId}/detail")
    @ApiOperation("查询演员详情, 该接口会同时查出来用户是否已追该演员。该接口会带出来演员关联的短剧，但有些剧不能在微信小程序上显示，因此如果在微信小程序平台上，需要传wxShow参数")
    public Result queryPerformerDetail(
            @PathVariable Long performerId, // 演员ID
            @RequestAttribute(required = false) Long userId, // 用户ID
            @RequestParam(required = false) Long wxShow // 是否只查询微信端显示的短剧，不传默认查询全部，传1则只展示微信小程序端显示的短剧
    ) {
        // 出演短剧
        AppPerformerVO appPerformerVO = this.performerService.userGetPerformerDetail(userId, performerId, wxShow);
        return Result.success().put("data", appPerformerVO);
    }

    //@Login
    @GetMapping("/tag/list")
    @ApiOperation("类型列表")
    public Result queryPerformerTags() {
        // 类型列表不会很大，是常数级别
        List<PTag> allPTags = this.pTagService.getAllVisiblePTagsOrderByPageIndex();
        List<AppPTagVO> res = AppPTagVO.fromEntityList(allPTags);
        return Result.success().put("data", res);
    }

    @GetMapping("/search")
    @ApiOperation("搜索演员")
    public Result searchPerformer(@RequestParam String name) {
        List<AppPerformerVO> res = this.performerService.userSearchPerformer(name);
        return Result.success().put("data", res);
    }

}
