package com.sqx.modules.performer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.performer.entity.PTag;

import java.util.List;

/**
 * (PTag)表服务接口
 *
 * @author bootun
 * @since 2024-11-06
 */
public interface PTagService extends IService<PTag> {
    /**
     * 获取所有的演员类型(标签)
     *
     * @return 演员类型集合
     */
    List<PTag> getAllPTags();

}
