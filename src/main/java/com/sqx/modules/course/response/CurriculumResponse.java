package com.sqx.modules.course.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 查询推荐短剧信息的返回参数
 *
 * @author liyuan
 * @since 2021-07-15
 */
@Data
public class CurriculumResponse {

    /**
     * 短剧id
     */
    @TableField("course_id")
    private int courseId;
    /**
     * 短剧的封面图
     */
    @TableField("title_img")
    private String titleImg;
    /**
     * 短剧标题
     */
    private String title;
    /**
     * 短剧价钱
     */
    private String price;
    /**
     * 短剧购买量
     */
    @TableField("pay_num")
    private int payNum;


}
