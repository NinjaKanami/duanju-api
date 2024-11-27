package com.sqx.modules.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.RedisUtils;
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
import org.apache.commons.lang.StringUtils;
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
    @Resource
    private RedisUtils redisUtils;

    @Override
    public Result selectSeriesPage(Page<Series> page, Series series) {
        Page<Series> seriesPage;
        // 检查缓存
        String seriesPageName = "page_series_" + page.getCurrent() + "_" + page.getSize();
        String cache = redisUtils.get(seriesPageName);
        // 如果缓存中没有数据，则从数据库中查询
        if (StringUtils.isEmpty(cache)) {
            seriesPage = this.page(page, new QueryWrapper<>(series).orderByAsc("sort"));
            if (seriesPage != null && seriesPage.getRecords() != null) {
                for (Series series1 : seriesPage.getRecords()) {
                    List<SeriesCourse> seriesCourseList = seriesCourseService.list(new QueryWrapper<SeriesCourse>().eq("series_id", series1.getSeriesId()));
                    if (!seriesCourseList.isEmpty()) {
                        List<Long> courseIdList = seriesCourseList.stream().map(SeriesCourse::getCourseId).collect(Collectors.toList());
                        List<Course> courseList = courseService.list(new QueryWrapper<Course>().in("course_id", courseIdList));
                        series1.setCourseIds(courseIdList);
                        series1.setCourseList(courseList);
                    }
                }
            }
            // 缓存数据
            redisUtils.set(seriesPageName, seriesPage);
        } else {
            // 从缓存中获取数据
            seriesPage = redisUtils.get(seriesPageName, Page.class);
        }
        return Result.success().put("data", seriesPage);
    }

    @Override
    public Result selectSeriesById(Serializable id) {
        Series series;
        // 检查缓存
        String seriesName = "seriesId_" + id;
        String cache = redisUtils.get(seriesName);
        // 如果缓存中没有数据，则从数据库中查询
        if (StringUtils.isEmpty(cache)) {
            series = this.getById(id);
            if (series != null) {
                List<SeriesCourse> seriesCourseList = seriesCourseService.list(new QueryWrapper<SeriesCourse>().eq("series_id", id));
                if (!seriesCourseList.isEmpty()) {
                    List<Long> courseIdList = seriesCourseList.stream().map(SeriesCourse::getCourseId).collect(Collectors.toList());
                    // List<Course> courseList = courseService.list(new QueryWrapper<Course>().in("course_id", courseIdList));
                    series.setCourseIds(courseIdList);
                    // series.setCourseList(courseList);
                }
            }
            redisUtils.set(seriesName, series);
        } else {
            series = redisUtils.get(seriesName, Series.class);
        }
        return Result.success().put("data", series);
    }

    @Override
    public Result selectSeriesById(Long userId, Serializable id) {
        Series series;
        // 检查缓存
        String seriesName = "seriesId_app_" + id;
        String cache = redisUtils.get(seriesName);
        // 如果缓存中没有数据，则从数据库中查询
        if (StringUtils.isEmpty(cache)) {
            series = this.getById(id);
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
                        // 查询用户信息提到缓存外面
                    });
                    // series.setCourseIds(courseIdList);
                    series.setCourseList(courseList);
                }
            }
            redisUtils.set(seriesName, series);
        } else {
            series = redisUtils.get(seriesName, Series.class);
        }

        // 查询用户是否登录
        if (userId != null && series != null && series.getCourseList() != null && !series.getCourseList().isEmpty()) {
            series.getCourseList().forEach(course -> {
                // 查询用户是否收藏了该短剧
                course.setIsCollect(courseCollectService.count(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("classify", 1).eq("course_id", course.getCourseId())));

                // 查询用户是否看过了该短剧
                course.setIsView(courseCollectService.count(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("classify", 3).eq("course_id", course.getCourseId())));

                // 查询用户的评分
                // CourseScore courseScore = courseScoreService.getOne(new QueryWrapper<CourseScore>()
                //         .eq("user_id", userId).eq("course_id", course.getCourseId()));
            });
        }
        return Result.success().put("data", series);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertSeries(Series series) {
        if (this.save(series)) {
            // 修改剧单关系
            if (!series.getCourseIds().isEmpty()) {
                seriesCourseService.remove(new QueryWrapper<SeriesCourse>().eq("series_id", series.getSeriesId()));
                for (Long courseId : series.getCourseIds()) {
                    SeriesCourse seriesCourse = new SeriesCourse(series.getSeriesId(), courseId);
                    seriesCourseService.save(seriesCourse);
                }
            }
            this.flushSeries();
            return Result.success();
        }
        return Result.error();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateSeries(Series series) {
        if (this.updateById(series)) {
            // 修改剧单关系
            if (!series.getCourseIds().isEmpty()) {
                seriesCourseService.remove(new QueryWrapper<SeriesCourse>().eq("series_id", series.getSeriesId()));
                for (Long courseId : series.getCourseIds()) {
                    SeriesCourse seriesCourse = new SeriesCourse(series.getSeriesId(), courseId);
                    seriesCourseService.save(seriesCourse);
                }
                this.flushSeries(series.getSeriesId());
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
            // this.flushSeriesAll();
            redisUtils.deleteByPattern("page_series_*");
            idList.forEach(seriesId -> redisUtils.deleteByPattern(String.format("seriesId_*%d", seriesId)));
            return Result.success();
        }
        return Result.error();
    }

    public void flushSeries() {
        flushSeries(null);
    }

    public void flushSeries(Long seriesId) {
        redisUtils.deleteByPattern("page_series_*");
        if (seriesId != null) {
            redisUtils.deleteByPattern(String.format("seriesId_(app_)?%d", seriesId));
        }
    }

    public void flushSeriesAll() {
        redisUtils.deleteByPattern("page_series_*");
        redisUtils.deleteByPattern("seriesId_*");
    }
}
