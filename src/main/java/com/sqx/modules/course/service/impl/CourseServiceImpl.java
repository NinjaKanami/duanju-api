package com.sqx.modules.course.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.RedisUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.utils.JwtUtils;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.course.dao.CourseCollectDao;
import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.dao.CourseDetailsDao;
import com.sqx.modules.course.dao.CourseUserDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseCollect;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.course.entity.CourseUser;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.course.service.CourseUserService;
import com.sqx.modules.course.vo.CourseIn;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.platform.entity.CoursePerformer;
import com.sqx.modules.platform.service.CoursePerformerService;
import com.sqx.modules.search.service.AppSearchService;
import com.sqx.modules.utils.EasyPoi.ExcelUtils;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseDao, Course> implements CourseService {
    @Autowired
    private CourseDetailsDao courseDetailsDao;
    @Autowired
    private CourseCollectDao courseCollectDao;
    @Autowired
    private CourseUserDao courseUserDao;
    @Autowired
    private AppSearchService appSearchService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CourseUserService courseUserService;
    @Autowired
    private CoursePerformerService coursePerformerService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 创建线程池处理业务逻辑
     */
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().build();
    private ExecutorService singleThreadPool = new ThreadPoolExecutor(30, 100,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    private static boolean sys = false;

    @Override
    public Result insertCourse(Course course) {
        //设置删除标识
        course.setIsDelete(0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置创建时间
        course.setCreateTime(df.format(new Date()));
        //设置更新时间
        course.setUpdateTime(df.format(new Date()));
        if (course.getCourseType().equals(2) || course.getCourseType().equals(3)) {
            baseMapper.insert(course);
            CourseDetails courseDetails = new CourseDetails();
            courseDetails.setCourseId(course.getCourseId());
            courseDetails.setVideoUrl(course.getRemark());
            courseDetailsDao.insert(courseDetails);
        } else {
            baseMapper.insert(course);
        }
        redisUtils.deleteByPattern("page*");
        return Result.success("操作成功！");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateCourse(Course course) {
        baseMapper.updateById(course);
        // 修改演员关系
        if(!course.getPerformerIds().isEmpty()){
            coursePerformerService.remove(new QueryWrapper<CoursePerformer>().eq("course_id", course.getCourseId()));
            for (Long performerId : course.getPerformerIds()) {
                CoursePerformer coursePerformer = new CoursePerformer(course.getCourseId(), performerId);
                coursePerformerService.save(coursePerformer);
            }
        }
        redisUtils.deleteByPattern("page*");
        redisUtils.deleteByPattern(String.format("*%d*", course.getCourseId()));
        return Result.success("操作成功！");
    }

    @Override
    public Result updateDelete(Long id) {
        baseMapper.updateDelete(id);
        redisUtils.deleteByPattern("page*");
        redisUtils.deleteByPattern(String.format("*%d*", id));
        return Result.success("操作成功！");
    }

    @Override
    public Map<String, Object> selectCourseByCourseId(Long userId, Long courseId) {
        return baseMapper.selectCourseByCourseId(userId, courseId);
    }


    @Override
    public Result selectCourse(Integer page, Integer limit, Long classifyId, String title, Integer isRecommend, Integer status,
                               Long bannerId, Integer sort, String token, Integer isPrice, Integer admin, Integer over,
                               Integer wxCourse, Integer dyCourse, Integer wxShow, Integer dyShow, Integer isCut, Integer priceType) {
        Long userId = null;
        if (admin == null) {
            if (StringUtils.isNotEmpty(token)) {
                Claims claims = jwtUtils.getClaimByToken(token);
                if (claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())) {
                    userId = Long.parseLong(claims.getSubject());
                }
            }
        }
        Page<Map<String, Object>> pages = new Page<>(page, limit);

        if (admin == null) {
            String redisCourseName = "page_" + page + "_limit_" + limit + "_classifyId_" + classifyId + "_title_" + title + "_isRecommend_" + isRecommend + "_status_" + status +
                    "_bannerId_" + bannerId + "_sort_" + sort + "_isPrice_" + isPrice + "_over_" + over + "_wxCourse_" + wxCourse + "_dyCourse_" + dyCourse + "_wxShow_" + wxShow +
                    "_dyShow_" + dyShow + "_isCut_" + isCut + "_priceType_" + priceType;
            String s = redisUtils.get(redisCourseName);
            if (StringUtils.isEmpty(s)) {
                //
                IPage<Map<String, Object>> mapIPage = baseMapper.selectCourse(pages, classifyId, title, isRecommend, status, bannerId,
                        sort, isPrice, over, wxCourse, dyCourse, wxShow, dyShow, isCut, priceType);
                List<Map<String, Object>> records = mapIPage.getRecords();
                for (Map<String, Object> map : records) {
                    Object courseId = map.get("courseId");
                    if (userId != null) {
                        int isCollect = courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                                .eq("classify", 1).eq("course_id", courseId).eq("user_id", userId));
                        map.put("isCollect", isCollect);

                        CourseCollect courseCollect = courseCollectDao.selectOne(new QueryWrapper<CourseCollect>()
                                .eq("classify", 3).eq("course_id", courseId).eq("user_id", userId).orderByDesc("update_time").last(" limit 1"));
                        if (courseCollect != null) {
                            CourseDetails courseDetails = courseDetailsDao.selectById(Long.parseLong(String.valueOf(courseCollect.getCourseDetailsId())));
                            if (courseDetails != null) {
                                map.put("courseDetailsId", courseDetails.getCourseDetailsId());
                                map.put("courseDetailsName", courseDetails.getCourseDetailsName());
                                map.put("dyEpisodeId", courseDetails.getDyEpisodeId());
                                map.put("wxCourseDetailsId", courseDetails.getWxCourseDetailsId());
                            }
                        }
                    }
                    Object courseDetailsId = map.get("courseDetailsId");
                    if (courseDetailsId == null) {
                        //默认取第一集
                        CourseDetails courseDetails = courseDetailsDao.selectOne(new QueryWrapper<CourseDetails>().eq("course_id", courseId)
                                .orderByAsc("course_id").last(" limit 1"));
                        if (courseDetails != null) {
                            map.put("courseDetailsId", courseDetails.getCourseDetailsId());
                            map.put("courseDetailsName", courseDetails.getCourseDetailsName());
                            map.put("dyEpisodeId", courseDetails.getDyEpisodeId());
                            map.put("wxCourseDetailsId", courseDetails.getWxCourseDetailsId());
                            map.put("isCollect", 0);
                        }
                    }
                }
                redisUtils.set(redisCourseName, JSONObject.toJSONString(mapIPage));
                return Result.success().put("data", new PageUtils(mapIPage));
            } else {
                JSONObject jsonObject = JSONObject.parseObject(s);
                Integer current = jsonObject.getInteger("current");
                Integer size = jsonObject.getInteger("size");
                Integer total = jsonObject.getInteger("total");
                List<Map<String, Object>> records = jsonObject.getObject("records", List.class);
                for (Map<String, Object> map : records) {
                    Object courseId = map.get("courseId");
                    if (userId != null) {
                        int isCollect = courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                                .eq("classify", 1).eq("course_id", courseId).eq("user_id", userId));
                        map.put("isCollect", isCollect);

                        CourseCollect courseCollect = courseCollectDao.selectOne(new QueryWrapper<CourseCollect>()
                                .eq("classify", 3).eq("course_id", courseId).eq("user_id", userId).orderByDesc("update_time").last(" limit 1"));
                        if (courseCollect != null) {
                            CourseDetails courseDetails = courseDetailsDao.selectById(Long.parseLong(String.valueOf(courseCollect.getCourseDetailsId())));
                            if (courseDetails != null) {
                                map.put("courseDetailsId", courseDetails.getCourseDetailsId());
                                map.put("courseDetailsName", courseDetails.getCourseDetailsName());
                                map.put("dyEpisodeId", courseDetails.getDyEpisodeId());
                                map.put("wxCourseDetailsId", courseDetails.getWxCourseDetailsId());
                            }
                        }
                    }
                    Object courseDetailsId = map.get("courseDetailsId");
                    if (courseDetailsId == null) {
                        //默认取第一集
                        CourseDetails courseDetails = courseDetailsDao.selectOne(new QueryWrapper<CourseDetails>().eq("course_id", courseId)
                                .orderByAsc("course_id").last(" limit 1"));
                        if (courseDetails != null) {
                            map.put("courseDetailsId", courseDetails.getCourseDetailsId());
                            map.put("courseDetailsName", courseDetails.getCourseDetailsName());
                            map.put("dyEpisodeId", courseDetails.getDyEpisodeId());
                            map.put("wxCourseDetailsId", courseDetails.getWxCourseDetailsId());
                            map.put("isCollect", 0);
                        }
                    }
                }
                return Result.success().put("data", new PageUtils(records, total, size, current));
            }
        }
        IPage<Map<String, Object>> mapIPage = baseMapper.selectCourseAdmin(pages, classifyId, title, isRecommend, status, bannerId,
                sort, userId, isPrice, over, wxCourse, dyCourse, wxShow, dyShow, isCut, priceType);
        List<Map<String, Object>> records = mapIPage.getRecords();
        for (Map<String, Object> map : records) {
            Object courseId = map.get("courseId");
            //默认取第一集
            CourseDetails courseDetails = courseDetailsDao.selectOne(new QueryWrapper<CourseDetails>().eq("course_id", courseId)
                    .orderByAsc("course_id").last(" limit 1"));
            if (courseDetails != null) {
                map.put("courseDetailsId", courseDetails.getCourseDetailsId());
                map.put("courseDetailsName", courseDetails.getCourseDetailsName());
                map.put("dyEpisodeId", courseDetails.getDyEpisodeId());
                map.put("wxCourseDetailsId", courseDetails.getWxCourseDetailsId());
            }
        }
        return Result.success().put("data", new PageUtils(mapIPage));
    }

    /*@Override
    public Result selectCourseById(Long id, Long userId) {
        //查询短剧信息
        Course bean = baseMapper.selectById(id);
        if (userId != null) {
            bean.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>().eq("user_id", userId).eq("course_id", id)));
        } else {
            bean.setIsCollect(0);
        }
        //查询用户是否购买了这本书
        CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);
        Orders one = ordersService.selectOrdersByCourseIdAndUserId(userId,id);
        if (courseUser != null) {
            bean.setListsDetail(courseDetailsDao.findByCourseId(id));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            courseUser.setUpdateTime(df.format(new Date()));
            courseUserDao.updateCourseTime(courseUser);
            bean.setIsMyCourse(2);
            bean.setOrders(one);
        }else{
            if(bean.getCourseType()==null || bean.getCourseType().equals(1)){
                bean.setListsDetail(courseDetailsDao.findByCourseIdNotUrl(id));
            }
            bean.setIsMyCourse(1);
        }
        return Result.success().put("data", bean);
    }*/

    @Override
    public Result selectCourseById(Integer page, Integer limit, Long id, Integer good) {
        Page<CourseDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", new PageUtils(courseDetailsDao.selectCoursePageByCourseId(pages, id, good)));
    }

    @Override
    public Result selectCourseTitle(Integer page, Integer limit, String title, Long userId) {
        //分页
        Page<Map<String, Object>> pages = new Page<>(page, limit);
        if (userId != null) {
            //记录或更新搜索内容
            appSearchService.insetAppSearch(title, userId);
        }
        //拼接模糊查询
        String title1 = null;
        if (StringUtils.isNotBlank(title)) {
            title1 = "%" + title + "%";
            return Result.success().put("data", new PageUtils(baseMapper.selectCourseTitle(pages, title1)));
        } else {
            return Result.error("请输入要搜索的内容！");
        }

    }

    @Override
    public Result synCourse() {
        if (sys) {
            return Result.error("视频正在同步中，请稍等！");
        }
        sys = true;
        singleThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String value = commonInfoService.findOne(250).getValue();
                    String s = HttpClientUtil.doGet(value);
                    log.error("返回值：" + s);
                    JSONArray jsonArray = JSONArray.parseArray(s);
                    for (int i = 2; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String cname = jsonObject.getString("cname");
                        String name = jsonObject.getString("name");
                        String m3u8url = jsonObject.getString("m3u8url");
                        String picurl = jsonObject.getString("picurl");
                        String description = jsonObject.getString("description");
                        Course course = baseMapper.selectOne(new QueryWrapper<Course>().eq("title", cname));
                        if (course == null) {
                            course = new Course();
                            course.setTitle(cname);
                            course.setTitleImg(picurl);
                            course.setPrice(BigDecimal.ZERO);
                            course.setPayNum(0);
                            course.setImg(picurl);
                            course.setDetails(description);
                            course.setIsDelete(0);
                            course.setCreateTime(DateUtils.format(new Date()));
                            course.setUpdateTime(course.getCreateTime());
                            course.setIsRecommend(0);
                            course.setStatus(2);
                            course.setIsPrice(2);
                            course.setViewCounts(0);
                            baseMapper.insert(course);
                        } else {
                            course.setTitle(cname);
                            course.setTitleImg(picurl);
                            course.setImg(picurl);
                            course.setDetails(description);
                            baseMapper.updateById(course);
                        }
                        Integer count = courseDetailsDao.selectCount(new QueryWrapper<CourseDetails>().eq("course_details_name", name));
                        if (count == 0) {
                            CourseDetails courseDetails = new CourseDetails();
                            courseDetails.setCourseId(course.getCourseId());
                            courseDetails.setCourseDetailsName(name);
                            courseDetails.setVideoUrl(m3u8url);
                            courseDetails.setCreateTime(DateUtils.format(new Date()));
                            courseDetails.setTitleImg(picurl);
                            courseDetails.setContent(description);
                            courseDetails.setGoodNum(0);
                            courseDetails.setPrice(BigDecimal.ZERO);
                            courseDetails.setIsPrice(2);
                            courseDetailsDao.insert(courseDetails);
                        } else {
                            CourseDetails courseDetails = courseDetailsDao.selectOne(new QueryWrapper<CourseDetails>().eq("course_details_name", name).last(" limit 1"));
                            courseDetails.setCourseDetailsName(name);
                            courseDetails.setVideoUrl(m3u8url);
                            courseDetails.setCreateTime(DateUtils.format(new Date()));
                            courseDetails.setTitleImg(picurl);
                            courseDetails.setContent(description);
                            courseDetailsDao.updateById(courseDetails);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("同步视频出错：" + e.getMessage(), e);
                } finally {
                    sys = false;
                }
            }
        });
        return Result.success();
    }

    @Override
    public Result updateCourseDetails(String ids, BigDecimal price, String content, String titleImg) {
        for (String id : ids.split(",")) {
            CourseDetails courseDetails = courseDetailsDao.selectById(Long.parseLong(id));
            courseDetails.setPrice(price);
            if (price.doubleValue() == 0) {
                courseDetails.setIsPrice(2);
            } else {
                courseDetails.setIsPrice(1);
            }
            courseDetails.setContent(content);
            courseDetails.setTitleImg(titleImg);
            courseDetailsDao.updateById(courseDetails);
        }
        String[] split = ids.split(",");
        List<Course> courses = baseMapper.selectBatchIds(Arrays.asList(split));
        redisUtils.deleteByPattern("page*");
        for (Course course : courses) {
            redisUtils.deleteByPattern(String.format("*%d*", course.getCourseId()));
        }
        return Result.success();
    }

    @Override
    public Result updateCourseStatus(String ids, Integer status) {
        for (String id : ids.split(",")) {
            Course course = baseMapper.selectById(Long.parseLong(id));
            course.setStatus(status);
            baseMapper.updateById(course);
            redisUtils.deleteByPattern(String.format("*%s*", id));
        }
        redisUtils.deleteByPattern("page*");
        return Result.success();
    }


    @Override
    public Result deleteCourseByIds(String ids) {
        for (String id : ids.split(",")) {
            baseMapper.deleteById(Long.parseLong(id));
            courseDetailsDao.delete(new QueryWrapper<CourseDetails>().eq("course_id", Long.parseLong(id)));
            redisUtils.deleteByPattern(String.format("*%s*", id));
        }
        redisUtils.deleteByPattern("page*");
        return Result.success();
    }

    @Override
    public Result deleteCourseDetailsByIds(String ids) {
        for (String id : ids.split(",")) {
            courseDetailsDao.deleteById(Long.parseLong(id));
            redisUtils.deleteByPattern(String.format("*%s*", id));
        }
        redisUtils.deleteByPattern("page*");
        return Result.success();
    }

    @Override
    public Result courseNotify(Long userId, Long courseId, Long courseDetailsId) {
        CourseUser courseUser = new CourseUser();
        //设置短剧id
        courseUser.setCourseId(courseId);
        courseUser.setCourseDetailsId(courseDetailsId);
        courseUser.setClassify(2);
        //设置用户id
        courseUser.setUserId(userId);

        //加入我的列表
        courseUserService.insertCourseUser(courseUser);
        return Result.success();
    }

    @Override
    public Result dyVideoUpload(Long courseId) {
        uploadVideo(courseId);
        return Result.success();
    }

    @Async
    public void uploadVideo(Long courseId) {
        try {
            String appid = commonInfoService.findOne(805).getValue();
            Course course = baseMapper.selectById(courseId);
            List<CourseDetails> courseDetailsList = courseDetailsDao.selectList(new QueryWrapper<CourseDetails>().eq("course_id", courseId));
            if (StringUtils.isEmpty(course.getDyImgId())) {
                //上传短剧封面图
                String imgUrl = "https://open.douyin.com/api/playlet/v2/resource/upload/";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("resource_type", 2);
                jsonObject.put("ma_app_id", appid);
                JSONObject image_meta = new JSONObject();
                image_meta.put("url", course.getTitleImg());
                jsonObject.put("image_meta", image_meta);
                String s = HttpClientUtil.doPostJson(imgUrl, jsonObject.toJSONString(), SenInfoCheckUtil.getDyToken());
                JSONObject jsonObject1 = JSONObject.parseObject(s);
                String err_no = jsonObject1.getString("err_no");
                if ("0".equals(err_no)) {
                    JSONObject data = jsonObject1.getJSONObject("data");
                    JSONObject image_result = data.getJSONObject("image_result");
                    String open_pic_id = image_result.getString("open_pic_id");
                    course.setDyImgId(open_pic_id);
                    baseMapper.updateById(course);
                }
            }
            if (StringUtils.isEmpty(course.getDyCourseId())) {
                //创建短剧
                String url = "https://open.douyin.com/api/playlet/v2/video/create/";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ma_app_id", appid);
                JSONObject album_info = new JSONObject();
                album_info.put("title", course.getTitle());
                album_info.put("seq_num", courseDetailsList.size());
                JSONArray cover_list = new JSONArray();
                cover_list.add(course.getDyImgId());
                album_info.put("cover_list", cover_list);
                album_info.put("year", DateUtils.format(new Date(), "yyyy"));
                album_info.put("album_status", "3");
                album_info.put("recommendation", course.getTitle());
                album_info.put("desp", course.getDetails());
                JSONArray tag_list = new JSONArray();
                if (course.getCourseLabelIds() != null) {
                    String[] split = course.getCourseLabelIds().split(",");
                    for (String tag : split) {
                        tag_list.add(tag);
                        if (tag_list.size() == 3) {
                            break;
                        }
                    }
                }
                album_info.put("tag_list", tag_list);
                if (StringUtils.isNotEmpty(course.getLicenseNum()) || StringUtils.isNotEmpty(course.getRegistrationNum()) ||
                        StringUtils.isNotEmpty(course.getKeyRecordNum()) || StringUtils.isNotEmpty(course.getOrdinaryRecordNum())) {
                    album_info.put("qualification", "2");
                    JSONObject record_info = new JSONObject();
                    if (StringUtils.isNotEmpty(course.getLicenseNum())) {
                        record_info.put("license_num", course.getLicenseNum());
                    } else if (StringUtils.isNotEmpty(course.getRegistrationNum())) {
                        record_info.put("registration_num", course.getRegistrationNum());
                    } else if (StringUtils.isNotEmpty(course.getOrdinaryRecordNum())) {
                        record_info.put("ordinary_record_num", course.getOrdinaryRecordNum());
                    } else {
                        record_info.put("key_record_num", course.getKeyRecordNum());
                    }
                    album_info.put("record_info", record_info);
                } else {
                    album_info.put("qualification", "1");
                }

                jsonObject.put("album_info", album_info);
                String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString(), SenInfoCheckUtil.getDyToken());
                JSONObject jsonObject1 = JSONObject.parseObject(s);
                String err_no = jsonObject1.getString("err_no");
                if ("0".equals(err_no)) {
                    JSONObject data = jsonObject1.getJSONObject("data");
                    String album_id = data.getString("album_id");
                    course.setDyCourseId(album_id);
                    baseMapper.updateById(course);
                }

            }
            //上传视频
            for (CourseDetails courseDetails : courseDetailsList) {

                if (StringUtils.isEmpty(courseDetails.getDyCourseDetailsId()) || courseDetails.getDyUrlStatus() == 3) {
                    String imgUrl = "https://open.douyin.com/api/playlet/v2/resource/upload/";
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("resource_type", 1);
                    jsonObject.put("ma_app_id", appid);
                    JSONObject video_meta = new JSONObject();
                    video_meta.put("url", courseDetails.getVideoUrl());
                    video_meta.put("title", courseDetails.getCourseDetailsName());
                    video_meta.put("description", courseDetails.getContent());
                    if (courseDetails.getVideoUrl().contains(".mp4")) {
                        video_meta.put("format", "mp4");
                    } else {
                        video_meta.put("format", "m3u8");
                    }
                    jsonObject.put("video_meta", video_meta);
                    String s = HttpClientUtil.doPostJson(imgUrl, jsonObject.toJSONString(), SenInfoCheckUtil.getDyToken());
                    JSONObject jsonObject1 = JSONObject.parseObject(s);
                    String err_no = jsonObject1.getString("err_no");
                    if (!"0".equals(err_no)) {
                        continue;
                    }
                    JSONObject data = jsonObject1.getJSONObject("data");
                    JSONObject video_result = data.getJSONObject("video_result");
                    String open_video_id = video_result.getString("open_video_id");
                    courseDetails.setDyCourseDetailsId(open_video_id);
                    courseDetails.setDyUrlStatus(1);
                    courseDetailsDao.updateById(courseDetails);
                }
                if (StringUtils.isEmpty(courseDetails.getDyImgId())) {
                    //上传短剧封面图
                    String imgUrl = "https://open.douyin.com/api/playlet/v2/resource/upload/";
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("resource_type", 2);
                    jsonObject.put("ma_app_id", appid);
                    JSONObject image_meta = new JSONObject();
                    image_meta.put("url", courseDetails.getTitleImg());
                    jsonObject.put("image_meta", image_meta);
                    String s = HttpClientUtil.doPostJson(imgUrl, jsonObject.toJSONString(), SenInfoCheckUtil.getDyToken());
                    JSONObject jsonObject1 = JSONObject.parseObject(s);
                    String err_no = jsonObject1.getString("err_no");
                    if (!"0".equals(err_no)) {
                        continue;
                    }
                    JSONObject data = jsonObject1.getJSONObject("data");
                    JSONObject image_result = data.getJSONObject("image_result");
                    String open_pic_id = image_result.getString("open_pic_id");
                    courseDetails.setDyImgId(open_pic_id);
                    courseDetailsDao.updateById(courseDetails);
                }
            }
            redisUtils.deleteByPattern("page*");
            redisUtils.deleteByPattern(String.format("*%d*", courseId));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("抖音上传视频出错：" + e.getMessage(), e);
        }

    }

    @Override
    public Result dyVideoAudit(Long courseId) {
        String appid = commonInfoService.findOne(805).getValue();
        Course course = baseMapper.selectById(courseId);
        String url = "https://open.douyin.com/api/playlet/v2/video/review/";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("album_id", course.getDyCourseId());
        jsonObject.put("ma_app_id", appid);
        String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString(), SenInfoCheckUtil.getDyToken());
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        String err_no = jsonObject1.getString("err_no");
        if (!"0".equals(err_no)) {
            return Result.error(jsonObject1.getString("err_msg"));
        }
        JSONObject data = jsonObject1.getJSONObject("data");
        String version = data.getString("version");
        course.setDyVersion(version);
        course.setDyStatus(1);
        baseMapper.updateById(course);
        redisUtils.deleteByPattern(String.format("*%d*", courseId));
        redisUtils.deleteByPattern("page*");
        return Result.success();
    }

    @Override
    public Result dyVideoUp(Long courseId) {
        Course course = baseMapper.selectById(courseId);
        String url = "https://open.douyin.com/api/playlet/v2/album/online/";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("album_id", course.getDyCourseId());
        jsonObject.put("operate", "2");
        jsonObject.put("version", course.getDyVersion());
        String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString(), SenInfoCheckUtil.getDyToken());
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        String err_no = jsonObject1.getString("err_no");
        if (!"0".equals(err_no)) {
            return Result.error(jsonObject1.getString("err_msg"));
        }
        course.setDyStatus(4);
        baseMapper.updateById(course);
        redisUtils.deleteByPattern(String.format("*%d*", courseId));
        redisUtils.deleteByPattern("page*");
        return Result.success();
    }

    @Override
    public Result setDyNotifyUrl(String notifyUrl) {
        notifyUrl += "/sqx_fast/app/course/notifyUrl";
        String url = "https://open.douyin.com/api/industry/v1/solution/set_impl";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", "管理员");
        jsonObject.put("release_reason", "配置短剧回调地址");
        JSONArray industry_impl_list = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("template_id", 20001);
        JSONArray open_ability_impl_list = new JSONArray();
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("ability_identity", "/msg/playlet/review/notify");
        jsonObject2.put("is_delete", false);
        jsonObject2.put("test_url", notifyUrl);
        jsonObject2.put("prod_url", notifyUrl);
        jsonObject2.put("ability_type", "2");
        jsonObject2.put("impl_name", "短剧回调消息实现配置");
        open_ability_impl_list.add(jsonObject2);
        jsonObject1.put("open_ability_impl_list", open_ability_impl_list);
        industry_impl_list.add(jsonObject1);
        jsonObject.put("industry_impl_list", industry_impl_list);
        jsonObject.put("app_config_item_list", new JSONArray());
        String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString(), SenInfoCheckUtil.getDyToken());
        log.error("抖音设置回调域名返回值：" + s);
        JSONObject jsonObject3 = JSONObject.parseObject(s);
        JSONObject data = jsonObject3.getJSONObject("data");
        String err_no = data.getString("error_code");
        if (!"0".equals(err_no)) {
            return Result.error(data.getString("description"));
        }
        return Result.success();
    }

    @Override
    public JSONObject notifyUrl(JSONObject jsonObject) {
        String type = jsonObject.getString("type");
        JSONObject msg = jsonObject.getJSONObject("msg");
        if ("upload_video".equals(type)) {
            String open_video_id = msg.getString("open_video_id");
            String success = msg.getString("success");
            CourseDetails courseDetails = courseDetailsDao.selectOne(new QueryWrapper<CourseDetails>().eq("dy_course_details_id", open_video_id));
            if (courseDetails != null) {
                if ("true".equals(success)) {
                    courseDetails.setDyUrlStatus(2);
                } else {
                    courseDetails.setDyUrlStatus(3);
                }
                courseDetailsDao.updateById(courseDetails);
                redisUtils.deleteByPattern(String.format("*%d*", courseDetails.getCourseId()));
                redisUtils.deleteByPattern("page*");
                //获取是否所有的集都上传成功 如果成功 则添加集
                Integer count = courseDetailsDao.selectCount(new QueryWrapper<CourseDetails>()
                        .eq("course_id", courseDetails.getCourseId()).in("dy_url_status", 1, 3));
                if (count == 0) {
                    videoEdit(courseDetails);
                }
            }
        } else if ("album_audit".equals(type)) {
            String album_id = msg.getString("album_id");
            Course course = baseMapper.selectOne(new QueryWrapper<Course>().eq("dy_course_id", album_id));
            if (course != null) {
                String audit_status = msg.getString("audit_status");
                if ("1".equals(audit_status)) {
                    course.setDyStatus(3);
                    course.setDyStatusContent(msg.getString("audit_msg"));
                } else {
                    course.setDyStatus(2);
                    course.setDyVersion(msg.getString("version"));
                }
                baseMapper.updateById(course);
                redisUtils.deleteByPattern(String.format("*%d*", course.getCourseId()));
                redisUtils.deleteByPattern("page*");
            }
        }
        if ("episode_audit".equals(type)) {
            String episode_id = msg.getString("episode_id");
            CourseDetails courseDetails = courseDetailsDao.selectOne(new QueryWrapper<CourseDetails>().eq("dy_episode_id", episode_id));
            if (courseDetails != null) {
                String audit_status = msg.getString("audit_status");
                if ("1".equals(audit_status)) {
                    courseDetails.setDyStatus(3);
                    courseDetails.setDyStatusContent(msg.getString("audit_msg"));
                } else {
                    courseDetails.setDyStatus(2);
                    courseDetails.setDyVersion(msg.getString("version"));
                }
                courseDetailsDao.updateById(courseDetails);
                redisUtils.deleteByPattern(String.format("*%d*", courseDetails.getCourseId()));
                redisUtils.deleteByPattern("page*");
            }
        }
        JSONObject result = new JSONObject();
        result.put("err_no", 0);
        result.put("err_tips", "success");
        return result;
    }

    @Async
    public void videoEdit(CourseDetails courseDetails) {
        //这里进行延迟操作 抖音不允许并发操作
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //没有待审核和审核失败的 吧所有的集进行上传
        List<CourseDetails> courseDetailsList = courseDetailsDao.selectList(new QueryWrapper<CourseDetails>()
                .eq("course_id", courseDetails.getCourseId()).orderByAsc("create_time"));
        String appid = commonInfoService.findOne(805).getValue();
        Course course = baseMapper.selectById(courseDetails.getCourseId());
        String url = "https://open.douyin.com/api/playlet/v2/video/edit/";
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("album_id", course.getDyCourseId());
        jsonObject1.put("ma_app_id", appid);
        int num = 1;
        JSONArray episode_info_list = new JSONArray();
        while (true) {

            CourseDetails courseDetails1 = courseDetailsList.get(num - 1);
            JSONObject episode = new JSONObject();
            episode.put("title", courseDetails1.getCourseDetailsName());
            episode.put("seq", num);
            JSONArray cover_list = new JSONArray();
            cover_list.add(courseDetails1.getDyImgId());
            episode.put("cover_list", cover_list);
            episode.put("open_video_id", courseDetails1.getDyCourseDetailsId());
            episode_info_list.add(episode);
            if (courseDetailsList.size() == num) {
                jsonObject1.put("episode_info_list", episode_info_list);
                String s = HttpClientUtil.doPostJson(url, jsonObject1.toJSONString(), SenInfoCheckUtil.getDyToken());
                JSONObject jsonObject2 = JSONObject.parseObject(s);
                JSONObject data = jsonObject2.getJSONObject("data");
                JSONObject episode_id_map = data.getJSONObject("episode_id_map");
                Map<String, Object> map = new HashMap<>(episode_id_map);
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    CourseDetails courseDetails2 = courseDetailsList.get(Integer.parseInt(key) - 1);
                    courseDetails2.setDyEpisodeId(String.valueOf(value));
                    courseDetailsDao.updateById(courseDetails2);
                }
                break;
            }
            if (episode_info_list.size() % 100 == 0) {
                jsonObject1.put("episode_info_list", episode_info_list);
                String s = HttpClientUtil.doPostJson(url, jsonObject1.toJSONString(), SenInfoCheckUtil.getDyToken());
                JSONObject jsonObject2 = JSONObject.parseObject(s);
                JSONObject data = jsonObject2.getJSONObject("data");
                JSONObject episode_id_map = data.getJSONObject("episode_id_map");
                Map<String, Object> map = new HashMap<>(episode_id_map);
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    CourseDetails courseDetails2 = courseDetailsList.get(Integer.parseInt(key) - 1);
                    courseDetails2.setDyEpisodeId(String.valueOf(value));
                    courseDetailsDao.updateById(courseDetails2);
                }
                episode_info_list = new JSONArray();
            }
            num++;
        }
        //对页面进行绑定 这里同样进行延迟操作
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        url = "https://open.douyin.com/api/playlet/v2/album/bind/";
        jsonObject1 = new JSONObject();
        jsonObject1.put("schema_bind_type", "1");

        for (CourseDetails courseDetails1 : courseDetailsList) {
            JSONObject single_schema_bind = new JSONObject();
            single_schema_bind.put("album_id", course.getDyCourseId());
            single_schema_bind.put("episode_id", courseDetails1.getDyEpisodeId());
            single_schema_bind.put("path", "me/detail/detail");
            JSONArray params = new JSONArray();
            JSONObject param1 = new JSONObject();
            param1.put("key", "id");
            param1.put("value", String.valueOf(course.getCourseId()));
            JSONObject param2 = new JSONObject();
            param2.put("key", "courseDetailsId");
            param2.put("value", String.valueOf(courseDetails1.getCourseDetailsId()));
            params.add(param1);
            params.add(param2);
            single_schema_bind.put("params", params);
            jsonObject1.put("single_schema_bind", single_schema_bind);
            String s = HttpClientUtil.doPostJson(url, jsonObject1.toJSONString(), SenInfoCheckUtil.getDyToken());
            log.info("绑定页面返回值：" + s);
        }
        redisUtils.deleteByPattern(String.format("*%d*", course.getCourseId()));
        redisUtils.deleteByPattern("page*");
    }

    @Override
    public Result uploadCourseDetails(Long courseDetailsId) {
        String appid = commonInfoService.findOne(805).getValue();
        CourseDetails courseDetails = courseDetailsDao.selectById(courseDetailsId);
        String imgUrl = "https://open.douyin.com/api/playlet/v2/resource/upload/";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resource_type", 1);
        jsonObject.put("ma_app_id", appid);
        JSONObject video_meta = new JSONObject();
        video_meta.put("url", courseDetails.getVideoUrl());
        video_meta.put("title", courseDetails.getCourseDetailsName());
        video_meta.put("description", courseDetails.getContent());
        if (courseDetails.getVideoUrl().contains("mp4")) {
            video_meta.put("format", "mp4");
        } else {
            video_meta.put("format", "m3u8");
        }
        jsonObject.put("video_meta", video_meta);
        String s = HttpClientUtil.doPostJson(imgUrl, jsonObject.toJSONString(), SenInfoCheckUtil.getDyToken());
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        String err_no = jsonObject1.getString("err_no");
        if (!"0".equals(err_no)) {
            return Result.error(jsonObject1.getString("err_msg"));
        }
        JSONObject data = jsonObject1.getJSONObject("data");
        JSONObject video_result = data.getJSONObject("video_result");
        String open_video_id = video_result.getString("open_video_id");
        courseDetails.setDyCourseDetailsId(open_video_id);
        courseDetails.setDyUrlStatus(1);
        courseDetailsDao.updateById(courseDetails);
        //上传短剧封面图
        imgUrl = "https://open.douyin.com/api/playlet/v2/resource/upload/";
        jsonObject = new JSONObject();
        jsonObject.put("resource_type", 2);
        jsonObject.put("ma_app_id", appid);
        JSONObject image_meta = new JSONObject();
        image_meta.put("url", courseDetails.getTitleImg());
        jsonObject.put("image_meta", image_meta);
        s = HttpClientUtil.doPostJson(imgUrl, jsonObject.toJSONString(), SenInfoCheckUtil.getDyToken());
        jsonObject1 = JSONObject.parseObject(s);
        err_no = jsonObject1.getString("err_no");
        if (!"0".equals(err_no)) {
            return Result.error(jsonObject1.getString("err_msg"));
        }
        data = jsonObject1.getJSONObject("data");
        JSONObject image_result = data.getJSONObject("image_result");
        String open_pic_id = image_result.getString("open_pic_id");
        courseDetails.setDyImgId(open_pic_id);
        courseDetailsDao.updateById(courseDetails);
        redisUtils.deleteByPattern(String.format("*%d*", courseDetails.getCourseId()));
        redisUtils.deleteByPattern("page*");
        return Result.success();
    }

    @Override
    public Result updateDyCourse(Course course) {
        baseMapper.updateById(course);
        if (course.getDyCourseId() != null) {
            //已经提交抖音 同步抖音
            String appid = commonInfoService.findOne(805).getValue();
            String url = "https://open.douyin.com/api/playlet/v2/video/edit/";
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("album_id", course.getDyCourseId());
            jsonObject1.put("ma_app_id", appid);
            List<CourseDetails> courseDetailsList = courseDetailsDao.selectList(new QueryWrapper<CourseDetails>().eq("course_id", course.getCourseId()));
            JSONObject album_info = new JSONObject();
            album_info.put("title", course.getTitle());
            album_info.put("seq_num", courseDetailsList.size());
            JSONArray cover_list = new JSONArray();
            cover_list.add(course.getDyImgId());
            album_info.put("cover_list", cover_list);
            album_info.put("year", DateUtils.format(new Date(), "yyyy"));
            album_info.put("album_status", "3");
            album_info.put("recommendation", course.getTitle());
            album_info.put("desp", course.getDetails());
            JSONArray tag_list = new JSONArray();
            if (course.getCourseLabelIds() != null) {
                String[] split = course.getCourseLabelIds().split(",");
                for (String tag : split) {
                    tag_list.add(tag);
                    if (tag_list.size() == 3) {
                        break;
                    }
                }
            }
            album_info.put("tag_list", tag_list);
            if (StringUtils.isNotEmpty(course.getLicenseNum()) || StringUtils.isNotEmpty(course.getRegistrationNum()) ||
                    StringUtils.isNotEmpty(course.getKeyRecordNum()) || StringUtils.isNotEmpty(course.getOrdinaryRecordNum())) {
                album_info.put("qualification", "2");
                JSONObject record_info = new JSONObject();
                if (StringUtils.isNotEmpty(course.getLicenseNum())) {
                    record_info.put("license_num", course.getLicenseNum());
                } else if (StringUtils.isNotEmpty(course.getRegistrationNum())) {
                    record_info.put("registration_num", course.getRegistrationNum());
                } else if (StringUtils.isNotEmpty(course.getOrdinaryRecordNum())) {
                    record_info.put("ordinary_record_num", course.getOrdinaryRecordNum());
                } else {
                    record_info.put("key_record_num", course.getKeyRecordNum());
                }
                album_info.put("record_info", record_info);
            } else {
                album_info.put("qualification", "1");
            }

            jsonObject1.put("album_info", album_info);
            jsonObject1.put("episode_info_list", new JSONArray());
            String s = HttpClientUtil.doPostJson(url, jsonObject1.toJSONString(), SenInfoCheckUtil.getDyToken());
            JSONObject jsonObject2 = JSONObject.parseObject(s);
            String err_no = jsonObject2.getString("err_no");
            if (!"0".equals(err_no)) {
                return Result.error(jsonObject2.getString("err_msg"));
            }
        }
        redisUtils.deleteByPattern(String.format("*%d*", course.getCourseId()));
        redisUtils.deleteByPattern("page*");
        return Result.success();
    }

    @Override
    public Result sysWxCourse() {
        sysWxCourses();
        return Result.success();
    }

    @Async
    public void sysWxCourses() {
        int offset = 0;
        while (true) {
            // 获取剧目列表
            // 该接口用于获取已提交的剧目列表。
            // https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/industry/mini-drama/mini_drama.html#_3-2-%E8%8E%B7%E5%8F%96%E5%89%A7%E7%9B%AE%E5%88%97%E8%A1%A8
            String url = "https://api.weixin.qq.com/wxa/sec/vod/listdramas?access_token=" + SenInfoCheckUtil.getMpToken();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("offset", offset);
            jsonObject.put("limit", 100);
            String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
            JSONObject jsonObject1 = JSONObject.parseObject(s);
            String errcode = jsonObject1.getString("errcode");
            if (!"0".equals(errcode)) {
                log.error("同步微信短剧失败：" + jsonObject1.getString("errmsg"));
                break;
            }
            JSONArray drama_info_list = jsonObject1.getJSONArray("drama_info_list");
            if (drama_info_list == null || drama_info_list.size() == 0) {
                break;
            }
            for (int i = 0; i < drama_info_list.size(); i++) {
                JSONObject jsonObject2 = drama_info_list.getJSONObject(i);
                String name = jsonObject2.getString("name");
                Course course = baseMapper.selectOne(new QueryWrapper<Course>().eq("title", name));
                if (course != null) {
                    course.setWxCourseId(jsonObject2.getString("drama_id"));
                    baseMapper.updateById(course);
                }
            }
            offset = offset + 100;
        }
        //同步集
        List<Course> courseList = baseMapper.selectList(new QueryWrapper<Course>().isNotNull("wx_course_id"));
        for (Course course : courseList) {
            offset = 0;
            while (true) {
                // 获取媒资列表
                // 该接口用于获取已上传到平台的媒资列表。
                // https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/industry/mini-drama/mini_drama.html#_2-1-%E8%8E%B7%E5%8F%96%E5%AA%92%E8%B5%84%E5%88%97%E8%A1%A8
                String url = "https://api.weixin.qq.com/wxa/sec/vod/listmedia?access_token=" + SenInfoCheckUtil.getMpToken();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("drama_id", Integer.parseInt(course.getWxCourseId()));
                jsonObject.put("limit", 100);
                jsonObject.put("offset", offset);
                String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
                JSONObject jsonObject1 = JSONObject.parseObject(s);
                String errcode = jsonObject1.getString("errcode");
                if (!"0".equals(errcode)) {
                    log.error("同步微信短剧集失败：" + jsonObject1.getString("errmsg"));
                    break;
                }
                JSONArray media_info_list = jsonObject1.getJSONArray("media_info_list");
                if (media_info_list == null || media_info_list.size() == 0) {
                    break;
                }
                for (int i = 0; i < media_info_list.size(); i++) {
                    JSONObject jsonObject2 = media_info_list.getJSONObject(i);
                    String name = jsonObject2.getString("name");
                    name = name.replace(".mp4", "");
                    name = name.replace(".avi", "");
                    name = name.replace(".wmv", "");
                    name = name.replace(".flv", "");
                    name = name.replace(".m3u8", "");
                    name = name.replace(".mov", "");

                    CourseDetails courseDetails = courseDetailsDao.selectOne(new QueryWrapper<CourseDetails>()
                            .eq("course_id", course.getCourseId()).eq("course_details_name", name));
                    // course.courseId=1189 name="穆少别虐了 - 第1集", 有空格的
                    if (courseDetails != null) {
                        courseDetails.setWxCourseDetailsId(jsonObject2.getString("media_id"));
                        courseDetailsDao.updateById(courseDetails);
                    }
                }
                offset = offset + 100;
            }
            redisUtils.deleteByPattern(String.format("*%d*", course.getCourseId()));
        }
        redisUtils.deleteByPattern("page*");
    }


    @Override
    public Result selectWxVideoUrl(String wxCourseDetailsIds) {
        List<Map<String, String>> list = new ArrayList<>();
        for (String wxCourseDetailsIdStr : wxCourseDetailsIds.split(",")) {
            Long wxCourseDetailsId = Long.parseLong(wxCourseDetailsIdStr);
            String url = "https://api.weixin.qq.com/wxa/sec/vod/getmedialink?access_token=" + SenInfoCheckUtil.getMpToken();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("media_id", wxCourseDetailsId);
            jsonObject.put("t", (System.currentTimeMillis() / 1000) + 7200);
            String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
            JSONObject jsonObject1 = JSONObject.parseObject(s);
            String errcode = jsonObject1.getString("errcode");
            if (!"0".equals(errcode)) {
                log.error(wxCourseDetailsIdStr + "   获取微信播放链接失败：" + jsonObject1.getString("errmsg"));
//                return Result.error("获取微信播放链接失败："+jsonObject1.getString("errmsg"));
                continue;
            }
            JSONObject media_info = jsonObject1.getJSONObject("media_info");
            String duration = media_info.getString("duration");
            String cover_url = media_info.getString("cover_url");
            String mp4_url = media_info.getString("mp4_url");
            Map<String, String> result = new HashMap<>();
            result.put("duration", duration);
            result.put("cover_url", cover_url);
            result.put("mp4_url", mp4_url);
            result.put("wxCourseDetailsId", String.valueOf(wxCourseDetailsId));
            list.add(result);
        }
        return Result.success().put("data", list);
    }

    @Override
    public Result courseListExcelIn(MultipartFile file) throws IOException {
        List<CourseIn> courseList = ExcelUtils.importExcel(file, 2, 1, CourseIn.class);
        if (CollectionUtils.isEmpty(courseList)) {
            return Result.error("Excel数据为空，excel转化失败！");
        }
        //当前行索引（Excel的数据从第几行开始,就填写几）
        int index = 4;
        //失败条数
        int repeat = 0;
        //成功条数
        int successIndex = 0;
        //空数据
        int emptyCount = 0;
        for (CourseIn courseIn : courseList) {
            if (courseIn.getTitle() == null) {
                emptyCount++;
                continue;
            }
            if (courseIn.getIsPrice() != 2) {
                if (courseIn.getPrice() == null || courseIn.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    return Result.error("第【" + index + "】行数据为收费短剧,但并没有设置价格");
                }
            }
            index++;
            Integer count = baseMapper.selectCount(new QueryWrapper<Course>().eq("title", courseIn.getTitle()));
            if (count <= 0) {
                Course course = new Course();
                BeanUtils.copyProperties(courseIn, course);
                course.setCreateTime(DateUtils.format(new Date()));
                course.setUpdateTime(DateUtils.format(new Date()));
                int result = baseMapper.insert(course);
                if (result > 0) {
                    successIndex++;
                }
            } else {
                repeat++;
            }

        }
        return Result.success("导入成功,共新增【" + successIndex + "】条,失败【" + (courseList.size() - successIndex - emptyCount) + "】条,其中过滤重复数据【" + repeat + "】条");

    }

    @Override
    public List<Course> selectCourseListByCourseIds(List<Integer> courseIds) {
        return baseMapper.selectList(new QueryWrapper<Course>().in("course_id", courseIds));
    }


}
