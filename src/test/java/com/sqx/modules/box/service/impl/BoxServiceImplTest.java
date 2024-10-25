package com.sqx.modules.box.service.impl;

import com.sqx.common.utils.Result;
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
        box.setCount(1);
        boxService.save(box);
    }

    @Test
    void selectBoxCollection() {
        Result result = boxService.selectBoxCollection(10086L);
        System.out.println(result);
    }

    @Test
    void openBox() {
        Result result = boxService.openBox(10086L, 10);
        System.out.println(result);
    }
}
