package com.sqx.modules.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.RedisUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
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
import com.sqx.modules.course.service.CourseDetailsService;
import com.sqx.modules.course.vo.CourseDetailsIn;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.performer.service.PerformerService;
import com.sqx.modules.platform.entity.CoursePerformer;
import com.sqx.modules.platform.entity.CourseScore;
import com.sqx.modules.platform.service.CoursePerformerService;
import com.sqx.modules.platform.service.CourseScoreService;
import com.sqx.modules.utils.EasyPoi.ExcelUtils;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseDetailsServiceImpl extends ServiceImpl<CourseDetailsDao, CourseDetails> implements CourseDetailsService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseCollectDao courseCollectDao;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CourseUserDao courseUserDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private CommonInfoService commonInfoService;
    @Resource
    private CourseScoreService courseScoreService;
    @Resource
    private CoursePerformerService coursePerformerService;
    @Resource
    private PerformerService performerService;


    @Override
    public Result insert(CourseDetails courseDetails) {
        if (courseDetails.getGoodNum() == null) {
            courseDetails.setGoodNum(0);
        }
        baseMapper.insert(courseDetails);
        redisUtils.deleteByPattern("page*");
        redisUtils.deleteByPattern(String.format("*%d*", courseDetails.getCourseId()));
        return Result.success();
    }

    @Override
    public Result updateCourseDetails(CourseDetails courseDetails) {
        redisUtils.deleteByPattern("page*");
        redisUtils.deleteByPattern(String.format("*%d*", courseDetails.getCourseId()));
        baseMapper.updateById(courseDetails);
        return Result.success();
    }

    @Override
    public Result deleteCourseDetails(String ids) {
        String[] split = ids.split(",");
        List<CourseDetails> courseDetails = baseMapper.selectBatchIds(Arrays.asList(split));
        for (CourseDetails courseDetail : courseDetails) {
            redisUtils.deleteByPattern(String.format("*%d*", courseDetail.getCourseId()));
        }
        redisUtils.deleteByPattern("page*");
        baseMapper.deleteCourseDetails(split);
        return Result.success();
    }


    /**
     * 根据ID查询短剧详情
     *
     * @param id              短剧ID
     * @param token           用户认证令牌
     * @param courseDetailsId 短剧详情ID
     * @return 返回短剧详情结果
     */
    @Override
    public Result selectCourseDetailsById(Long id, String token, String courseDetailsId) {
        // Redis缓存键名构造
        String redisCourseDetailsName = "selectCourseDetailsById_" + id;
        // 尝试从Redis缓存中获取短剧详情字符串
        String s1 = redisUtils.get(redisCourseDetailsName);
        // 如果缓存中没有数据，则从数据库中查询
        if (StringUtils.isEmpty(s1)) {
            // 通过短剧ID从数据库中查询短剧信息
            Course bean = courseDao.selectById(id);

            // 查询总分
            if (bean.getScore() == null) {
                List<CourseScore> scores = courseScoreService.list(new QueryWrapper<CourseScore>().eq("course_id", bean.getCourseId()));
                // 计算平均分
                if (!scores.isEmpty()) {
                    bean.setScore(scores.stream().map(CourseScore::getScore).reduce(BigDecimal::add).orElse(new BigDecimal(0)).divide(new BigDecimal(scores.size()), 1, RoundingMode.HALF_UP));
                }
            }
            // 查询演员
            List<CoursePerformer> coursePerformerList = coursePerformerService.list(new QueryWrapper<CoursePerformer>().eq("course_id", bean.getCourseId()));
            if (!coursePerformerList.isEmpty()) {

                List<Performer> performerList = performerService.list(new QueryWrapper<Performer>().in("id", coursePerformerList.stream().map(CoursePerformer::getPerformerId).collect(Collectors.toList())));
                bean.setPerformerList(performerList);
            }


            /*
            // 以下为注释掉的代码，用于统计短剧的浏览量
            if(bean.getViewCounts()==null){
                bean.setViewCounts(1);
            }else{
                bean.setViewCounts(bean.getViewCounts()+1);
            }
            // 更新数据库中的浏览量信息
            courseDao.updateById(bean);*/
            // 初始化用户ID为null
            Long userId = null;
            // 如果token非空，尝试解析token获取用户ID
            if (StringUtils.isNotEmpty(token)) {
                // 从token中获取用户信息
                Claims claims = jwtUtils.getClaimByToken(token);
                // 验证claims是否非空且token未过期
                if (claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())) {
                    // 将claims中的用户ID转换为Long类型
                    userId = Long.parseLong(claims.getSubject());
                }
            }
            // 初始化短剧收藏状态为0
            bean.setIsCollect(0);
            // 如果用户ID非空，进一步处理用户相关逻辑
            if (userId != null) {
                // 查询用户是否收藏了该短剧
                bean.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("classify", 1).eq("course_id", bean.getCourseId())));

                // 查询用户的评分
                CourseScore courseScore = courseScoreService.getOne(new QueryWrapper<CourseScore>()
                        .eq("user_id", userId).eq("course_id", bean.getCourseId()));

                bean.setUserScore(courseScore != null ? courseScore.getScore() : null);

                // 根据用户ID查询用户信息
                UserEntity userEntity = userService.selectUserById(userId);
                // 查询用户是否购买了整集
                CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);

                // 使用Redis缓存短剧详情列表
                String redisCourseDetailsListName = "selectCourseDetailsList_" + id + "user_id" + userId;
                String redisCourseDetailsList = redisUtils.get(redisCourseDetailsListName);
                List<CourseDetails> courseDetailsList = null;
                if (StringUtils.isEmpty(redisCourseDetailsList)) {
                    // 如果Redis中没有缓存数据，则从数据库查询
                    courseDetailsList = baseMapper.findByCourseId(id, userId);
                    // 将查询结果转换为JSON字符串并存入Redis
                    redisUtils.set(redisCourseDetailsListName, JSONObject.toJSONString(courseDetailsList));
                } else {
                    // 如果Redis中有缓存数据，则直接读取并转换为对象列表
                    String s = redisUtils.get(redisCourseDetailsListName);
                    courseDetailsList = JSON.parseArray(s, CourseDetails.class);
                }

                // 使用Redis缓存未包含视频URL的短剧详情列表
                String redisCourseDetailsNoUrlListName = "selectCourseDetailsNoUrlList_" + id + "user_id" + userId;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if (StringUtils.isEmpty(redisCourseDetailsNoUrlList)) {
                    // 如果Redis中没有缓存数据，则从数据库查询
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    // 将查询结果转换为JSON字符串并存入Redis
                    redisUtils.set(redisCourseDetailsNoUrlListName, JSONObject.toJSONString(courseDetailsNotList));
                } else {
                    // 如果Redis中有缓存数据，则直接读取并转换为对象列表
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }

                // 标记用户是否为特定会员类型
                boolean flag = false;

                // 如果不是云短剧 根据会员类型选择性放行
                if (bean.getIsPrice() != 3) {

                    // 获取会员类型信息
                    String value = commonInfoService.findOne(887).getValue();
                    // 判断角色 1是梵会员  2是剧达人  3剧荐官   4推荐人
                    String[] split = value.split(",");

                    // 根据会员类型判断用户是否享有特权
                    for (String member : split) {
                        if ("1".equals(member)) {
                            if (userEntity.getAgencyIndex() != null && userEntity.getAgencyIndex() == 1) {
                                flag = true;
                            }
                        } else if ("2".equals(member)) {
                            if (userEntity.getAgencyIndex() != null && userEntity.getAgencyIndex() == 2) {
                                flag = true;
                            }
                        } else if ("3".equals(member)) {
                            if (userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
                                flag = true;
                            }
                        } else if ("4".equals(member)) {
                            if (userEntity.getIsRecommend() != null && userEntity.getIsRecommend() == 1) {
                                flag = true;
                            }
                        }
                    }
                }

                // 查询用户是否购买了全集
                if (courseUser != null || (flag)) {
                    // 用户购买了整集，设置包含全部详情的列表
                    bean.setListsDetail(courseDetailsList);
                } else {
                    // 用户未购买整集，设置不包含视频URL的详情列表
                    bean.setListsDetail(courseDetailsNotList);
                    // 查询用户是否单独购买了集
                    List<CourseUser> courseUsers = courseUserDao.selectCourseUserList(id, userId);
                    // 如果用户单独购买了某些集，则更新这些集的视频URL
                    if (courseUsers.size() > 0) {
                        for (CourseUser courseUser1 : courseUsers) {
                            for (CourseDetails courseDetails : bean.getListsDetail()) {
                                if (courseUser1.getCourseDetailsId().equals(courseDetails.getCourseDetailsId())) {
                                    CourseDetails courseDetails1 = baseMapper.selectById(courseDetails.getCourseDetailsId());
                                    courseDetails.setVideoUrl(courseDetails1.getVideoUrl());
                                }
                            }
                        }
                    }
                }
                // 如果短剧详情未在缓存中找到
            } else {
                // 构造Redis中短剧详情列表的键名
                String redisCourseDetailsNoUrlListName = "selectCourseDetailsNoUrlList_" + id;
                // 从Redis中获取短剧详情列表字符串
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                // 初始化未包含URL的短剧详情列表
                List<CourseDetails> courseDetailsNotList = null;
                // 如果Redis中没有短剧详情列表缓存数据
                if (StringUtils.isEmpty(redisCourseDetailsNoUrlList)) {
                    // 通过数据库查询短剧详情列表，排除已收藏的URL
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    // 将查询结果转为JSON字符串并存入Redis
                    redisUtils.set(redisCourseDetailsNoUrlListName, JSONObject.toJSONString(courseDetailsNotList));
                } else {
                    // 从Redis中获取短剧详情列表字符串
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    // 将JSON字符串解析为短剧详情列表
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }
                // 设置短剧详情列表到返回对象
                bean.setListsDetail(courseDetailsNotList);
            }

            // 如果短剧详情ID不为空
            if (courseDetailsId != null) {
                // 如果短剧详情列表存在且不为空
                if (bean.getListsDetail().size() > 0) {
                    // 遍历短剧详情列表
                    for (CourseDetails courseDetails : bean.getListsDetail()) {
                        // 在列表中查找与给定短剧详情ID匹配的项
                        if (courseDetails.getCourseDetailsId().equals(Long.parseLong(courseDetailsId)) && StringUtils.isNotEmpty(courseDetails.getWxCourseDetailsId())) {
                            // 获取微信内部的视频链接
                            String url = "https://api.weixin.qq.com/wxa/sec/vod/getmedialink?access_token=" + SenInfoCheckUtil.getMpToken();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("media_id", courseDetails.getWxCourseDetailsId());
                            jsonObject.put("t", (System.currentTimeMillis() / 1000) + 7200);
                            String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
                            JSONObject jsonObject1 = JSONObject.parseObject(s);
                            String errcode = jsonObject1.getString("errcode");
                            // 检查是否成功获取视频链接
                            if (!"0".equals(errcode)) {
                                return Result.error("获取微信播放链接失败：" + jsonObject1.getString("errmsg"));
                            }
                            JSONObject media_info = jsonObject1.getJSONObject("media_info");
                            String mp4_url = media_info.getString("mp4_url");
                            // 将链接存储在短剧详情对象中
                            courseDetails.setWxUrl(mp4_url);
                            break;
                        }
                    }
                }
            }
            // 将更新后的短剧详情列表存储到Redis中
            redisUtils.set(redisCourseDetailsName, bean);
            // 返回成功结果，包含更新后的短剧详情列表
            return Result.success().put("data", bean);
        } else {
            // 从Redis中获取短剧详情数据
            String ss = redisUtils.get(redisCourseDetailsName);
            // 将JSON字符串转换为Course对象
            Course bean = JSONObject.parseObject(ss, Course.class);

            // 查询总分
            if (bean.getScore() == null) {
                List<CourseScore> scores = courseScoreService.list(new QueryWrapper<CourseScore>().eq("course_id", bean.getCourseId()));
                // 计算平均分
                if (!scores.isEmpty()) {
                    bean.setScore(scores.stream().map(CourseScore::getScore).reduce(BigDecimal::add).orElse(new BigDecimal(0)).divide(new BigDecimal(scores.size()), 1, RoundingMode.HALF_UP));
                }
            }
            // 查询演员
            List<CoursePerformer> coursePerformerList = coursePerformerService.list(new QueryWrapper<CoursePerformer>().eq("course_id", bean.getCourseId()));
            if (!coursePerformerList.isEmpty()) {
                List<Performer> performerList = performerService.list(new QueryWrapper<Performer>().in("id", coursePerformerList.stream().map(CoursePerformer::getPerformerId).collect(Collectors.toList())));
                bean.setPerformerList(performerList);
            }

            // 初始化用户ID为null
            Long userId = null;
            // 如果token存在且不为空
            if (StringUtils.isNotEmpty(token)) {
                // 通过token获取claims信息
                Claims claims = jwtUtils.getClaimByToken(token);
                // 如果claims存在且token未过期
                if (claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())) {
                    // 将claims中的用户ID转换为Long类型
                    userId = Long.parseLong(claims.getSubject());
                }
            }
            // 初始化是否收藏状态为0
            bean.setIsCollect(0);
            // 如果用户ID存在
            if (userId != null) {
                // 查询用户是否收藏了短剧
                bean.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("classify", 1).eq("course_id", bean.getCourseId())));

                // 查询用户的评分
                CourseScore courseScore = courseScoreService.getOne(new QueryWrapper<CourseScore>()
                        .eq("user_id", userId).eq("course_id", bean.getCourseId()));

                bean.setUserScore(courseScore != null ? courseScore.getScore() : null);

                // 查询用户信息
                UserEntity userEntity = userService.selectUserById(userId);
                // 查询用户是否购买了整集
                CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);

                // 缓存所有集的详情到Redis
                String redisCourseDetailsListName = "selectCourseDetailsList_" + id + "user_id" + userId;
                String redisCourseDetailsList = redisUtils.get(redisCourseDetailsListName);
                List<CourseDetails> courseDetailsList = null;
                if (StringUtils.isEmpty(redisCourseDetailsList)) {
                    // 如果Redis中没有缓存数据，则从数据库查询
                    courseDetailsList = baseMapper.findByCourseId(id, userId);
                    // 将查询结果存入Redis
                    redisUtils.set(redisCourseDetailsListName, JSONObject.toJSONString(courseDetailsList));
                } else {
                    // 如果Redis中有缓存数据，则直接获取
                    courseDetailsList = JSON.parseArray(redisCourseDetailsList, CourseDetails.class);
                }

                // 缓存没有视频URL的短剧详情到Redis
                String redisCourseDetailsNoUrlListName = "selectCourseDetailsNoUrlList_" + id + "user_id" + userId;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if (StringUtils.isEmpty(redisCourseDetailsNoUrlList)) {
                    // 如果Redis中没有缓存数据，则从数据库查询
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    // 将查询结果存入Redis
                    redisUtils.set(redisCourseDetailsNoUrlListName, JSONObject.toJSONString(courseDetailsNotList));
                } else {
                    // 如果Redis中有缓存数据，则直接获取
                    courseDetailsNotList = JSON.parseArray(redisCourseDetailsNoUrlList, CourseDetails.class);
                }

                // 标记用户是否为特定会员类型
                boolean flag = false;

                // 如果不是云短剧 根据会员类型选择性放行
                if (bean.getIsPrice() != 3) {

                    // 获取会员类型配置
                    String value = commonInfoService.findOne(887).getValue();
                    // 划分会员类型
                    String[] split = value.split(",");

                    // 判断用户是否为特定会员类型
                    for (String member : split) {
                        if ("1".equals(member)) {
                            if (userEntity.getAgencyIndex() != null && userEntity.getAgencyIndex() == 1) {
                                flag = true;
                            }
                        } else if ("2".equals(member)) {
                            if (userEntity.getAgencyIndex() != null && userEntity.getAgencyIndex() == 2) {
                                flag = true;
                            }
                        } else if ("3".equals(member)) {
                            if (userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
                                flag = true;
                            }
                        } else if ("4".equals(member)) {
                            if (userEntity.getIsRecommend() != null && userEntity.getIsRecommend() == 1) {
                                flag = true;
                            }
                        }
                    }
                }

                // 根据用户是否购买整集或是否为特定会员类型，设置短剧详情列表
                if (courseUser != null || flag) {
                    bean.setListsDetail(courseDetailsList);
                } else {
                    bean.setListsDetail(courseDetailsNotList);
                    // 查询用户是否单独购买了集
                    List<CourseUser> courseUsers = courseUserDao.selectCourseUserList(id, userId);
                    if (courseUsers.size() > 0) {
                        for (CourseUser courseUser1 : courseUsers) {
                            for (CourseDetails courseDetails : bean.getListsDetail()) {
                                if (courseUser1.getCourseDetailsId().equals(courseDetails.getCourseDetailsId())) {
                                    CourseDetails courseDetails1 = baseMapper.selectById(courseDetails.getCourseDetailsId());
                                    courseDetails.setVideoUrl(courseDetails1.getVideoUrl());
                                }
                            }
                        }
                    }
                }
            } else {
                // 如果用户ID不存在，则直接从Redis或数据库获取短剧详情列表，不带视频URL
                String redisCourseDetailsNoUrlListName = "selectCourseDetailsNoUrlList_" + id;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if (StringUtils.isEmpty(redisCourseDetailsNoUrlList)) {
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName, JSONObject.toJSONString(courseDetailsNotList));
                } else {
                    courseDetailsNotList = JSON.parseArray(redisCourseDetailsNoUrlList, CourseDetails.class);
                }
                bean.setListsDetail(courseDetailsNotList);
            }
            // 如果短剧详情ID不为空
            if (courseDetailsId != null) {
                // 如果短剧详情列表大于0
                if (bean.getListsDetail().size() > 0) {
                    // 遍历短剧详情列表
                    for (CourseDetails courseDetails : bean.getListsDetail()) {
                        // 如果当前短剧详情的ID与给定的短剧详情ID相等且微信短剧详情ID不为空
                        if (courseDetails.getCourseDetailsId().equals(Long.parseLong(courseDetailsId)) && StringUtils.isNotEmpty(courseDetails.getWxCourseDetailsId())) {
                            // 在微信环境内获取视频链接
                            String url = "https://api.weixin.qq.com/wxa/sec/vod/getmedialink?access_token=" + SenInfoCheckUtil.getMpToken();
                            JSONObject jsonObject = new JSONObject();
                            // 将微信短剧详情ID放入JSON对象
                            jsonObject.put("media_id", courseDetails.getWxCourseDetailsId());
                            // 设置过期时间戳
                            jsonObject.put("t", (System.currentTimeMillis() / 1000) + 7200);
                            // 发起HTTP请求获取视频链接
                            String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
                            // 解析返回的JSON对象
                            JSONObject jsonObject1 = JSONObject.parseObject(s);
                            // 获取错误码
                            String errcode = jsonObject1.getString("errcode");
                            // 如果错误码不为0，则获取视频链接失败
                            if (!"0".equals(errcode)) {
                                return Result.error("获取微信播放链接失败：" + jsonObject1.getString("errmsg"));
                            }
                            // 获取媒体信息
                            JSONObject media_info = jsonObject1.getJSONObject("media_info");
                            // 获取mp4视频链接
                            String mp4_url = media_info.getString("mp4_url");
                            // 设置短剧详情的微信视频链接
                            courseDetails.setWxUrl(mp4_url);
                            // 找到对应的短剧详情后，停止遍历
                            break;
                        }
                    }
                }
            }
            // 返回包含短剧详情列表的结果对象
            return Result.success().put("data", bean);
        }

    }

    @Override
    public Result selectCourseDetailsByTitle(String token, String title) {
        Course course = courseDao.selectOne(new QueryWrapper<Course>().eq("title", title).eq("is_delete", 0));
        if (course == null) {
            return Result.success();
        }
        Long courseDetailsId = null;
        Long id = course.getCourseId();
        String redisCourseDetailsName = "selectCourseDetailsById_" + id;
        String s1 = redisUtils.get(redisCourseDetailsName);
        if (StringUtils.isEmpty(s1)) {
            Course bean = courseDao.selectById(id);

            // 查询总分
            if (bean.getScore() == null) {
                List<CourseScore> scores = courseScoreService.list(new QueryWrapper<CourseScore>().eq("course_id", bean.getCourseId()));
                // 计算平均分
                if (!scores.isEmpty()) {
                    bean.setScore(scores.stream().map(CourseScore::getScore).reduce(BigDecimal::add).orElse(new BigDecimal(0)).divide(new BigDecimal(scores.size()), 1, RoundingMode.HALF_UP));
                }
            }
            // 查询演员
            List<CoursePerformer> coursePerformerList = coursePerformerService.list(new QueryWrapper<CoursePerformer>().eq("course_id", bean.getCourseId()));
            if (!coursePerformerList.isEmpty()) {
                List<Performer> performerList = performerService.list(new QueryWrapper<Performer>().in("id", coursePerformerList.stream().map(CoursePerformer::getPerformerId).collect(Collectors.toList())));
                bean.setPerformerList(performerList);
            }

            Long userId = null;
            if (StringUtils.isNotEmpty(token)) {
                Claims claims = jwtUtils.getClaimByToken(token);
                if (claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())) {
                    userId = Long.parseLong(claims.getSubject());
                }
            }
            bean.setIsCollect(0);
            if (userId != null) {
                CourseCollect courseCollect = courseCollectDao.selectOne(new QueryWrapper<CourseCollect>().eq("classify", 3).eq("user_id", userId).last(" order by update_time desc limit 1 "));
                if (courseCollect != null) {
                    courseDetailsId = courseCollect.getCourseDetailsId();
                }
                bean.setCourseDetailsId(courseDetailsId);
                bean.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("classify", 1).eq("course_id", bean.getCourseId())));
                UserEntity userEntity = userService.selectUserById(userId);
                // 查询用户是否购买了整集
                CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);

                // 查询用户的评分
                CourseScore courseScore = courseScoreService.getOne(new QueryWrapper<CourseScore>()
                        .eq("user_id", userId).eq("course_id", bean.getCourseId()));

                bean.setUserScore(courseScore != null ? courseScore.getScore() : null);


                // 直接直接通过redis缓存所有集
                String redisCourseDetailsListName = "selectCourseDetailsList_" + id + "user_id" + userId;
                String redisCourseDetailsList = redisUtils.get(redisCourseDetailsListName);
                List<CourseDetails> courseDetailsList = null;
                if (StringUtils.isEmpty(redisCourseDetailsList)) {
                    courseDetailsList = baseMapper.findByCourseId(id, userId);
                    redisUtils.set(redisCourseDetailsListName, JSONObject.toJSONString(courseDetailsList));
                } else {
                    String s = redisUtils.get(redisCourseDetailsListName);
                    courseDetailsList = JSON.parseArray(s, CourseDetails.class);
                }

                String redisCourseDetailsNoUrlListName = "selectCourseDetailsNoUrlList_" + id + "user_id" + userId;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if (StringUtils.isEmpty(redisCourseDetailsNoUrlList)) {
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName, JSONObject.toJSONString(courseDetailsNotList));
                } else {
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }

                String value = commonInfoService.findOne(887).getValue();
                // 判断角色 1是梵会员  2是剧达人  3剧荐官   4推荐人
                String[] split = value.split(",");
                boolean flag = false;

                for (String member : split) {
                    if ("1".equals(member)) {
                        if (userEntity.getAgencyIndex() != null && userEntity.getAgencyIndex() == 1) {
                            flag = true;
                        }
                    } else if ("2".equals(member)) {
                        if (userEntity.getAgencyIndex() != null && userEntity.getAgencyIndex() == 2) {
                            flag = true;
                        }
                    } else if ("3".equals(member)) {
                        if (userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
                            flag = true;
                        }
                    } else if ("4".equals(member)) {
                        if (userEntity.getIsRecommend() != null && userEntity.getIsRecommend() == 1) {
                            flag = true;
                        }
                    }
                }

                if (courseUser != null || (flag)) {
                    // 用户购买了整集
                    bean.setListsDetail(courseDetailsList);
                } else {
                    bean.setListsDetail(courseDetailsNotList);
                    // 查询用户是否单独购买了集
                    List<CourseUser> courseUsers = courseUserDao.selectCourseUserList(id, userId);
                    if (courseUsers.size() > 0) {
                        for (CourseUser courseUser1 : courseUsers) {
                            for (CourseDetails courseDetails : bean.getListsDetail()) {
                                if (courseUser1.getCourseDetailsId().equals(courseDetails.getCourseDetailsId())) {
                                    CourseDetails courseDetails1 = baseMapper.selectById(courseDetails.getCourseDetailsId());
                                    courseDetails.setVideoUrl(courseDetails1.getVideoUrl());
                                }
                            }
                        }
                    }
                }
            } else {
                String redisCourseDetailsNoUrlListName = "selectCourseDetailsNoUrlList_" + id;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if (StringUtils.isEmpty(redisCourseDetailsNoUrlList)) {
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName, JSONObject.toJSONString(courseDetailsNotList));
                } else {
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }
                bean.setListsDetail(courseDetailsNotList);
            }
            if (courseDetailsId != null) {
                if (bean.getListsDetail().size() > 0) {
                    for (CourseDetails courseDetails : bean.getListsDetail()) {
                        if (courseDetails.getCourseDetailsId().equals(courseDetailsId) && StringUtils.isNotEmpty(courseDetails.getWxCourseDetailsId())) {
                            // 微信内
                            String url = "https://api.weixin.qq.com/wxa/sec/vod/getmedialink?access_token=" + SenInfoCheckUtil.getMpToken();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("media_id", courseDetails.getWxCourseDetailsId());
                            jsonObject.put("t", (System.currentTimeMillis() / 1000) + 7200);
                            String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
                            JSONObject jsonObject1 = JSONObject.parseObject(s);
                            String errcode = jsonObject1.getString("errcode");
                            if (!"0".equals(errcode)) {
                                return Result.error("获取微信播放链接失败：" + jsonObject1.getString("errmsg"));
                            }
                            JSONObject media_info = jsonObject1.getJSONObject("media_info");
                            String mp4_url = media_info.getString("mp4_url");
                            courseDetails.setWxUrl(mp4_url);
                            break;
                        }
                    }
                }
            }
            redisUtils.set(redisCourseDetailsName, bean);
            return Result.success().put("data", bean);
        } else {
            String ss = redisUtils.get(redisCourseDetailsName);
            Course bean = JSONObject.parseObject(ss, Course.class);

            // 查询总分
            if (bean.getScore() == null) {
                List<CourseScore> scores = courseScoreService.list(new QueryWrapper<CourseScore>().eq("course_id", bean.getCourseId()));
                // 计算平均分
                if (!scores.isEmpty()) {
                    bean.setScore(scores.stream().map(CourseScore::getScore).reduce(BigDecimal::add).orElse(new BigDecimal(0)).divide(new BigDecimal(scores.size()), 1, RoundingMode.HALF_UP));
                }
            }
            // 查询演员
            List<CoursePerformer> coursePerformerList = coursePerformerService.list(new QueryWrapper<CoursePerformer>().eq("course_id", bean.getCourseId()));
            if (!coursePerformerList.isEmpty()) {
                List<Performer> performerList = performerService.list(new QueryWrapper<Performer>().in("id", coursePerformerList.stream().map(CoursePerformer::getPerformerId).collect(Collectors.toList())));
                bean.setPerformerList(performerList);
            }

            Long userId = null;
            if (StringUtils.isNotEmpty(token)) {
                Claims claims = jwtUtils.getClaimByToken(token);
                if (claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())) {
                    userId = Long.parseLong(claims.getSubject());
                }
            }
            bean.setIsCollect(0);
            if (userId != null) {
                CourseCollect courseCollect = courseCollectDao.selectOne(new QueryWrapper<CourseCollect>().eq("classify", 3).eq("user_id", userId).last(" order by update_time desc limit 1 "));
                if (courseCollect != null) {
                    courseDetailsId = courseCollect.getCourseDetailsId();
                }
                bean.setCourseDetailsId(courseDetailsId);
                bean.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("classify", 1).eq("course_id", bean.getCourseId())));
                UserEntity userEntity = userService.selectUserById(userId);
                // 查询用户是否购买了整集
                CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);

                // 查询用户的评分
                CourseScore courseScore = courseScoreService.getOne(new QueryWrapper<CourseScore>()
                        .eq("user_id", userId).eq("course_id", bean.getCourseId()));

                bean.setUserScore(courseScore != null ? courseScore.getScore() : null);

                // 直接直接通过redis缓存所有集
                String redisCourseDetailsListName = "selectCourseDetailsList_" + id + "user_id" + userId;
                String redisCourseDetailsList = redisUtils.get(redisCourseDetailsListName);
                List<CourseDetails> courseDetailsList = null;
                if (StringUtils.isEmpty(redisCourseDetailsList)) {
                    courseDetailsList = baseMapper.findByCourseId(id, userId);
                    redisUtils.set(redisCourseDetailsListName, JSONObject.toJSONString(courseDetailsList));
                } else {
                    String s = redisUtils.get(redisCourseDetailsListName);
                    courseDetailsList = JSON.parseArray(s, CourseDetails.class);
                }

                String redisCourseDetailsNoUrlListName = "selectCourseDetailsNoUrlList_" + id + "user_id" + userId;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if (StringUtils.isEmpty(redisCourseDetailsNoUrlList)) {
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName, JSONObject.toJSONString(courseDetailsNotList));
                } else {
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }

                String value = commonInfoService.findOne(887).getValue();
                // 判断角色 1是梵会员  2是剧达人  3剧荐官   4推荐人
                String[] split = value.split(",");
                boolean flag = false;

                for (String member : split) {
                    if ("1".equals(member)) {
                        if (userEntity.getAgencyIndex() != null && userEntity.getAgencyIndex() == 1) {
                            flag = true;
                        }
                    } else if ("2".equals(member)) {
                        if (userEntity.getAgencyIndex() != null && userEntity.getAgencyIndex() == 2) {
                            flag = true;
                        }
                    } else if ("3".equals(member)) {
                        if (userEntity.getIsChannel() != null && userEntity.getIsChannel() == 1) {
                            flag = true;
                        }
                    } else if ("4".equals(member)) {
                        if (userEntity.getIsRecommend() != null && userEntity.getIsRecommend() == 1) {
                            flag = true;
                        }
                    }
                }

                if (courseUser != null || (flag)) {
                    // 用户购买了整集
                    bean.setListsDetail(courseDetailsList);
                } else {
                    bean.setListsDetail(courseDetailsNotList);
                    // 查询用户是否单独购买了集
                    List<CourseUser> courseUsers = courseUserDao.selectCourseUserList(id, userId);
                    if (courseUsers.size() > 0) {
                        for (CourseUser courseUser1 : courseUsers) {
                            for (CourseDetails courseDetails : bean.getListsDetail()) {
                                if (courseUser1.getCourseDetailsId().equals(courseDetails.getCourseDetailsId())) {
                                    CourseDetails courseDetails1 = baseMapper.selectById(courseDetails.getCourseDetailsId());
                                    courseDetails.setVideoUrl(courseDetails1.getVideoUrl());
                                }
                            }
                        }
                    }
                }
            } else {
                String redisCourseDetailsNoUrlListName = "selectCourseDetailsNoUrlList_" + id;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if (StringUtils.isEmpty(redisCourseDetailsNoUrlList)) {
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName, JSONObject.toJSONString(courseDetailsNotList));
                } else {
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }
                bean.setListsDetail(courseDetailsNotList);
            }
            if (courseDetailsId != null) {
                if (bean.getListsDetail().size() > 0) {
                    for (CourseDetails courseDetails : bean.getListsDetail()) {
                        if (courseDetails.getCourseDetailsId().equals(courseDetailsId) && StringUtils.isNotEmpty(courseDetails.getWxCourseDetailsId())) {
                            // 微信内
                            String url = "https://api.weixin.qq.com/wxa/sec/vod/getmedialink?access_token=" + SenInfoCheckUtil.getMpToken();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("media_id", courseDetails.getWxCourseDetailsId());
                            jsonObject.put("t", (System.currentTimeMillis() / 1000) + 7200);
                            String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
                            JSONObject jsonObject1 = JSONObject.parseObject(s);
                            String errcode = jsonObject1.getString("errcode");
                            if (!"0".equals(errcode)) {
                                return Result.error("获取微信播放链接失败：" + jsonObject1.getString("errmsg"));
                            }
                            JSONObject media_info = jsonObject1.getJSONObject("media_info");
                            String mp4_url = media_info.getString("mp4_url");
                            courseDetails.setWxUrl(mp4_url);
                            break;
                        }
                    }
                }
            }
            return Result.success().put("data", bean);
        }

    }


    @Override
    public Result selectCourseDetailsList(Integer page, Integer limit, String token, String randomNum) {
        Long userId = null;
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = jwtUtils.getClaimByToken(token);
            if (claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())) {
                userId = Long.parseLong(claims.getSubject());
            }
        }
        IPage<CourseDetails> courseDetailsIPage = baseMapper.selectCourseDetailsList(new Page<>(page, limit), randomNum);

        if (userId != null) {
            List<CourseDetails> records = courseDetailsIPage.getRecords();
            for (CourseDetails courseDetails : records) {
                courseDetails.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("course_details_id", courseDetails.getCourseDetailsId()).eq("classify", 1)));
                courseDetails.setIsGood(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("course_details_id", courseDetails.getCourseDetailsId()).eq("classify", 2)));
                courseDetails.setCourse(courseDao.selectById(courseDetails.getCourseId()));
                courseDetails.setTitle(courseDetails.getCourse().getTitle());
                courseDetails.setCourseDetailsCount(baseMapper.selectCount(new QueryWrapper<CourseDetails>().eq("course_id", courseDetails.getCourseId())));
            }
        }
        return Result.success().put("data", new PageUtils(courseDetailsIPage));
    }

    @Override
    public Result courseDetailsListExcelIn(MultipartFile file, Long courseId) throws IOException {
        Course course = courseDao.selectById(courseId);
        if (course == null) {
            return Result.error("所选剧不存在");
        }
        List<CourseDetailsIn> courseDetailsList = ExcelUtils.importExcel(file, 2, 1, CourseDetailsIn.class);
        if (CollectionUtils.isEmpty(courseDetailsList)) {
            return Result.error("Excel数据为空，excel转化失败！");
        }
        // 当前行索引（Excel的数据从第几行开始,就填写几）
        int index = 4;
        // 失败条数
        int repeat = 0;
        // 成功条数
        int successIndex = 0;
        // 空数据
        int emptyCount = 0;
        for (CourseDetailsIn courseDetailsIn : courseDetailsList) {
            if (courseDetailsIn.getCourseDetailsName() == null) {
                emptyCount++;
                continue;
            }
            if (courseDetailsIn.getIsPrice() != 2) {
                if (courseDetailsIn.getPrice() == null || courseDetailsIn.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    return Result.error("第【" + index + "】行数据为收费,但并没有设置价格");
                }
            }
            index++;
            Integer count = baseMapper.selectCount(new QueryWrapper<CourseDetails>().eq("course_details_name", courseDetailsIn.getCourseDetailsName()).eq("course_id", courseId));
            if (count <= 0) {
                CourseDetails courseDetails = new CourseDetails();
                courseDetails.setCourseId(courseId);
                courseDetails.setCreateTime(DateUtils.format(new Date()));
                BeanUtils.copyProperties(courseDetailsIn, courseDetails);
                int result = baseMapper.insert(courseDetails);
                if (result > 0) {
                    successIndex++;
                } else {
                    repeat++;
                }
            }

        }
        return Result.success("导入成功,共新增【" + successIndex + "】条,失败【" + (courseDetailsList.size() - successIndex - emptyCount) + "】条,其中过滤重复数据【" + repeat + "】条");
    }


}
