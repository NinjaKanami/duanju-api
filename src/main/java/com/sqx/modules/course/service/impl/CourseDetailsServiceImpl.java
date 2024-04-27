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
import com.sqx.modules.utils.EasyPoi.ExcelUtils;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CourseDetailsServiceImpl  extends ServiceImpl<CourseDetailsDao, CourseDetails> implements CourseDetailsService {

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


    @Override
    public Result insert(CourseDetails courseDetails) {
        if(courseDetails.getGoodNum()==null){
            courseDetails.setGoodNum(0);
        }
        baseMapper.insert(courseDetails);
        redisUtils.flushdb();
        return Result.success();
    }

    @Override
    public Result updateCourseDetails(CourseDetails courseDetails) {
        baseMapper.updateById(courseDetails);
        redisUtils.flushdb();
        return Result.success();
    }

    @Override
    public Result deleteCourseDetails(String ids) {
        String[] split = ids.split(",");
        baseMapper.deleteCourseDetails(split);
        redisUtils.flushdb();
        return Result.success();
    }

    @Override
    public Result selectCourseDetailsById(Long id,String token,String courseDetailsId){
        String redisCourseDetailsName="selectCourseDetailsById_"+id;
        String s1 = redisUtils.get(redisCourseDetailsName);
        if(StringUtils.isEmpty(s1)){
            Course bean = courseDao.selectById(id);
            /*
            if(bean.getViewCounts()==null){
                bean.setViewCounts(1);
            }else{
                bean.setViewCounts(bean.getViewCounts()+1);
            }
            courseDao.updateById(bean);*/
            Long userId=null;
            if(StringUtils.isNotEmpty(token)){
                Claims claims = jwtUtils.getClaimByToken(token);
                if(claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())){
                    userId=Long.parseLong(claims.getSubject());
                }
            }
            bean.setIsCollect(0);
            if (userId != null) {
                bean.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id",userId).eq("classify",1).eq("course_id",bean.getCourseId())));
                UserEntity userEntity = userService.selectUserById(userId);
                //查询用户是否购买了整集
                CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);

                //直接直接通过redis缓存所有集
                String redisCourseDetailsListName="selectCourseDetailsList_"+id+"user_id"+userId;
                String redisCourseDetailsList = redisUtils.get(redisCourseDetailsListName);
                List<CourseDetails> courseDetailsList = null;
                if(StringUtils.isEmpty(redisCourseDetailsList)){
                    courseDetailsList = baseMapper.findByCourseId(id, userId);
                    redisUtils.set(redisCourseDetailsListName,JSONObject.toJSONString(courseDetailsList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsListName);
                    courseDetailsList = JSON.parseArray(s, CourseDetails.class);
                }

                String redisCourseDetailsNoUrlListName="selectCourseDetailsNoUrlList_"+id+"user_id"+userId;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if(StringUtils.isEmpty(redisCourseDetailsNoUrlList)){
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName,JSONObject.toJSONString(courseDetailsNotList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }

                String value = commonInfoService.findOne(887).getValue();
                //判断角色 1是梵会员  2是剧达人  3剧荐官   4推荐人
                String[] split = value.split(",");
                boolean flag=false;

                for (String member:split){
                    if("1".equals(member)){
                        if(userEntity.getAgencyIndex()!=null && userEntity.getAgencyIndex()==1){
                            flag=true;
                        }
                    }else if("2".equals(member)){
                        if(userEntity.getAgencyIndex()!=null && userEntity.getAgencyIndex()==2){
                            flag=true;
                        }
                    }else if("3".equals(member)){
                        if(userEntity.getIsChannel()!=null && userEntity.getIsChannel()==1){
                            flag=true;
                        }
                    }else if("4".equals(member)){
                        if(userEntity.getIsRecommend()!=null && userEntity.getIsRecommend()==1){
                            flag=true;
                        }
                    }
                }

                if (courseUser != null || (flag)) {
                    //用户购买了整集
                    bean.setListsDetail(courseDetailsList);
                }else{
                    bean.setListsDetail(courseDetailsNotList);
                    //查询用户是否单独购买了集
                    List<CourseUser> courseUsers = courseUserDao.selectCourseUserList(id, userId);
                    if(courseUsers.size()>0){
                        for (CourseUser courseUser1:courseUsers){
                            for (CourseDetails courseDetails:bean.getListsDetail()) {
                                if(courseUser1.getCourseDetailsId().equals(courseDetails.getCourseDetailsId())){
                                    CourseDetails courseDetails1 = baseMapper.selectById(courseDetails.getCourseDetailsId());
                                    courseDetails.setVideoUrl(courseDetails1.getVideoUrl());
                                }
                            }
                        }
                    }
                }
            } else {
                String redisCourseDetailsNoUrlListName="selectCourseDetailsNoUrlList_"+id;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if(StringUtils.isEmpty(redisCourseDetailsNoUrlList)){
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName,JSONObject.toJSONString(courseDetailsNotList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }
                bean.setListsDetail(courseDetailsNotList);
            }
            if(courseDetailsId!=null){
                if (bean.getListsDetail().size()>0){
                    for (CourseDetails courseDetails:bean.getListsDetail()){
                        if(courseDetails.getCourseDetailsId().equals(Long.parseLong(courseDetailsId)) && StringUtils.isNotEmpty(courseDetails.getWxCourseDetailsId())){
                            //微信内
                            String url="https://api.weixin.qq.com/wxa/sec/vod/getmedialink?access_token="+ SenInfoCheckUtil.getMpToken();
                            JSONObject jsonObject=new JSONObject();
                            jsonObject.put("media_id",courseDetails.getWxCourseDetailsId());
                            jsonObject.put("t",(System.currentTimeMillis()/1000)+7200);
                            String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
                            JSONObject jsonObject1 = JSONObject.parseObject(s);
                            String errcode = jsonObject1.getString("errcode");
                            if(!"0".equals(errcode)){
                                return Result.error("获取微信播放链接失败："+jsonObject1.getString("errmsg"));
                            }
                            JSONObject media_info = jsonObject1.getJSONObject("media_info");
                            String mp4_url = media_info.getString("mp4_url");
                            courseDetails.setWxUrl(mp4_url);
                            break;
                        }
                    }
                }
            }
            redisUtils.set(redisCourseDetailsName,bean);
            return Result.success().put("data",bean);
        }else{
            String ss = redisUtils.get(redisCourseDetailsName);
            Course bean = JSONObject.parseObject(ss, Course.class);
            Long userId=null;
            if(StringUtils.isNotEmpty(token)){
                Claims claims = jwtUtils.getClaimByToken(token);
                if(claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())){
                    userId=Long.parseLong(claims.getSubject());
                }
            }
            bean.setIsCollect(0);
            if (userId != null) {
                bean.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id",userId).eq("classify",1).eq("course_id",bean.getCourseId())));
                UserEntity userEntity = userService.selectUserById(userId);
                //查询用户是否购买了整集
                CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);

                //直接直接通过redis缓存所有集
                String redisCourseDetailsListName="selectCourseDetailsList_"+id+"user_id"+userId;
                String redisCourseDetailsList = redisUtils.get(redisCourseDetailsListName);
                List<CourseDetails> courseDetailsList = null;
                if(StringUtils.isEmpty(redisCourseDetailsList)){
                    courseDetailsList = baseMapper.findByCourseId(id, userId);
                    redisUtils.set(redisCourseDetailsListName,JSONObject.toJSONString(courseDetailsList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsListName);
                    courseDetailsList = JSON.parseArray(s, CourseDetails.class);
                }

                String redisCourseDetailsNoUrlListName="selectCourseDetailsNoUrlList_"+id+"user_id"+userId;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if(StringUtils.isEmpty(redisCourseDetailsNoUrlList)){
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName,JSONObject.toJSONString(courseDetailsNotList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }

                String value = commonInfoService.findOne(887).getValue();
                //判断角色 1是梵会员  2是剧达人  3剧荐官   4推荐人
                String[] split = value.split(",");
                boolean flag=false;

                for (String member:split){
                    if("1".equals(member)){
                        if(userEntity.getAgencyIndex()!=null && userEntity.getAgencyIndex()==1){
                            flag=true;
                        }
                    }else if("2".equals(member)){
                        if(userEntity.getAgencyIndex()!=null && userEntity.getAgencyIndex()==2){
                            flag=true;
                        }
                    }else if("3".equals(member)){
                        if(userEntity.getIsChannel()!=null && userEntity.getIsChannel()==1){
                            flag=true;
                        }
                    }else if("4".equals(member)){
                        if(userEntity.getIsRecommend()!=null && userEntity.getIsRecommend()==1){
                            flag=true;
                        }
                    }
                }

                if (courseUser != null || (flag)) {
                    //用户购买了整集
                    bean.setListsDetail(courseDetailsList);
                }else{
                    bean.setListsDetail(courseDetailsNotList);
                    //查询用户是否单独购买了集
                    List<CourseUser> courseUsers = courseUserDao.selectCourseUserList(id, userId);
                    if(courseUsers.size()>0){
                        for (CourseUser courseUser1:courseUsers){
                            for (CourseDetails courseDetails:bean.getListsDetail()) {
                                if(courseUser1.getCourseDetailsId().equals(courseDetails.getCourseDetailsId())){
                                    CourseDetails courseDetails1 = baseMapper.selectById(courseDetails.getCourseDetailsId());
                                    courseDetails.setVideoUrl(courseDetails1.getVideoUrl());
                                }
                            }
                        }
                    }
                }
            } else {
                String redisCourseDetailsNoUrlListName="selectCourseDetailsNoUrlList_"+id;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if(StringUtils.isEmpty(redisCourseDetailsNoUrlList)){
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName,JSONObject.toJSONString(courseDetailsNotList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }
                bean.setListsDetail(courseDetailsNotList);
            }
            if(courseDetailsId!=null){
                if (bean.getListsDetail().size()>0){
                    for (CourseDetails courseDetails:bean.getListsDetail()){
                        if(courseDetails.getCourseDetailsId().equals(Long.parseLong(courseDetailsId)) && StringUtils.isNotEmpty(courseDetails.getWxCourseDetailsId())){
                            //微信内
                            String url="https://api.weixin.qq.com/wxa/sec/vod/getmedialink?access_token="+ SenInfoCheckUtil.getMpToken();
                            JSONObject jsonObject=new JSONObject();
                            jsonObject.put("media_id",courseDetails.getWxCourseDetailsId());
                            jsonObject.put("t",(System.currentTimeMillis()/1000)+7200);
                            String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
                            JSONObject jsonObject1 = JSONObject.parseObject(s);
                            String errcode = jsonObject1.getString("errcode");
                            if(!"0".equals(errcode)){
                                return Result.error("获取微信播放链接失败："+jsonObject1.getString("errmsg"));
                            }
                            JSONObject media_info = jsonObject1.getJSONObject("media_info");
                            String mp4_url = media_info.getString("mp4_url");
                            courseDetails.setWxUrl(mp4_url);
                            break;
                        }
                    }
                }
            }
            return Result.success().put("data",bean);
        }

    }

    @Override
    public Result selectCourseDetailsByTitle(String token,String title){
        Course course = courseDao.selectOne(new QueryWrapper<Course>().eq("title",title).eq("is_delete",0));
        if(course==null){
            return Result.success();
        }
        Long courseDetailsId=null;
        Long id=course.getCourseId();
        String redisCourseDetailsName="selectCourseDetailsById_"+id;
        String s1 = redisUtils.get(redisCourseDetailsName);
        if(StringUtils.isEmpty(s1)){
            Course bean = courseDao.selectById(id);
            Long userId=null;
            if(StringUtils.isNotEmpty(token)){
                Claims claims = jwtUtils.getClaimByToken(token);
                if(claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())){
                    userId=Long.parseLong(claims.getSubject());
                }
            }
            bean.setIsCollect(0);
            if (userId != null) {
                CourseCollect courseCollect = courseCollectDao.selectOne(new QueryWrapper<CourseCollect>().eq("classify", 3).eq("user_id", userId).last(" order by update_time desc limit 1 "));
                if(courseCollect!=null){
                    courseDetailsId=courseCollect.getCourseDetailsId();
                }
                bean.setCourseDetailsId(courseDetailsId);
                bean.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id",userId).eq("classify",1).eq("course_id",bean.getCourseId())));
                UserEntity userEntity = userService.selectUserById(userId);
                //查询用户是否购买了整集
                CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);

                //直接直接通过redis缓存所有集
                String redisCourseDetailsListName="selectCourseDetailsList_"+id+"user_id"+userId;
                String redisCourseDetailsList = redisUtils.get(redisCourseDetailsListName);
                List<CourseDetails> courseDetailsList = null;
                if(StringUtils.isEmpty(redisCourseDetailsList)){
                    courseDetailsList = baseMapper.findByCourseId(id, userId);
                    redisUtils.set(redisCourseDetailsListName,JSONObject.toJSONString(courseDetailsList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsListName);
                    courseDetailsList = JSON.parseArray(s, CourseDetails.class);
                }

                String redisCourseDetailsNoUrlListName="selectCourseDetailsNoUrlList_"+id+"user_id"+userId;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if(StringUtils.isEmpty(redisCourseDetailsNoUrlList)){
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName,JSONObject.toJSONString(courseDetailsNotList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }

                String value = commonInfoService.findOne(887).getValue();
                //判断角色 1是梵会员  2是剧达人  3剧荐官   4推荐人
                String[] split = value.split(",");
                boolean flag=false;

                for (String member:split){
                    if("1".equals(member)){
                        if(userEntity.getAgencyIndex()!=null && userEntity.getAgencyIndex()==1){
                            flag=true;
                        }
                    }else if("2".equals(member)){
                        if(userEntity.getAgencyIndex()!=null && userEntity.getAgencyIndex()==2){
                            flag=true;
                        }
                    }else if("3".equals(member)){
                        if(userEntity.getIsChannel()!=null && userEntity.getIsChannel()==1){
                            flag=true;
                        }
                    }else if("4".equals(member)){
                        if(userEntity.getIsRecommend()!=null && userEntity.getIsRecommend()==1){
                            flag=true;
                        }
                    }
                }

                if (courseUser != null || (flag)) {
                    //用户购买了整集
                    bean.setListsDetail(courseDetailsList);
                }else{
                    bean.setListsDetail(courseDetailsNotList);
                    //查询用户是否单独购买了集
                    List<CourseUser> courseUsers = courseUserDao.selectCourseUserList(id, userId);
                    if(courseUsers.size()>0){
                        for (CourseUser courseUser1:courseUsers){
                            for (CourseDetails courseDetails:bean.getListsDetail()) {
                                if(courseUser1.getCourseDetailsId().equals(courseDetails.getCourseDetailsId())){
                                    CourseDetails courseDetails1 = baseMapper.selectById(courseDetails.getCourseDetailsId());
                                    courseDetails.setVideoUrl(courseDetails1.getVideoUrl());
                                }
                            }
                        }
                    }
                }
            } else {
                String redisCourseDetailsNoUrlListName="selectCourseDetailsNoUrlList_"+id;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if(StringUtils.isEmpty(redisCourseDetailsNoUrlList)){
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName,JSONObject.toJSONString(courseDetailsNotList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }
                bean.setListsDetail(courseDetailsNotList);
            }
            if(courseDetailsId!=null){
                if (bean.getListsDetail().size()>0){
                    for (CourseDetails courseDetails:bean.getListsDetail()){
                        if(courseDetails.getCourseDetailsId().equals(courseDetailsId) && StringUtils.isNotEmpty(courseDetails.getWxCourseDetailsId())){
                            //微信内
                            String url="https://api.weixin.qq.com/wxa/sec/vod/getmedialink?access_token="+ SenInfoCheckUtil.getMpToken();
                            JSONObject jsonObject=new JSONObject();
                            jsonObject.put("media_id",courseDetails.getWxCourseDetailsId());
                            jsonObject.put("t",(System.currentTimeMillis()/1000)+7200);
                            String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
                            JSONObject jsonObject1 = JSONObject.parseObject(s);
                            String errcode = jsonObject1.getString("errcode");
                            if(!"0".equals(errcode)){
                                return Result.error("获取微信播放链接失败："+jsonObject1.getString("errmsg"));
                            }
                            JSONObject media_info = jsonObject1.getJSONObject("media_info");
                            String mp4_url = media_info.getString("mp4_url");
                            courseDetails.setWxUrl(mp4_url);
                            break;
                        }
                    }
                }
            }
            redisUtils.set(redisCourseDetailsName,bean);
            return Result.success().put("data",bean);
        }else{
            String ss = redisUtils.get(redisCourseDetailsName);
            Course bean = JSONObject.parseObject(ss, Course.class);
            Long userId=null;
            if(StringUtils.isNotEmpty(token)){
                Claims claims = jwtUtils.getClaimByToken(token);
                if(claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())){
                    userId=Long.parseLong(claims.getSubject());
                }
            }
            bean.setIsCollect(0);
            if (userId != null) {
                CourseCollect courseCollect = courseCollectDao.selectOne(new QueryWrapper<CourseCollect>().eq("classify", 3).eq("user_id", userId).last(" order by update_time desc limit 1 "));
                if(courseCollect!=null){
                    courseDetailsId=courseCollect.getCourseDetailsId();
                }
                bean.setCourseDetailsId(courseDetailsId);
                bean.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id",userId).eq("classify",1).eq("course_id",bean.getCourseId())));
                UserEntity userEntity = userService.selectUserById(userId);
                //查询用户是否购买了整集
                CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);

                //直接直接通过redis缓存所有集
                String redisCourseDetailsListName="selectCourseDetailsList_"+id+"user_id"+userId;
                String redisCourseDetailsList = redisUtils.get(redisCourseDetailsListName);
                List<CourseDetails> courseDetailsList = null;
                if(StringUtils.isEmpty(redisCourseDetailsList)){
                    courseDetailsList = baseMapper.findByCourseId(id, userId);
                    redisUtils.set(redisCourseDetailsListName,JSONObject.toJSONString(courseDetailsList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsListName);
                    courseDetailsList = JSON.parseArray(s, CourseDetails.class);
                }

                String redisCourseDetailsNoUrlListName="selectCourseDetailsNoUrlList_"+id+"user_id"+userId;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if(StringUtils.isEmpty(redisCourseDetailsNoUrlList)){
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName,JSONObject.toJSONString(courseDetailsNotList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }

                String value = commonInfoService.findOne(887).getValue();
                //判断角色 1是梵会员  2是剧达人  3剧荐官   4推荐人
                String[] split = value.split(",");
                boolean flag=false;

                for (String member:split){
                    if("1".equals(member)){
                        if(userEntity.getAgencyIndex()!=null && userEntity.getAgencyIndex()==1){
                            flag=true;
                        }
                    }else if("2".equals(member)){
                        if(userEntity.getAgencyIndex()!=null && userEntity.getAgencyIndex()==2){
                            flag=true;
                        }
                    }else if("3".equals(member)){
                        if(userEntity.getIsChannel()!=null && userEntity.getIsChannel()==1){
                            flag=true;
                        }
                    }else if("4".equals(member)){
                        if(userEntity.getIsRecommend()!=null && userEntity.getIsRecommend()==1){
                            flag=true;
                        }
                    }
                }

                if (courseUser != null || (flag)) {
                    //用户购买了整集
                    bean.setListsDetail(courseDetailsList);
                }else{
                    bean.setListsDetail(courseDetailsNotList);
                    //查询用户是否单独购买了集
                    List<CourseUser> courseUsers = courseUserDao.selectCourseUserList(id, userId);
                    if(courseUsers.size()>0){
                        for (CourseUser courseUser1:courseUsers){
                            for (CourseDetails courseDetails:bean.getListsDetail()) {
                                if(courseUser1.getCourseDetailsId().equals(courseDetails.getCourseDetailsId())){
                                    CourseDetails courseDetails1 = baseMapper.selectById(courseDetails.getCourseDetailsId());
                                    courseDetails.setVideoUrl(courseDetails1.getVideoUrl());
                                }
                            }
                        }
                    }
                }
            } else {
                String redisCourseDetailsNoUrlListName="selectCourseDetailsNoUrlList_"+id;
                String redisCourseDetailsNoUrlList = redisUtils.get(redisCourseDetailsNoUrlListName);
                List<CourseDetails> courseDetailsNotList = null;
                if(StringUtils.isEmpty(redisCourseDetailsNoUrlList)){
                    courseDetailsNotList = baseMapper.findByCourseIdNotUrl(id, userId);
                    redisUtils.set(redisCourseDetailsNoUrlListName,JSONObject.toJSONString(courseDetailsNotList));
                }else{
                    String s = redisUtils.get(redisCourseDetailsNoUrlListName);
                    courseDetailsNotList = JSON.parseArray(s, CourseDetails.class);
                }
                bean.setListsDetail(courseDetailsNotList);
            }
            if(courseDetailsId!=null){
                if (bean.getListsDetail().size()>0){
                    for (CourseDetails courseDetails:bean.getListsDetail()){
                        if(courseDetails.getCourseDetailsId().equals(courseDetailsId) && StringUtils.isNotEmpty(courseDetails.getWxCourseDetailsId())){
                            //微信内
                            String url="https://api.weixin.qq.com/wxa/sec/vod/getmedialink?access_token="+ SenInfoCheckUtil.getMpToken();
                            JSONObject jsonObject=new JSONObject();
                            jsonObject.put("media_id",courseDetails.getWxCourseDetailsId());
                            jsonObject.put("t",(System.currentTimeMillis()/1000)+7200);
                            String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
                            JSONObject jsonObject1 = JSONObject.parseObject(s);
                            String errcode = jsonObject1.getString("errcode");
                            if(!"0".equals(errcode)){
                                return Result.error("获取微信播放链接失败："+jsonObject1.getString("errmsg"));
                            }
                            JSONObject media_info = jsonObject1.getJSONObject("media_info");
                            String mp4_url = media_info.getString("mp4_url");
                            courseDetails.setWxUrl(mp4_url);
                            break;
                        }
                    }
                }
            }
            return Result.success().put("data",bean);
        }

    }



    @Override
    public Result selectCourseDetailsList(Integer page,Integer limit,String token,String randomNum){
        Long userId=null;
        if(StringUtils.isNotEmpty(token)){
            Claims claims = jwtUtils.getClaimByToken(token);
            if(claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())){
                userId=Long.parseLong(claims.getSubject());
            }
        }
        IPage<CourseDetails> courseDetailsIPage = baseMapper.selectCourseDetailsList(new Page<>(page, limit),randomNum);

        if (userId != null) {
            List<CourseDetails> records = courseDetailsIPage.getRecords();
            for (CourseDetails courseDetails:records){
                courseDetails.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("course_details_id", courseDetails.getCourseDetailsId()).eq("classify",1)));
                courseDetails.setIsGood(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("course_details_id", courseDetails.getCourseDetailsId()).eq("classify",2)));
                courseDetails.setCourse(courseDao.selectById(courseDetails.getCourseId()));
                courseDetails.setTitle(courseDetails.getCourse().getTitle());
                courseDetails.setCourseDetailsCount(baseMapper.selectCount(new QueryWrapper<CourseDetails>().eq("course_id",courseDetails.getCourseId())));
            }
        }
        return Result.success().put("data",new PageUtils(courseDetailsIPage));
    }

    @Override
    public Result courseDetailsListExcelIn(MultipartFile file, Long courseId) throws IOException {
        Course course = courseDao.selectById(courseId);
        if (course==null){
            return Result.error("所选剧不存在");
        }
        List<CourseDetailsIn> courseDetailsList = ExcelUtils.importExcel(file, 2, 1, CourseDetailsIn.class);
        if (CollectionUtils.isEmpty(courseDetailsList)) {
            return Result.error("Excel数据为空，excel转化失败！");
        }
        //当前行索引（Excel的数据从第几行开始,就填写几）
        int index = 4;
        //失败条数
        int repeat = 0;
        //成功条数
        int successIndex = 0;
        //空数据
        int emptyCount=0;
        for (CourseDetailsIn courseDetailsIn : courseDetailsList) {
            if(courseDetailsIn.getCourseDetailsName()==null){
                emptyCount++;
                continue;
            }
            if (courseDetailsIn.getIsPrice()!=2){
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
                int result =  baseMapper.insert(courseDetails);
                if (result > 0) {
                    successIndex++;
                }else {
                    repeat++;
                }
            }

        }
        return Result.success("导入成功,共新增【" + successIndex + "】条,失败【" + (courseDetailsList.size() - successIndex - emptyCount) + "】条,其中过滤重复数据【" + repeat + "】条");
    }


}
