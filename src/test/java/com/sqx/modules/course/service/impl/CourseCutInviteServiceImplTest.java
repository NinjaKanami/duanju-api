package com.sqx.modules.course.service.impl;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.service.CourseCutInviteService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CourseCutInviteServiceImplTest {
    @Resource
    CourseCutInviteService courseCutInviteService;
    @org.junit.jupiter.api.Test
    void insertCourseCutInvite() {
        Result result = courseCutInviteService.insertCourseCutInvite(7L, 14965L);
        System.out.println(result);

    }

}
