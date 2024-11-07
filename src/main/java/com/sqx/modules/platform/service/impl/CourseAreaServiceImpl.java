package com.sqx.modules.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.platform.dao.CourseAreaDao;
import com.sqx.modules.platform.entity.CourseArea;
import com.sqx.modules.platform.service.CourseAreaService;
import org.springframework.stereotype.Service;

/**
 * (CourseArea)表服务实现类
 *
 * @author makejava
 * @since 2024-11-07 18:57:41
 */
@Service("courseAreaService")
public class CourseAreaServiceImpl extends ServiceImpl<CourseAreaDao, CourseArea> implements CourseAreaService {

}
