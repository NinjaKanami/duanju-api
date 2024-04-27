package com.sqx.modules.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description member
 * @author fang
 * @date 2021-06-19
 */
@Data
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 会员特权id
     */
    @TableId(type = IdType.AUTO)
    private Integer memberId;

    /**
     * 特权等级
     */
    private Integer memberIndex;
    
    /**
     * 特权图标
     */
    private String memberImg;

    /**
     * 特权名称
     */
    private String memberName;

    /**
     * 特权详情
     */
    private String memberContent;

    /**
     * 排序
     */
    private String sort;

    public Member() {}
}
