package com.sqx.modules.performer.vo;

import com.sqx.modules.performer.entity.Performer;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class AppPerformerVO implements Serializable {
    private Long id;

    private String name;

    private Integer sex;

    private String company; // 公司

    private String profile; // 简介

    private Long followers; // 虚拟粉丝数

    private String photo; // 照片URL

    private List<Long> tags; // 标签列表, 使用","分隔

    public AppPerformerVO(Performer entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.sex = entity.getSex();
        this.company = entity.getCompany();
        this.profile = entity.getProfile();
        Long realFollowers = entity.getRealFollower() == null ? 0 : entity.getRealFollower();
        Long fakeFollowers = entity.getFakeFollower() == null ? 0 : entity.getFakeFollower();
        this.followers = realFollowers + fakeFollowers;
        this.photo = entity.getPhoto();
        ArrayList<Long> newTags = new ArrayList<>();
        for (String tag : entity.getTags().split(",")) {
            newTags.add(Long.parseLong(tag));
        }
        this.tags = newTags;
    }

    public Performer toEntity() {
        Performer entity = new Performer();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setSex(this.sex);
        entity.setCompany(this.company);
        entity.setProfile(this.profile);
        entity.setPhoto(this.photo);
        StringBuilder tags = new StringBuilder();
        for (Long tag : this.tags) {
            tags.append(tag).append(",");
        }
        entity.setTags(tags.toString());
        return entity;
    }

    public static List<AppPerformerVO> fromEntityList(List<Performer> entities) {
        List<AppPerformerVO> appPerformers = new ArrayList<>(entities.size());
        for (Performer entity : entities) {
            appPerformers.add(new AppPerformerVO(entity));
        }
        return appPerformers;
    }
}

