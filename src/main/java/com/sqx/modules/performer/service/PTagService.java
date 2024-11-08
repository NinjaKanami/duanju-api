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

    /**
     * 获取所有用户可以看到的演员类型，根据pageIndex升序排序
     *
     * @return 演员类型集合
     */
    List<PTag> getAllVisiblePTagsOrderByPageIndex();

    /**
     * 删除演员类型, 如果该类型下有演员，则无法删除
     *
     * @param id 演员类型id
     * @return 是否删除成功
     */
    boolean deletePTagIfNoRelation(Long id);
}
