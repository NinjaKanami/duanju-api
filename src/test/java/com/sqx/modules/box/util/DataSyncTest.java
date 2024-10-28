package com.sqx.modules.box.util;

import com.sqx.modules.box.vo.BoxCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zbc
 * @version 1.0
 * @since 2024/10/25 14:34
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class DataSyncTest {
    @Resource
    private DataSync dataSync;

    @Test
    void sync() throws Exception {
        boolean sync = dataSync.syncUserCollection("18827526652", new BigDecimal(100), 1, "11");
        System.out.println(sync);
    }

    @Test
    void get() throws Exception {
        BoxCollection userCollection = dataSync.getUserCollection("18827526652");
        System.out.println(userCollection);
    }
}
