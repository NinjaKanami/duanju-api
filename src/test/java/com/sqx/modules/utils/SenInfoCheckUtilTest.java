package com.sqx.modules.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zbc
 * @version 1.0
 * @since 2024/10/28 21:07
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class SenInfoCheckUtilTest {

    @Resource
    private SenInfoCheckUtil senInfoCheckUtil;
    @Test
    void getMpToken() {
        SenInfoCheckUtil.getMpToken();
    }
}
