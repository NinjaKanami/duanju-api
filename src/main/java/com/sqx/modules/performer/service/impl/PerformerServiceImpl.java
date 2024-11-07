package com.sqx.modules.performer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.performer.dao.PTagDao;
import com.sqx.modules.performer.dao.PerformerDao;
import com.sqx.modules.performer.dao.PerformerPTagDao;
import com.sqx.modules.performer.dao.PerformerUserDao;
import com.sqx.modules.performer.entity.PTag;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.performer.entity.PerformerPTag;
import com.sqx.modules.performer.entity.PerformerUser;
import com.sqx.modules.performer.service.PerformerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


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
        performerDao.insert(performer);

        // 2. 插入 performer_ptag 表
        if (performer.getTags() != null) {
            ptagDao.insertPerformerTags(performer.getId(), Arrays.asList(performer.getTags().split(",")));
        }
    }

    @Transactional
    public int updatePerformer(Performer performer) {
        // 1. 更新 performer 表中的信息
        int rowsAffected = performerDao.update(performer, new QueryWrapper<Performer>().eq("id", performer.getId()));
        if (rowsAffected < 1) {
            return 0;
        }

        // 2. 删除 performer_ptag 表中的旧标签关系
        performerPTagDao.delete(new QueryWrapper<PerformerPTag>().eq("performer_id", performer.getId()));

        // 3. 插入新的标签关系（如果有标签）
        if (performer.getTags() != null && !performer.getTags().isEmpty()) {
            ptagDao.insertPerformerTags(performer.getId(), Arrays.asList(performer.getTags().split(",")));
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

}
