package com.sqx.modules.box.vo;

import com.sqx.modules.box.entity.CollectLog;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 盲盒 青龙 龙鳞 记录 最大发行 剩余数量
 *
 * @author Zbc
 * @version 1.0
 * @since 2024/10/18 11:24
 */
@Data
public class BoxCollection {
    private int box;
    private int collect;
    private BigDecimal collectPoint;
    private int collectMax;
    private int collectRemain;
    private boolean registered;
    private List<CollectLog> collectLogs;
}
