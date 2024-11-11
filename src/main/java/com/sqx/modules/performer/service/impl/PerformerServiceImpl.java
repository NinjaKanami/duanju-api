package com.sqx.modules.performer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.message.constant.MessageConstant;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.performer.dao.PTagDao;
import com.sqx.modules.performer.dao.PerformerDao;
import com.sqx.modules.performer.dao.PerformerPTagDao;
import com.sqx.modules.performer.dao.PerformerUserDao;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.performer.entity.PerformerPTag;
import com.sqx.modules.performer.entity.PerformerUser;
import com.sqx.modules.performer.service.PerformerService;
import com.sqx.modules.performer.vo.AppPerformerVO;
import com.sqx.modules.platform.dao.CoursePerformerDao;
import com.sqx.modules.platform.entity.CoursePerformer;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * (PTag)表服务实现类
 *
 * @author bootun
 * @since 2024-11-06
 */
@Slf4j
@Service("performerService")
public class PerformerServiceImpl extends ServiceImpl<PerformerDao, Performer> implements PerformerService {

    @Autowired
    private PerformerDao performerDao;
    @Autowired
    private PerformerUserDao performerUserDao;
    @Autowired
    private PTagDao ptagDao;
    @Autowired
    private PerformerPTagDao performerPTagDao;
    @Autowired
    private CoursePerformerDao coursePerformerDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CourseService courseService;

    /**
     * 查询演员列表
     *
     * @param page    页数
     * @param limit   每页数量
     * @param name    姓名模糊查询
     * @param sex     性别搜索
     * @param company 公司模糊搜索
     * @param tag     演员类型搜索
     * @param sort    排序
     * @return List<Performer>
     */
    @Override
    public Page<Performer> selectPerformers(Integer page, Integer limit, String name, Integer sex, String company, Integer tag, String sort) {
        page = page == null ? 1 : page;
        limit = limit == null ? 10 : limit;
        Page<Performer> pages = new Page<>(page, limit);

        sort = sort == null ? null : Strings.toUpperCase(sort);
        return performerDao.selectPerformersWithCondition(pages, name, sex, company, tag, sort);
    }


    @Transactional
    public void createPerformer(Performer performer) {
        // 1. 插入 performer 表
        performerDao.insert(performer);

        // 2. 插入 performer_ptag 表
        if (performer.getTagsStr() != null) {
            ptagDao.insertPerformerTags(performer.getId(), Arrays.asList(performer.getTagsStr().split(",")));
        }
        // 3.插入 course_performer 表
        if (performer.getCourseStr() != null) {
            String[] courseIds = performer.getCourseStr().split(",");
            List<CoursePerformer> cps = new ArrayList<>();
            for (String courseId : courseIds) {
                cps.add(new CoursePerformer(Long.parseLong(courseId), performer.getId()));
            }
            coursePerformerDao.insertBatch(cps);
        }
    }

    @Transactional
    public int updatePerformer(Performer performer) {
        // 先查询下当前演员关联的视频集合，如果上了新剧，给用户批量发送通知
        List<Course> courses = coursePerformerDao.selectCourseListByPerformerId(performer.getId(), null, null);
        List<String> oldCourseIds = new ArrayList<>();
        for (Course course : courses) {
            oldCourseIds.add(course.getCourseId().toString());
        }
        // 管理员设置的新关联列表
        String[] newList = performer.getCourseStr().split(",");
        // 新加的短剧
        List<String> newCourseIds = new ArrayList<>();
        for (String courseId : newList) {
            // 如果新的关联列表里没有，说明这部剧是新加的，要记录下来，给用户发送通知
            if (!oldCourseIds.contains(courseId)) {
                newCourseIds.add(courseId);
            }
        }


        // 1. 更新 performer 表中的信息
        int rowsAffected = performerDao.update(performer, new QueryWrapper<Performer>().eq("id", performer.getId()));
        if (rowsAffected < 1) {
            return 0;
        }

        // 2.演员类型(标签)的处理
        {
            // 2.1 删除 performer_ptag 表中的旧标签关系
            performerPTagDao.delete(new QueryWrapper<PerformerPTag>().eq("performer_id", performer.getId()));

            // 2.2 插入新的标签关系（如果有标签）
            if (performer.getTagsStr() != null && !performer.getTagsStr().isEmpty()) {
                ptagDao.insertPerformerTags(performer.getId(), Arrays.asList(performer.getTagsStr().split(",")));
            }
        }

        // 3.演员关联短剧的处理
        {
            // 3.1 删除 course_performer 表中的旧关联关系
            coursePerformerDao.delete(new QueryWrapper<CoursePerformer>().eq("performer_id", performer.getId()));
            // 3.2 插入新的关联关系（如果有关联的短剧）
            if (performer.getCourseStr() != null && !performer.getCourseStr().isEmpty()) {
                String[] courseIds = performer.getCourseStr().split(",");
                List<CoursePerformer> cps = new ArrayList<>();
                for (String courseId : courseIds) {
                    cps.add(new CoursePerformer(Long.parseLong(courseId), performer.getId()));
                }
                coursePerformerDao.insertBatch(cps);
            }
        }

        return rowsAffected;
    }

