package com.sqx.modules.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.platform.dao.SeriesDao;
import com.sqx.modules.platform.entity.CoursePerformer;
import com.sqx.modules.platform.entity.Series;
import com.sqx.modules.platform.entity.SeriesCourse;
import com.sqx.modules.platform.service.SeriesCourseService;
import com.sqx.modules.platform.service.SeriesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Series)表服务实现类
 *
 * @author makejava
 * @since 2024-11-07 18:57:42
 */
@Service("seriesService")
public class SeriesServiceImpl extends ServiceImpl<SeriesDao, Series> implements SeriesService {

    @Resource
    private SeriesCourseService seriesCourseService;
    @Resource
    private CourseService courseService;

    @Override
    public Result selectSeriesPage(Page<Series> page, Series series) {
        Page<Series> seriesPage = this.page(page, new QueryWrapper<>(series).orderByAsc("sort"));
        if (seriesPage != null && seriesPage.getRecords() != null) {
            for (Series series1 : seriesPage.getRecords()) {
                List<SeriesCourse> seriesCourseList = seriesCourseService.list(new QueryWrapper<SeriesCourse>().eq("series_id", series1.getSeriesId()));
                if (!seriesCourseList.isEmpty()) {
                    List<Long> courseIdList = seriesCourseList.stream().map(SeriesCourse::getCourseId).collect(Collectors.toList());
                    List<Course> courseList = courseService.list(new QueryWrapper<Course>().in("course_id", courseIdList));
                    series1.setCourseList(courseList);
                }
            }
        }
        return Result.success().put("data", seriesPage);
    }

    @Override
    public Result selectSeriesById(Serializable id) {
        Series series = this.getById(id);
        if (series != null) {
            List<SeriesCourse> seriesCourseList = seriesCourseService.list(new QueryWrapper<SeriesCourse>().eq("series_id", id));
            if (!seriesCourseList.isEmpty()) {
                List<Course> courseList = courseService.list(new QueryWrapper<Course>().in("course_id", seriesCourseList.stream().map(SeriesCourse::getCourseId).collect(Collectors.toList())));
                series.setCourseList(courseList);
            }
        }
        return Result.success().put("data", series);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertSeries(Series series) {
        if (this.save(series)) {
            // 修改演员关系
            if (!series.getCourseIds().isEmpty()) {
                seriesCourseService.remove(new QueryWrapper<SeriesCourse>().eq("series_id", series.getSeriesId()));
                for (Long courseId : series.getCourseIds()) {
                    SeriesCourse seriesCourse = new SeriesCourse(series.getSeriesId(), courseId);
                    seriesCourseService.save(seriesCourse);
                }
            }
            return Result.success();
        }
        return Result.error();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateSeries(Series series) {
        if (this.updateById(series)) {
            // 修改演员关系
            if (!series.getCourseIds().isEmpty()) {
                seriesCourseService.remove(new QueryWrapper<SeriesCourse>().eq("series_id", series.getSeriesId()));
                for (Long courseId : series.getCourseIds()) {
                    SeriesCourse seriesCourse = new SeriesCourse(series.getSeriesId(), courseId);
                    seriesCourseService.save(seriesCourse);
                }
                return Result.success();
            }
        }
        return Result.error();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteSeries(List<Long> idList) {
        if (this.removeByIds(idList)) {
            seriesCourseService.remove(new QueryWrapper<SeriesCourse>().in("series_id", idList));
            return Result.success();
        }
        return Result.error();
    }
}
