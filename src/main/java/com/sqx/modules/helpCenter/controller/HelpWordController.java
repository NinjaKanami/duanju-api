package com.sqx.modules.helpCenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.helpCenter.entity.HelpClassify;
import com.sqx.modules.helpCenter.entity.HelpWord;
import com.sqx.modules.helpCenter.service.HelpClassifyService;
import com.sqx.modules.helpCenter.service.HelpWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Api(value = "帮助中心", tags = {"帮助中心"})
@RequestMapping(value = "/helpWord")
public class HelpWordController {

    @Autowired
    private HelpClassifyService helpClassifyService;
    @Autowired
    private HelpWordService helpWordService;


    @PostMapping("/insertHelpClassify")
    @ApiOperation("添加帮助分类")
    public Result insertHelpClassify(@RequestBody HelpClassify helpClassify){
        helpClassify.setCreateTime(DateUtils.format(new Date()));
        helpClassifyService.save(helpClassify);
        return Result.success();
    }

    @PostMapping("/updateHelpClassify")
    @ApiOperation("修改帮助分类")
    public Result updateHelpClassify(@RequestBody HelpClassify helpClassify){
        helpClassifyService.updateById(helpClassify);
        return Result.success();
    }

    @PostMapping("/deleteHelpClassify")
    @ApiOperation("删除帮助分类")
    public Result deleteHelpClassify(Long helpClassifyId){
        helpClassifyService.removeById(helpClassifyId);
        return Result.success();
    }


    @GetMapping("/selectHelpClassifyList")
    @ApiOperation("查询帮助分类")
    public Result selectHelpClassifyList(Integer page,Integer limit,Long parentId,Integer types,String helpClassifyName){
        if(page==null || limit==null){
            List<HelpClassify> page1 = helpClassifyService.list(
                    new QueryWrapper<HelpClassify>()
                            .eq(types!=null,"types",types)
                            .eq(StringUtils.isNotBlank(helpClassifyName), "help_classify_name", helpClassifyName)
                            .eq(parentId != null, "parent_id", parentId).orderByAsc("sort"));
            return Result.success().put("data",page1);
        }
        IPage<HelpClassify> page1 = helpClassifyService.page(new Page<>(page, limit),
                new QueryWrapper<HelpClassify>()
                        .eq(types!=null,"types",types)
                        .eq(StringUtils.isNotBlank(helpClassifyName), "help_classify_name", helpClassifyName)
                        .eq(parentId != null, "parent_id", parentId).orderByAsc("sort"));
        return Result.success().put("data",new PageUtils(page1));
    }


    @PostMapping("/insertHelpWord")
    @ApiOperation("添加帮助文档")
    public Result insertHelpWord(@RequestBody HelpWord helpWord){
        helpWord.setCreateTime(DateUtils.format(new Date()));
        helpWordService.save(helpWord);
        return Result.success();
    }

    @PostMapping("/updateHelpWord")
    @ApiOperation("修改帮助文档")
    public Result updateHelpWord(@RequestBody HelpWord helpWord){
        helpWordService.updateById(helpWord);
        return Result.success();
    }

    @PostMapping("/deleteHelpWord")
    @ApiOperation("删除帮助文档")
    public Result deleteHelpWord(Long helpWordId){
        helpWordService.removeById(helpWordId);
        return Result.success();
    }


    @GetMapping("/selectHelpWordList")
    @ApiOperation("查询帮助文档")
    public Result selectHelpWordList(Integer page,Integer limit,Long helpClassifyId,String helpWordTitle){
        IPage<HelpWord> page1 = helpWordService.page(new Page<>(page, limit), new QueryWrapper<HelpWord>()
                .eq(helpClassifyId != null, "help_classify_id", helpClassifyId)
                .eq(StringUtils.isNotBlank(helpWordTitle), "help_word_title", helpWordTitle).orderByAsc("sort"));
        return Result.success().put("data",new PageUtils(page1));
    }





}
