package com.sqx.modules.banner.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.banner.dao.BannerDao;
import com.sqx.modules.banner.entity.Banner;
import com.sqx.modules.banner.service.BannerService;
import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.dao.CourseDetailsDao;
import com.sqx.modules.course.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * banner图
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerDao, Banner> implements BannerService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private BannerDao bannerDao;
    @Autowired
    private CourseDetailsDao courseDetailsDao;


    @Override
    public List<Banner> selectBannerList(Integer classify) {
        return bannerDao.selectList(classify);
    }


    @Override
    public List<Banner> selectBannerLists(Integer classify) {
        return bannerDao.selectLists(classify);
    }

    @Override
    public PageUtils selectBannerPage(Integer page,Integer limit,Integer classify) {
        Page<Banner> pages=new Page<>(page,limit);
        return new PageUtils(bannerDao.selectBannerPage(pages,classify));
    }

    @Override
    public int saveBody(String image, String url, Integer sort) {
        Banner banner = new Banner();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        banner.setImageUrl(image);
        banner.setCreateTime(sdf.format(now));
        banner.setState(1);
        banner.setUrl(url);
        banner.setSort(sort == null ? 1 : sort);
        return bannerDao.insert(banner);
    }

    @Override
    public int insertBanner(Banner banner) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        banner.setCreateTime(sdf.format(now));
        banner.setState(2);
        return bannerDao.insert(banner);
    }

    @Override
    public Result clickBanner(Integer bannerId, int page, int limit) {
        Page<Course> page1 = new Page<>(page, limit);
        QueryWrapper<Course> queryWrapper = new QueryWrapper();
        //查询banner 对应短剧
        queryWrapper.eq("banner_id", bannerId);
        IPage<Course> coursePage = courseDao.selectPage(page1, queryWrapper);
        return Result.success().put("data", coursePage);
    }


    @Override
    public Banner selectBannerById(Long id) {
        return bannerDao.selectById(id);
    }

    @Override
    public int deleteBannerById(Long id) {
        return bannerDao.deleteById(id);
    }

    @Override
    public Result updateBannerStateById(Long id) {
        Banner banner = selectBannerById(id);
        if (banner != null) {
            if (banner.getState() == 1) {
                banner.setState(2);
            } else {
                banner.setState(1);
            }
            bannerDao.updateById(banner);
            return Result.success();
        } else {
            return Result.error("修改对象为空！");
        }
    }

    @Override
    public int updateBannerById(Banner banner) {
        return bannerDao.updateById(banner);
    }


}
