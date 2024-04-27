package com.sqx.modules.course.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

//导入集模板
@Data
public class CourseIn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "标题", orderNum = "1")
    private String title;

    @Excel(name = "短剧介绍", orderNum = "3")
    private String details;

    @Excel(name = "封面图", orderNum = "4")
    private String titleImg;

    @Excel(name = "是否收费", orderNum = "5", replace = {"免费_2", "收费_1"})
    private Integer isPrice;

    @Excel(name = "短剧标签", orderNum = "6")
    private String courseLabel;

    @Excel(name = "价格", orderNum = "8")
    private BigDecimal price;

    @Excel(name = "购买次数", orderNum = "9")
    private Integer payNum;

    @Excel(name = "播放量", orderNum = "10")
    private Integer viewCounts;

    @Excel(name = "是否是推荐", orderNum = "11", replace = {"否_0", "是_1"})
    private Integer is_recommend;

    @Excel(name = "上下架", orderNum = "12", replace = {"上架_1", "下架_2"})
    private Integer status;

    @Excel(name = "是否完结", orderNum = "13", replace = {"否_0", "是_1"})
    private Integer over;

    @Excel(name = "状态", orderNum = "14", replace = {"使用中_0", "已删除_1"})
    private Integer isDelete;

}
