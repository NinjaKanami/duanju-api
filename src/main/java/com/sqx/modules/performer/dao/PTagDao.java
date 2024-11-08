package com.sqx.modules.performer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.performer.entity.PTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (PTag)表数据库访问层
 *
 * @author bootun
 * @since 2024-11-06
 */
@Mapper
public interface PTagDao extends BaseMapper<PTag> {

    // 插入 performer_ptag 表关系
    @Insert("<script>" +
            "INSERT INTO performer_ptag (performer_id, ptag_id) VALUES " +
            "<foreach collection='tagIds' item='tagId' separator=','>" +
            "(#{performerId}, #{tagId})" +
            "</foreach>" +
            "</script>")
    int insertPerformerTags(@Param("performerId") Long performerId, @Param("tagIds") List<String> tagIds);

    // 删除 performer_ptag 表关系(如果没有关联)
    @Delete("DELETE FROM ptag WHERE id = #{tagId} AND ( SELECT COUNT( * ) FROM performer_ptag WHERE ptag_id = #{tagId} LIMIT 1 ) = 0")
    int deletePerformerTagsIfNoRelation(@Param("tagId") Long tagId);

}
