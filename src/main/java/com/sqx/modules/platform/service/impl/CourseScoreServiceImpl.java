package com.sqx.modules.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.platform.dao.CourseScoreDao;
import com.sqx.modules.platform.entity.CourseScore;
import com.sqx.modules.platform.service.CourseScoreService;
import org.springframework.stereotype.Service;

/**
 * (CourseScore)表服务实现类
 *
 * @author makejava
 * @since 2024-11-07 18:57:42
 */
@Service("courseScoreService")
public class CourseScoreServiceImpl extends ServiceImpl<CourseScoreDao, CourseScore> implements CourseScoreService {

}
