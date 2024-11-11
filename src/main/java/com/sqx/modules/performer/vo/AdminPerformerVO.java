package com.sqx.modules.performer.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.modules.performer.entity.Performer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

@Data
public class AdminPerformerVO implements Serializable {
    private Long id;

    private String name;

    private Integer sex;

    private String company; // 公司

    private String profile; // 简介

    private Long fakeFollower; // 虚拟粉丝数

    private String photo; // 照片URL

    private List<Integer> courseList; // 演员关联的短剧id

    private Long realFollower; // 真实粉丝数

    private Long totalFollower; // 真实粉丝数

    private List<Integer> tagList; // 演员关联的标签类型id

    public AdminPerformerVO() {
    }

    public AdminPerformerVO(Performer entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.sex = entity.getSex();
        this.company = entity.getCompany();
        this.profile = entity.getProfile();
        this.fakeFollower = entity.getFakeFollower() == null ? 0 : entity.getFakeFollower();
        this.photo = entity.getPhoto();
        this.realFollower = entity.getRealFollower() == null ? 0 : entity.getRealFollower();
        this.totalFollower = this.fakeFollower + this.realFollower;
        if (entity.getCourseStr() != null) {
            this.courseList = new ArrayList<>();
            String[] courseSplit = entity.getCourseStr().split(",");
            for (String s : courseSplit) {
                this.courseList.add(Integer.parseInt(s));
            }
        }
        if (entity.getTagsStr() != null) {
            this.tagList = new ArrayList<>();
            String[] tagSplit = entity.getTagsStr().split(",");
            for (String s : tagSplit) {
                this.tagList.add(Integer.parseInt(s));
            }
        }
    }

    public static List<AdminPerformerVO> fromEntityList(List<Performer> entities) {
        List<AdminPerformerVO> appPerformers = new ArrayList<>(entities.size());
        for (Performer entity : entities) {
            appPerformers.add(new AdminPerformerVO(entity));
        }
        return appPerformers;
    }

    public static PageUtils fromEntityPage(Page<Performer> performers) {
        Page<AdminPerformerVO> res = new Page<>();
        res.setRecords(AdminPerformerVO.fromEntityList(performers.getRecords()));
        res.setOrders(performers.getOrders());
        res.setCurrent(performers.getCurrent());
        res.setSize(performers.getSize());
        res.setTotal(performers.getTotal());
        res.setPages(performers.getPages());
        return new PageUtils(res);
    }

    public Performer toEntity() {
        Performer entity = new Performer();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setSex(this.sex);
        entity.setCompany(this.company);
        entity.setProfile(this.profile);
        entity.setFakeFollower(this.fakeFollower);
        entity.setPhoto(this.photo);
        entity.setRealFollower(this.realFollower);
        entity.setTotalFollower(this.totalFollower);
        if (this.courseList != null) {
            StringBuilder sb = new StringBuilder();
            for (Integer courseId : this.courseList) {
                sb.append(courseId).append(",");
            }
            entity.setCourseStr(sb.toString());
        }
        if (this.tagList != null) {
            StringBuilder sb = new StringBuilder();
            for (Integer tagId : this.tagList) {
                sb.append(tagId).append(",");
            }
            entity.setTagsStr(sb.toString());
        }
        return entity;
    }
}
