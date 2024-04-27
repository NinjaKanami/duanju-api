package com.sqx.modules.search.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.search.entity.Search;
import com.sqx.modules.search.service.SearchService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "搜索记录", tags = {"搜索记录"})
@RequestMapping(value = "/search")
public class SearchController extends AbstractController {
    @Autowired
    private SearchService searchService;
    @PostMapping("/insertSearch")
    @ApiOperation("记录搜索信息")
    public Result insertSearch(@RequestBody Search search){
        return searchService.insertSearch(search);
    }
    @GetMapping("/selectByUserId")
    @ApiOperation("查看搜索信息")
    public Result selectByUserId(Long userId){
        return searchService.selectByUserId(userId);
    }

    @GetMapping("/deleteById")
    @ApiOperation("删除搜索信息")
    public Result deleteById(Long id){
        return searchService.deleteById(id);
    }












}
