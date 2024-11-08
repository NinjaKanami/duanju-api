package com.sqx.modules.performer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.member.entity.Member;
import com.sqx.modules.performer.dao.PTagDao;
import com.sqx.modules.performer.entity.PTag;
import com.sqx.modules.performer.service.PTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * (PTag)表服务实现类
 *
 * @author bootun
 * @since 2024-11-06
 */
@Slf4j
@Service("ptagService")
public class PTagServiceImpl extends ServiceImpl<PTagDao, PTag> implements PTagService {

    @Override
    public List<PTag> getAllPTags() {
        return baseMapper.selectList(new QueryWrapper<PTag>().orderByDesc("id"));
    }

    @Override
    public List<PTag> getAllVisiblePTagsOrderByPageIndex() {
        return baseMapper.selectList(
                new QueryWrapper<PTag>().
                        eq("is_visible", 1).
                        orderByAsc("page_index")
        );
    }

    @Override
    public boolean deletePTagIfNoRelation(Long id) {
        return baseMapper.deletePerformerTagsIfNoRelation(id) > 0;
    }
}
