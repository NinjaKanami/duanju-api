package com.sqx.modules.box.entity;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Zbc
 * @version 1.0
 * @since 2024/10/17 15:53
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充...");
        /*
          将当前时间格式化为字符串，并填充到metaObject中的"createTime"字段。
          @param metaObject MyBatis的元对象，用于操作实体类的属性
         */
        this.strictInsertFill(metaObject, "createTime", String.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充...");
        // this.strictUpdateFill(metaObject, "updateTime", String.class, LocalDateTime.now().toString());
        this.strictUpdateFill(metaObject, "createTime", String.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
