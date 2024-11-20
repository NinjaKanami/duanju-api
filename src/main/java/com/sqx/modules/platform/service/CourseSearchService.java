package com.sqx.modules.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.platform.entity.Series;

import java.util.List;

/**
 * CourseSearch 服务接口
 *
 * @author makejava
 * @since 2024-11-07 18:57:41
 */
public interface CourseSearchService extends IService<Course> {
    /**
     * 根据多个条件搜索课程信息
     * 此方法用于根据关键词、区域、分类、平台、价格、排序字段和排序方式等条件来分页搜索课程
     * 它允许用户根据特定需求筛选和排序课程信息
     *
     * @param page       分页对象，用于封装分页信息
     * @param keyword    关键词，用于搜索课程名称、描述等内容
     * @param areaId     区域ID，用于筛选属于特定区域的课程
     * @param classifyId 分类ID，用于筛选属于特定分类的课程
     * @param platformId 平台ID，用于筛选属于特定平台的课程
     * @param isPrice    价格标识，用于筛选免费或付费课程
     * @param sortField  排序字段，指定按照哪个字段进行排序
     * @param sort       排序方式，指定是升序还是降序
     * @return 返回一个分页的课程列表，包含根据条件筛选和排序后的课程信息
     */
    Result searchCourse(Page<Course> page, String keyword, Long areaId, Long classifyId, Long platformId, Long isPrice, String sortField, Integer sort);

}
