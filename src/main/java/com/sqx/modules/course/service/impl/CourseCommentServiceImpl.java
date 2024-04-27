package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.dao.CourseCommentDao;
import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.entity.CommentGood;
import com.sqx.modules.course.entity.CourseComment;
import com.sqx.modules.course.service.CommentGoodService;
import com.sqx.modules.course.service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class CourseCommentServiceImpl extends ServiceImpl<CourseCommentDao, CourseComment> implements CourseCommentService {

    @Autowired
    private CommentGoodService commentGoodService;
    @Autowired
    private CourseDao courseDao;

    @Override
    public Result insertCourseComment(CourseComment courseComment) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        courseComment.setCreateTime(df.format(new Date()));
        baseMapper.insert(courseComment);
        return Result.success("操作成功！");
    }

    @Override
    public Result updateGoodsNum(Long courseCommentId, Long userId) {
        //先判断自己有没有点过赞
        CommentGood commentGood = commentGoodService.selectCommentGoodByCommentIdAndUserId(courseCommentId, userId);
        if (commentGood != null) {
            //取消点赞
            commentGoodService.deleteCommentGoodById(commentGood.getCommentGoodId());
            //取消点赞的标识
            int type = 0;
            //取消点赞，评论点赞数量减一
            baseMapper.updateCourseComment(type, courseCommentId);

        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            commentGood = new CommentGood();
            commentGood.setCourseCommentId(courseCommentId);
            commentGood.setUserId(userId);
            commentGood.setCreateTime(df.format(new Date()));
            commentGoodService.insertCommentGood(commentGood);
            //点赞标识
            int type = 1;
            //点赞成功 评论点赞数量加一
            baseMapper.updateCourseComment(type, courseCommentId);

        }
        return Result.success("操作成功！");
    }

    @Override
    public Result selectCourseComment(Integer page, Integer limit, Long courseId,Long userId) {
        Page<CourseComment> courseCommentPage = new Page<>(page, limit);
        return Result.success().put("data", new PageUtils(baseMapper.selectCourseComment(courseCommentPage, courseId,userId)));
    }

    @Override
    public Result deleteCourseComment(Long courseCommentId) {
        //删除评论
        baseMapper.deleteById(courseCommentId);
        //删除评论与点赞的关联
        baseMapper.deleteCommentGood(courseCommentId);
        return Result.success("操作成功！");
    }

    @Override
    public Result selectCourseCommentUser(Integer page, Integer limit,Long userId) {
        Page<Map<String,Object>> pages=new Page(page,limit);
        return Result.success().put("data",new PageUtils(baseMapper.selectCourseCommentByUserId(pages,userId)));
    }
}
