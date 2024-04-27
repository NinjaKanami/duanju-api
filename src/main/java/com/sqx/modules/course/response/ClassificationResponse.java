package com.sqx.modules.course.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 查询短剧分类信息的返回参数
 * @author liyuan
 * @since  2021-07-15
 */
@Data
public class ClassificationResponse {

    /**
     * 短剧分类id
     */
    @TableField("classification_id")
    private int classificationId;
    /**
     * 短剧名称
     */
    @TableField("classification_name")
    private String classificationName;



}
