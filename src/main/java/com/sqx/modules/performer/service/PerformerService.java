package com.sqx.modules.performer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.performer.entity.Performer;

import java.util.List;

/**
 * (Performer)表服务接口
 *
 * @author bootun
 * @since 2024-11-06
 */
public interface PerformerService extends IService<Performer> {

    List<Performer> selectPerformers(Integer page, Integer limit, String name, Integer sex, String company, Integer tag, String sort);

    void createPerformer(Performer performer);

    int updatePerformer(Performer performer);

    int deletePerformer(Long performerId);

    /**
     * 用户关注演员
     *
     * @param userId      用户Id
     * @param performerId 演员Id
     * @return 是否关注成功
     */
    boolean userFollowPerformer(Long userId, Long performerId);


    /**
     * 用户取消关注演员
     *
     * @param userId      用户Id
     * @param performerId 演员Id
     * @return 是否取消成功
     **/
    boolean userUnfollowPerformer(Long userId, Long performerId);

    /**
     * 查询用户关注的演员列表
     *
     * @param userId 用户Id
     * @return 演员列表
     */
    List<Performer> userFollowPerformersList(Long userId);
}
