package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.course.dao.CourseClassificationDao;
import com.sqx.modules.course.dao.CourseDetailsDao;
import com.sqx.modules.course.dao.CourseUserDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseUser;
import com.sqx.modules.course.service.CourseUserService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CourseUserServiceImpl extends ServiceImpl<CourseUserDao, CourseUser> implements CourseUserService {
    @Autowired
    private CourseClassificationDao courseClassificationDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseDetailsDao courseDetailsDao;

    @Override
    public void updateTime(Long courseId) {
        QueryWrapper<CourseUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        CourseUser bean = baseMapper.selectOne(queryWrapper);
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bean.setUpdateTime(df1.format(new Date()));
        UpdateWrapper<CourseUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("course_id", courseId);
        baseMapper.update(bean, updateWrapper);
    }

    @Override
    public Result selectCourseUser(Integer page, Integer limit, Long userId) {
        Page<Course> courseUserPage = new Page<>(page, limit);
        IPage<Course> iPage = baseMapper.selectCourseByCourseUser(courseUserPage, userId);
        List<Course> courses = iPage.getRecords();
        UserEntity userEntity = userService.selectUserById(userId);
        if (courses != null && courses.size() > 0) {
            for (Course course : courses) {
                //查询用户是否购买了整集
                CourseUser courseUser = baseMapper.selectCourseUser(course.getCourseId(), userId);
                if (courseUser != null || (userEntity.getMember()!=null && userEntity.getMember()==2)) {
                    course.setListsDetail(courseDetailsDao.findByCourseId(course.getCourseId(),userId));
                }else{
                    course.setListsDetail(courseDetailsDao.findByUserCourseId(course.getCourseId(),userId));
                }
            }
        }
        return Result.success().put("data", new PageUtils(iPage));
    }

    @Override
    public Result selectLatelyCourse(Integer page, Integer limit, Long userId) {
        Page<Course> pages = new Page<>(page, limit);
        IPage<Course> iPage = baseMapper.selectLatelyCourse(pages, userId);
        List<Course> courses = iPage.getRecords();
        if (courses != null && courses.size() > 0) {
            for (Course course : courses) {
                Orders orders = ordersService.selectOrdersByCourseIdAndUserId(userId, course.getCourseId());
                if(orders!=null){
                    course.setOrders(orders);
                }
                course.setCourseClassification(courseClassificationDao.selectById(course.getClassifyId()));
            }
        }
        return Result.success().put("data", new PageUtils(iPage));
    }


    @Override
    public Result insertCourseUser(CourseUser courseUser) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        courseUser.setCreateTime(df.format(new Date()));
        baseMapper.insert(courseUser);
        return Result.success();
    }


}
