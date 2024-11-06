package com.sqx.modules.performer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.performer.dao.PTagDao;
import com.sqx.modules.performer.dao.PerformerDao;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.performer.service.PerformerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    private PTagDao ptagDao;

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
    public List<Performer> selectPerformers(Integer page, Integer limit, String name, Integer sex, String company, Integer tag, String sort) {
        if (limit == null) {
            limit = 10;
        }
        if (page == null) {
            page = 1;
        }
        int offset = (page - 1) * limit;

        return performerDao.selectPerformersWithCondition(offset, limit, name, sex, company, tag, sort);
    }


    @Transactional
    public void createPerformer(Performer performer) {
        // 1. 插入 performer 表
        performerDao.insertPerformer(performer);

        // 2. 插入 performer_ptag 表
        if (performer.getTags() != null) {
            ptagDao.insertPerformerTags(performer.getId(), performer.getTags());
        }
    }

    @Transactional
    public int updatePerformer(Performer performer) {
        // 1. 更新 performer 表中的信息
        int rowsAffected = performerDao.updatePerformer(performer);

        // 2. 删除 performer_ptag 表中的旧标签关系
        ptagDao.deleteByPerformerId(performer.getId());

        // 3. 插入新的标签关系（如果有标签）
        if (performer.getTags() != null && !performer.getTags().isEmpty()) {
            ptagDao.insertPerformerTags(performer.getId(), performer.getTags());
        }

        return rowsAffected;
    }

    @Transactional
    public int deletePerformer(Long performerId) {
        int rowsAffected = ptagDao.deleteByPerformerId(performerId);
        performerDao.deletePerformer(performerId);
        return rowsAffected;
    }

}
