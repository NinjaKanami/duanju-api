package com.sqx.modules.box.service.impl;

import com.sqx.common.utils.Result;
import com.sqx.modules.box.service.BoxPointService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zbc
 * @version 1.0
 * @since 2024/10/17 21:43
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class BoxPointServiceImplTest {

    @Autowired
    private BoxPointService boxPointService;

    @Test
    void getPoints() {
        int result = boxPointService.getPoints(10086L, 10010L, 1000);
        System.out.println(">>>>>>>>>>>>>>>>" + result);

    }
}
