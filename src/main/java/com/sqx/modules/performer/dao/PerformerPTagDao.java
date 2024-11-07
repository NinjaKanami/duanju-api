package com.sqx.modules.performer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.performer.entity.PerformerPTag;
import com.sqx.modules.performer.entity.PerformerUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * (PerformerPTag)表数据库访问层
 *
 * @author bootun
 * @since 2024-11-07
 */
@Mapper
public interface PerformerPTagDao extends BaseMapper<PerformerPTag> {
}
