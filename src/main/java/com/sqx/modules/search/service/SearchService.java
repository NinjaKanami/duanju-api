package com.sqx.modules.search.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.search.entity.Search;

public interface SearchService extends IService<Search> {
    Result insertSearch(Search search);
    Result selectByUserId(Long userId);
    Result deleteById(Long id);
}
