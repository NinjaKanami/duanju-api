package com.sqx.modules.performer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.performer.entity.Performer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (Performer)表数据库访问层
 *
 * @author bootun
 * @since 2024-11-06
 */
@Mapper
public interface PerformerDao extends BaseMapper<Performer> {
    List<Performer> selectPerformersWithCondition(@Param("page") Integer page,
                                                  @Param("limit") Integer limit,
                                                  @Param("name") String name,
                                                  @Param("sex") Integer sex,
                                                  @Param("company") String company,
                                                  @Param("tag") Integer tag,
                                                  @Param("sort") String sort);

    /**
     * 创建演员并拿到演员id
     * @param performer 演员信息
     */
    @Insert("INSERT INTO performer (name, sex, company, profile, fake_follower, photo) " +
            "VALUES (#{name}, #{sex}, #{company}, #{profile}, #{fakeFollower}, #{photo})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPerformer(Performer performer);

    // 更新 performer 表信息，返回受影响的行数
    @Update("UPDATE performer SET name = #{name}, sex = #{sex}, company = #{company}, " +
            "profile = #{profile}, fake_follower = #{fakeFollower}, photo = #{photo} " +
            "WHERE id = #{id}")
    int updatePerformer(Performer performer);


    // 删除 performer 表中指定的演员，返回受影响的行数
    @Delete("DELETE FROM performer WHERE id = #{id}")
    int deletePerformer(@Param("id") Long performerId);
}