    @Transactional
    public int deletePerformer(Long performerId) {
        int rowsAffected = performerDao.deleteById(performerId);
        performerPTagDao.delete(new QueryWrapper<PerformerPTag>().eq("performer_id", performerId));
        return rowsAffected;
    }

    @Override
    public boolean userFollowPerformer(Long userId, Long performerId) {
        // 1. 检查是否已关注
        if (performerUserDao.selectCount(
                new QueryWrapper<PerformerUser>().
                        eq("performer_id", performerId).
                        eq("user_id", userId)
        ) > 0) {
            return false; // 已关注，返回 false
        }

        // 2. 插入关注记录
        return performerUserDao.insert(new PerformerUser(performerId, userId)) > 0;
    }

    @Override
    public boolean userUnfollowPerformer(Long userId, Long performerId) {
        return performerUserDao.delete(
                new QueryWrapper<PerformerUser>().
                        eq("performer_id", performerId).
                        eq("user_id", userId)
        ) > 0;
    }

    @Override
    public List<Performer> userFollowPerformersList(Long userId) {
        return performerDao.selectUserFollowPerformerList(userId);
    }

    @Override
    public List<Performer> selectPerformerRankOrderByFollower(Long ptagId, Integer sex, String sort) {
        sort = sort == null ? "DESC" : Strings.toUpperCase(sort);
        return performerDao.selectPerformerRankOrderByFollower(ptagId, sex, sort);
    }

    @Override
    public AppPerformerVO userGetPerformerDetail(Long userId, Long performerId, Long wxShow) {
        // 1.查询演员详细信息
        Performer performer = performerDao.selectById(performerId);

        // 2.查询用户是否已关注该演员
        boolean isFollowed = performerUserDao.selectCount(
                new QueryWrapper<PerformerUser>().
                        eq("performer_id", performerId).
                        eq("user_id", userId)
        ) > 0;

        // 3.组合返回
        AppPerformerVO res = new AppPerformerVO(performer);
        if (isFollowed) {
            res.setIsFollowed(true);
        }

        /*
        int isCollect = courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                                .eq("classify", 1).eq("course_id", courseId).eq("user_id", userId));
                        map.put("isCollect", isCollect);
        * */
        // 查询演员参演的短剧并组装
        List<Course> courses = coursePerformerDao.selectCourseListByPerformerId(performerId, wxShow, userId);
        if (courses != null) {
            res.setCourseList(courses);
        }
        return res;
    }

    @Override
    public List<AppPerformerVO> userSearchPerformer(String name) {
        List<Performer> performers = performerDao.selectList(new QueryWrapper<Performer>().like("name", name));
        return AppPerformerVO.fromEntityList(performers);
    }

    @Override
    public int pushPerformerUpdateMessageToFollower(Long performerId, MessageBuilder messageBuilder) {
        // 订阅列表
        List<PerformerUser> users = performerUserDao.selectList(new QueryWrapper<PerformerUser>().eq("performer_id", performerId));

        // 演员信息
        Performer performer = performerDao.selectById(performerId);
        List<MessageInfo> messages = new ArrayList<>(users.size());
        for (PerformerUser user : users) {
            MessageInfo msg = new MessageInfo();
            // 发送时间
            String now = DateUtils.format(new Date());
            msg.setSendTime(now);
            msg.setCreateAt(now);
            // 消息接收人
            msg.setUserId(String.valueOf(user.getUser_id()));
            // 消息内容
            String msgContent = messageBuilder.messageBuildMethod(performer.getName());
            msg.setContent(msgContent);
            // 消息标题
            msg.setTitle("你关注的演员有更新");
            // 消息类型
            msg.setState(String.valueOf(MessageConstant.StateUser));
            // 未读
            msg.setIsSee(String.valueOf(MessageConstant.IsSeeNo));
            messages.add(msg);
        }
        return messageService.batchSaveBodyWithTx(messages);
    }

    @Override
    public int pushPerformerMessageToFollowerByCourse(Long courseId, MessageBuilder messageBuilder) {
        // TODO: 产品需求暂无该需求，可以参考 pushPerformerUpdateMessageToFollower 方法进行实现
        return 0;
    }

}

