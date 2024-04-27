package com.sqx.modules.course.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

//导入集模板
@Data
public class CourseDetailsIn implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "名称", orderNum = "1")
    private String courseDetailsName;

    @Excel(name = "封面图", orderNum = "2")
    private String titleImg;

    @Excel(name = "视频地址", orderNum = "3")
    private String videoUrl;

    @Excel(name = "介绍", orderNum = "4")
    private String content;

    @Excel(name = "点赞数", orderNum = "5")
    private Integer goodNum;

    @Excel(name = "价格", orderNum = "6")
    private BigDecimal price;

    @Excel(name = "是否收费", orderNum = "7", replace = {"否_2", "是_1"})
    private Integer isPrice;

    @Excel(name = "是否是推荐", orderNum = "8", replace = {"否_2", "是_1", "否_null"})
    private Integer good;

    @Excel(name = "语种类型", orderNum = "9")
    private String languageType;
}
