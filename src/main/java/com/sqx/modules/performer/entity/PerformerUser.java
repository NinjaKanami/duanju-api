package com.sqx.modules.performer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@TableName(value = "performer_user")
@Data
public class PerformerUser implements Serializable {
    @NotNull(message = "演员id不能为空")
    private Long performer_id;

    @NotNull(message = "用户id不能为空")
    private Long user_id;

    public PerformerUser(Long performer_id, Long user_id) {
        this.performer_id = performer_id;
        this.user_id = user_id;
    }
}

