package com.sqx.modules.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.search.Response.SearchResponse;
import com.sqx.modules.search.dao.AppSearchDao;
import com.sqx.modules.search.entity.Search;
import com.sqx.modules.search.service.AppSearchService;
import com.sqx.modules.search.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class AppSearchServiceImpl extends ServiceImpl<AppSearchDao, Search> implements AppSearchService {
    private AppSearchDao appSearchDao;
    private SearchService searchService;

    /**
     * 记录用户搜索的内容
     *
     *
     * @param userId
     * @return
     */
    @Override
    public Result insetAppSearch(String searchName, Long userId) {
        //判断传过来的搜索信息不为空
        if (searchName != null) {
            //去查询用户搜索内容是否有重复
            QueryWrapper<Search> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("search_name", searchName);
            queryWrapper.eq("user_id", userId);
            Search search1 = baseMapper.selectOne(queryWrapper);
            //有重复则更新改变时间
            if (search1 != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                search1.setUpdateTime(simpleDateFormat.format(new Date()));
                int i = baseMapper.updateById(search1);
                if (i > 0) {
                    return Result.success("更新成功!");
                } else {
                    return Result.error("更新失败!");
                }
            } else {
                //没有则记录
                Search search=new Search();
                       search.setUserId(userId);
                       search.setSearchName(searchName);
                int count = baseMapper.insert(search);
                if (count > 0) {
                    return Result.success("记录成功!");
                } else {
                    return Result.error("记录失败!");
                }
            }

        } else {
            return Result.error("搜索信息为空！");
        }
    }

    /**
     * 经常搜索的名称
     *
     * @return
     */
    @Override
    public Result selectAppSearchNum(Long userId) {
        //经常搜索的名称
        List<String> list = appSearchDao.selectAppSearchNum();
        //用户经常搜索的名称
        Result result = searchService.selectByUserId(userId);
        //创建返回对象
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setAllSerchName(list);
        List<Search> searches = (List<Search>) result.get("data");
        for (Search search : searches) {
            searchResponse.getUserSearchName().add(search.getSearchName());
        }

        return Result.success().put("data", searchResponse);
    }

    /**
     * 删除用户的搜索记录
     *
     * @param userId
     * @return
     */
    @Override
    public Result deleteAppSearch(Long userId) {
        int count = appSearchDao.deleteAppSearch(userId);
        if (count > 0) {
            return Result.success("删除成功！");
        } else {
            return Result.error("删除失败！");
        }
    }
}
