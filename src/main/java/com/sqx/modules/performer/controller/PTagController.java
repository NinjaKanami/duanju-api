package com.sqx.modules.performer.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.sqx.common.utils.Result;
import com.sqx.modules.performer.entity.PTag;
import com.sqx.modules.performer.service.PTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (PTag)表控制层
 *
 * @author bootun
 * @since 2024-11-06
 */
@Api(value = "演员标签(演员类型)", tags = {"演员标签", "演员类型"})
@RestController
@RequestMapping("performer/ptag")
public class PTagController extends ApiController {
    @Resource
    private PTagService ptagService;

    @GetMapping(value = "/all")
    @ApiOperation("获取演员类型列表")
    public Result getTagList() {
        // NOTE: 类型列表通常不会很大，所以这里没做分页
        return Result.success().put("data", ptagService.getAllPTags());
    }

    @PostMapping(value = "/new")
    @ApiOperation("新增演员类型")
    public Result createTag(@RequestBody PTag ptag) {
        if (ptag == null) {
            return Result.error("创建失败,参数错误");
        }
        if (ptag.getName().contains(",") || ptag.getName().contains(":")) {
            return Result.error("创建失败,类型名称不能包含逗号或冒号");
        }
        return ptagService.save(ptag) ? Result.success("创建成功") : Result.error("创建失败");
    }

    @PostMapping(value = "/{id}/update")
    @ApiOperation("修改演员类型")
    public Result updateTag(@PathVariable Long id, @RequestBody PTag ptag) {
        if (ptag == null) {
            return Result.error("修改失败,参数错误");
        }
        ptag.setId(id);
        return ptagService.update(ptag, new QueryWrapper<PTag>().eq("id", id)) ? Result.success("修改成功") : Result.error("修改失败");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/delete")
    @ApiOperation("删除演员类型")
    public Result deleteTag(@PathVariable("id") Long id) {
        return ptagService.removeById(id) ? Result.success("删除成功") : Result.error("删除失败,要删除的内容不存在");
    }
}
