package com.sqx.modules.performer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.performer.service.PerformerService;
import com.sqx.modules.performer.vo.AdminPerformerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Performer)表控制层
 *
 * @author bootun
 * @since 2024-11-06
 */
@Api(value = "演员管理", tags = {"演员", "演员管理"})
@RestController
@RequestMapping("performer")
public class PerformerController {
    @Resource
    private PerformerService performerService;

    @GetMapping("/list")
    @ApiOperation("查询演员信息")
    public Result selectPerformers(
            @ApiParam("页") Integer page, @ApiParam("条") Integer limit,
            @ApiParam("演员名称") String name, @ApiParam("演员性别") Integer sex,
            @ApiParam("演员公司") String company, @ApiParam("演员类型") Integer ptag
    ) {
        Page<Performer> performers = performerService.selectPerformers(page, limit, name, sex, company, ptag, null);
        return Result.success().put("data", AdminPerformerVO.fromEntityPage(performers));
    }

    @PostMapping(value = "/new")
    @ApiOperation("添加演员")
    public Result insertPerformer(@RequestBody AdminPerformerVO performer) {
        Performer entity = performer.toEntity();
        performerService.createPerformer(entity);
        return Result.success("创建成功");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/update")
    @ApiOperation("修改演员信息")
    public Result updatePerformer(@PathVariable("id") Long id, @RequestBody AdminPerformerVO performer) {
        if (id == null) {
            return Result.error("参数错误");
        }
        performer.setId(id);
        if (performerService.updatePerformer(performer.toEntity()) > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/delete")
    @ApiOperation("删除演员")
    public Result deletePerformer(@PathVariable Long id) {
        if (performerService.deletePerformer(id) > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
