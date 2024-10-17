package com.sqx.modules.app.service.impl;

import com.sqx.common.utils.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zbc
 * @version 1.0
 * @since 2024/10/17 17:23
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Test
    void verifyUserIdNumber() {
        Result result = userService.verifyUserIdNumber("15515650895", "412728199812067272", "周勃辰");
        System.out.println(result);
    }
}
