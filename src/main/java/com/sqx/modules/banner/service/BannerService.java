package com.sqx.modules.banner.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.banner.entity.Banner;
import com.sqx.modules.course.entity.Course;

import java.util.List;

public interface BannerService extends IService<Banner> {

    List<Banner> selectBannerList(Integer classify);

    List<Banner> selectBannerLists(Integer classify);

    PageUtils selectBannerPage(Integer page, Integer limit, Integer classify);

    int saveBody(String image, String url, Integer sort);

    Banner selectBannerById(Long id);

    int deleteBannerById(Long id);

    Result updateBannerStateById(Long id);

    int updateBannerById(Banner banner);

    int insertBanner(Banner banner);

   Result clickBanner(Integer bannerId,int page,int limit);

}
