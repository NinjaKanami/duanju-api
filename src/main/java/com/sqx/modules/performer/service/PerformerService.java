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
}
