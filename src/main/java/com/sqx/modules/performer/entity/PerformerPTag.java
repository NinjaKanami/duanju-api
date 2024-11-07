package com.sqx.modules.performer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@TableName(value = "performer_ptag")
@Data
public class PerformerPTag implements Serializable {
    @NotNull(message = "演员id不能为空")
    private Long performer_id;

    @NotNull(message = "演员类型id不能为空")
    private Long ptag_id;

    public PerformerPTag(Long performer_id, Long ptag_id) {
        this.performer_id = performer_id;
        this.ptag_id = ptag_id;
    }
}

