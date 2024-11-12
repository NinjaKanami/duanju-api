package com.sqx.modules.box.service.impl;

import com.sqx.common.utils.Result;
import com.sqx.modules.box.service.BoxOnlineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zbc
 * @version 1.0
 * @since 2024/10/23 14:11
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class BoxOnlineServiceImplTest {

    @Resource
    private BoxOnlineService boxOnlineService;

    @Test
    void updateOnline() {
        Result result = boxOnlineService.updateOnline(10089L, 5, "589119569a95889fec12f71c85af0da9:1730900259");
        System.out.println(result);
    }
}
