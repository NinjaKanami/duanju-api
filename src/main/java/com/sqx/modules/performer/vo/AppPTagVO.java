package com.sqx.modules.performer.vo;

import com.sqx.modules.performer.entity.PTag;
import com.sqx.modules.performer.entity.Performer;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class AppPTagVO implements Serializable {
    private Long id;

    private String name;

    private Integer pageIndex;

    public AppPTagVO(PTag entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.pageIndex = entity.getPageIndex();
    }

    public static List<AppPTagVO> fromEntityList(List<PTag> entities) {
        List<AppPTagVO> appPTags = new ArrayList<>(entities.size());
        for (PTag entity : entities) {
            appPTags.add(new AppPTagVO(entity));
        }
        return appPTags;
    }
}
