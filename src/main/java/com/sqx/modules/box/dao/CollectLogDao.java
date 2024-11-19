package com.sqx.modules.box.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.sqx.modules.box.entity.CollectLog;
import org.apache.ibatis.annotations.Select;

/**
 * (CollectLog)表数据库访问层
 *
 * @author makejava
 * @since 2024-10-17 15:49:34
 */
@Mapper
public interface CollectLogDao extends BaseMapper<CollectLog> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CollectLog> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CollectLog> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CollectLog> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CollectLog> entities);

    /**
     * 查询待同步记录with phone
     *
     * @return List<CollectLog>
     */
    @Select("select c.*,u.phone from collect_log c left join tb_user u on c.user_id = u.user_id where is_sync = 0 and type = 0 " +
            "AND (c.retry_time < DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s') or c.retry_time is null) order by retry_time asc limit 100")
    List<CollectLog> selectSyncCollectLog();

}
