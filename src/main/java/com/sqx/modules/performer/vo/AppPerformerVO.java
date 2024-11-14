package com.sqx.modules.performer.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.performer.entity.Performer;
import lombok.Data;
import org.bouncycastle.util.Strings;

import java.io.Serializable;
import java.util.*;

@Data
public class AppPerformerVO implements Serializable {
    private Long id;

    private String name;

    private Integer sex;

    private String company; // 公司

    private String profile; // 简介

    private Long followers; // 虚拟粉丝数

    private Boolean isFollowed; // 是否已追

    private String photo; // 照片URL

    private Map<Long, String> tags; // 标签名称列表, 使用","分隔

    private List<Course> courseList; // 参演短剧

    public AppPerformerVO(Performer entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.sex = entity.getSex();
        this.company = entity.getCompany();
        this.profile = entity.getProfile();
        Long realFollowers = entity.getRealFollower() == null ? 0 : entity.getRealFollower();
        Long fakeFollowers = entity.getFakeFollower() == null ? 0 : entity.getFakeFollower();
        this.followers = realFollowers + fakeFollowers;
        if (entity.getTotalFollower() != null) {
            // 榜单排序的时候，是根据总粉丝数排序，会直接把totalFollower拿出来，所以这里直接赋值就好
            this.followers = entity.getTotalFollower();
        }
        this.photo = entity.getPhoto();
        // 标签列表转换
        Map<Long, String> newTags = new HashMap<>();
        if (entity.getTagsStr() != null) {
            for (String tag : entity.getTagsStr().split(",")) {
                String[] tagIdAndName = Strings.split(tag, ':');
                if (tagIdAndName.length == 2) {
                    newTags.put(Long.parseLong(tagIdAndName[0]), tagIdAndName[1]);
                }
            }
        }
        this.tags = newTags;
    }

    public static List<AppPerformerVO> fromEntityList(List<Performer> entities) {
        List<AppPerformerVO> appPerformers = new ArrayList<>(entities.size());
        for (Performer entity : entities) {
            appPerformers.add(new AppPerformerVO(entity));
        }
        return appPerformers;
    }
}

