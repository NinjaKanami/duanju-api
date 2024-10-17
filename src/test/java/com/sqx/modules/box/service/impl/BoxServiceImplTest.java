package com.sqx.modules.box.service.impl;

import com.sqx.modules.box.entity.Box;
import com.sqx.modules.box.service.BoxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zbc
 * @version 1.0
 * @since 2024/10/17 15:29
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class BoxServiceImplTest {

    @Resource
    private BoxService boxService;

    @Test
    void save() {
        Box box = new Box();
        box.setUserId(1L);
        box.setCourseId(1L);
        box.setCount(1);
        boxService.save(box);
    }
}
