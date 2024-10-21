package com.sqx.modules.box.service.impl;

import com.sqx.common.utils.Result;
import com.sqx.modules.box.service.CollectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zbc
 * @version 1.0
 * @since 2024/10/21 15:07
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class CollectServiceImplTest {
    @Resource
    private CollectService collectService;

    @Test
    void synthesise() {
        Result synthesise = collectService.synthesise(100816L, 1000);
        System.out.println(synthesise);
    }
}
