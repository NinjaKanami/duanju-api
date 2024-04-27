package com.sqx.modules.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.search.dao.SearchDao;
import com.sqx.modules.search.entity.Search;
import com.sqx.modules.search.service.SearchService;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl extends ServiceImpl<SearchDao, Search> implements SearchService {
    @Override
    public Result insertSearch(Search search) {
        baseMapper.insert(search);
        return Result.success();
    }

    @Override
    public Result selectByUserId(Long userId) {
        QueryWrapper<Search> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return Result.success().put("data",baseMapper.selectList(queryWrapper));
    }

    @Override
    public Result deleteById(Long id) {
        baseMapper.deleteById(id);
        return Result.success();
    }
}
