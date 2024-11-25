package com.sqx.modules.course.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

// 导入集模板
@Data
public class CourseIn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "标题", orderNum = "1")
    private String title;

    @Excel(name = "短剧介绍", orderNum = "2")
    private String details;

    @Excel(name = "封面图", orderNum = "3")
    private String titleImg;

    @Excel(name = "是否收费", orderNum = "4", replace = {"免费_2", "vip_1", "云短剧_3"})
    private Integer isPrice;

    @Excel(name = "短剧标签", orderNum = "5")
    private String courseLabel;

    @Excel(name = "价格", orderNum = "5")
    private BigDecimal price;

    @Excel(name = "购买次数", orderNum = "7")
    private Integer payNum;

    @Excel(name = "播放量", orderNum = "8")
    private Integer viewCounts;

    @Excel(name = "状态", orderNum = "9", replace = {"使用中_0", "已删除_1"})
    private Integer isDelete;

    @Excel(name = "上下架", orderNum = "10", replace = {"上架_1", "下架_2"})
    private Integer status;

    @Excel(name = "是否完结", orderNum = "11", replace = {"否_0", "是_1"})
    private Integer over;

    @Excel(name = "是否是推荐", orderNum = "12", replace = {"否_0", "是_1"})
    private Integer isRecommend;

    @Excel(name = "购买方式", orderNum = "15", replace = {"单集购买_0", "整剧购买_1"})
    private Integer priceType;

    @Excel(name = "是否可砍剧", orderNum = "16", replace = {"不可_0", "可以_1"})
    private Integer isCut;

    @Excel(name = "备案号", orderNum = "17")
    private String licenseNum;

    @Excel(name = "曾用名", orderNum = "18")
    private String formerName;

    @Excel(name = "出品方", orderNum = "19")
    private String producer;

    @Excel(name = "是否外部", orderNum = "20", replace = {"否_0", "是_1"})
    private Integer isExternal;

    @Excel(name = "外部链接", orderNum = "21")
    private String externalUrl;

    @Excel(name = "后台地区ID", orderNum = "22")
    private Integer areaId;

    @Excel(name = "后台平台ID", orderNum = "23")
    private Integer platformId;


}
