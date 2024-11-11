package com.sqx.modules.performer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.performer.vo.AppPerformerVO;

import java.util.List;

/**
 * (Performer)表服务接口
 *
 * @author bootun
 * @since 2024-11-06
 */
public interface PerformerService extends IService<Performer> {

    Page<Performer> selectPerformers(Integer page, Integer limit, String name, Integer sex, String company, Integer tag, String sort);

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

    /**
     * 根据follow数量排序查询演员列表
     *
     * @param ptagId 演员类型id
     * @param sex    性别,不写默认为查询全部性别
     * @param sort   升序降序(ASC/DESC)，默认降序
     * @return 演员列表
     */
    List<Performer> selectPerformerRankOrderByFollower(Long ptagId, Integer sex, String sort);


    /**
     * 用户查询演员详情
     *
     * @param userId      用户Id
     * @param performerId 演员id
     * @return 演员详情
     */
    AppPerformerVO userGetPerformerDetail(Long userId, Long performerId, Long wxShow);

    /**
     * 用户根据演员名称，模糊查询演员列表
     *
     * @param name 演员名称模糊查询
     * @return 演员列表
     */
    List<AppPerformerVO> userSearchPerformer(String name);

    /**
     * 推送消息给所有订阅演员的用户
     * 该方法内部使用事务实现，操作要么全部成功，要么全部失败
     *
     * @param performerId    演员id
     * @param messageBuilder 消息内容
     * @return 推送的用户数量
     */
    int pushPerformerUpdateMessageToFollower(Long performerId, MessageBuilder messageBuilder);

    /**
     * 以剧为维度，给这部剧所有参演演员的粉丝推送更新消息
     * @param courseId 短剧id
     * @param messageBuilder 消息内容
     * @return
     */
    int pushPerformerMessageToFollowerByCourse(Long courseId, MessageBuilder messageBuilder);

    @FunctionalInterface
    interface MessageBuilder {
        /**
         * 消息构建方法
         *
         * @param performerName 演员名
         * @return 最终消息
         */
        String messageBuildMethod(String performerName);
    }
}