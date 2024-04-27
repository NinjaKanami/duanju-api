package com.sqx.modules.banner.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.banner.entity.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fang
 * @date 2020/7/9
 */
@Mapper
public interface BannerDao extends BaseMapper<Banner> {


    List<Banner> selectLists(@Param("classify")  Integer classify);

    List<Banner> selectList(@Param("classify")  Integer classify);

    IPage<Banner> selectBannerPage(Page<Banner> page,@Param("classify") Integer classify);

}
