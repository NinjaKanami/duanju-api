package com.sqx.modules.search.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.search.entity.Search;
import org.springframework.web.bind.annotation.RequestAttribute;

/**
 * app搜索
 */
public interface AppSearchService {

    Result insetAppSearch(String searchName, Long userId);

    Result  selectAppSearchNum(Long userId);

    Result deleteAppSearch( Long userId);
}
