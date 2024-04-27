package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.course.entity.CommentGood;

public interface CommentGoodService extends IService<CommentGood> {

    CommentGood selectCommentGoodByCommentIdAndUserId(Long commentId,Long userId);

    int deleteCommentGoodById(Long id);

    int insertCommentGood(CommentGood commentGood);


}
