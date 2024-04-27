package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.course.dao.CommentGoodDao;
import com.sqx.modules.course.entity.CommentGood;
import com.sqx.modules.course.service.CommentGoodService;
import org.springframework.stereotype.Service;

@Service
public class CommentGoodServiceImpl extends ServiceImpl<CommentGoodDao, CommentGood> implements CommentGoodService {

    @Override
    public CommentGood selectCommentGoodByCommentIdAndUserId(Long commentId,Long userId){
        return baseMapper.selectOne(new QueryWrapper<CommentGood>().eq("course_comment_id",commentId).eq("user_id",userId));
    }

    @Override
    public int deleteCommentGoodById(Long id){
        return baseMapper.deleteById(id);
    }

    @Override
    public int insertCommentGood(CommentGood commentGood){
        return baseMapper.insert(commentGood);
    }



}
