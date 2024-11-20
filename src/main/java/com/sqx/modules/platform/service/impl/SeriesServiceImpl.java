package com.sqx.modules.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.dao.CourseCollectDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseCollect;
import com.sqx.modules.course.service.CourseCollectService;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.performer.service.PerformerService;
import com.sqx.modules.platform.dao.SeriesDao;
import com.sqx.modules.platform.entity.CoursePerformer;
import com.sqx.modules.platform.entity.CourseScore;
import com.sqx.modules.platform.entity.Series;
import com.sqx.modules.platform.entity.SeriesCourse;
import com.sqx.modules.platform.service.CoursePerformerService;
import com.sqx.modules.platform.service.CourseScoreService;
import com.sqx.modules.platform.service.SeriesCourseService;
import com.sqx.modules.platform.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Resource
    private CourseScoreService courseScoreService;
    @Resource
    private CoursePerformerService coursePerformerService;
    @Resource
    private PerformerService performerService;
    @Resource
    private CourseCollectService courseCollectService;

    @Override
    public Result selectSeriesPage(Page<Series> page, Series series) {
        Page<Series> seriesPage = this.page(page, new QueryWrapper<>(series).orderByAsc("sort"));
        if (seriesPage != null && seriesPage.getRecords() != null) {
            for (Series series1 : seriesPage.getRecords()) {
                List<SeriesCourse> seriesCourseList = seriesCourseService.list(new QueryWrapper<SeriesCourse>().eq("series_id", series1.getSeriesId()));
                if (!seriesCourseList.isEmpty()) {
                    List<Long> courseIdList = seriesCourseList.stream().map(SeriesCourse::getCourseId).collect(Collectors.toList());
                    // List<Course> courseList = courseService.list(new QueryWrapper<Course>().in("course_id", courseIdList));
                    series1.setCourseIds(courseIdList);
                    // series1.setCourseList(courseList);

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
                List<Long> courseIdList = seriesCourseList.stream().map(SeriesCourse::getCourseId).collect(Collectors.toList());
                // List<Course> courseList = courseService.list(new QueryWrapper<Course>().in("course_id", courseIdList));
                series.setCourseIds(courseIdList);
                // series.setCourseList(courseList);
            }
        }
        return Result.success().put("data", series);
    }

    @Override
    public Result selectSeriesById(Long userId, Serializable id) {
        Series series = this.getById(id);
        if (series != null) {
            List<SeriesCourse> seriesCourseList = seriesCourseService.list(new QueryWrapper<SeriesCourse>().eq("series_id", id));
            if (!seriesCourseList.isEmpty()) {
                List<Long> courseIdList = seriesCourseList.stream().map(SeriesCourse::getCourseId).collect(Collectors.toList());
                List<Course> courseList = courseService.list(new QueryWrapper<Course>().in("course_id", courseIdList));
                courseList.forEach(course -> {
                    // 查询总分
                    if (course.getScore() == null) {
                        List<CourseScore> scores = courseScoreService.list(new QueryWrapper<CourseScore>().eq("course_id", course.getCourseId()));
                        // 计算平均分
                        if (!scores.isEmpty()) {
                            course.setScore(scores.stream().map(CourseScore::getScore).reduce(BigDecimal::add).orElse(new BigDecimal(0)).divide(new BigDecimal(scores.size()), 1, RoundingMode.HALF_UP));
                        }
                    }
                    // 查询演员
                    List<CoursePerformer> coursePerformerList = coursePerformerService.list(new QueryWrapper<CoursePerformer>().eq("course_id", course.getCourseId()));
                    if (!coursePerformerList.isEmpty()) {
                        List<Performer> performerList = performerService.list(new QueryWrapper<Performer>().in("id", coursePerformerList.stream().map(CoursePerformer::getPerformerId).collect(Collectors.toList())));
                        course.setPerformerList(performerList);
                    }
                    // 查询用户是否登录
                    if (userId != null) {
                        // 查询用户是否收藏了该短剧
                        course.setIsCollect(courseCollectService.count(new QueryWrapper<CourseCollect>()
                                .eq("user_id", userId).eq("classify", 1).eq("course_id", course.getCourseId())));

                        // 查询用户是否看过了该短剧
                        course.setIsView(courseCollectService.count(new QueryWrapper<CourseCollect>()
                                .eq("user_id", userId).eq("classify", 3).eq("course_id", course.getCourseId())));

                        // 查询用户的评分
                        // CourseScore courseScore = courseScoreService.getOne(new QueryWrapper<CourseScore>()
                        //         .eq("user_id", userId).eq("course_id", course.getCourseId()));
                    }

                });
                // series.setCourseIds(courseIdList);
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
