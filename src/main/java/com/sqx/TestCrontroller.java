package com.sqx;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.AppDao;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zouqi
 * @version Id: TestCrontroller, v 0.1 2024/11/4 10:57 zouqi Exp $
 */
@Slf4j
@RestController
@Api(value = "短剧信息", tags = {"短剧信息"})
@RequestMapping(value = "/test")
public class TestCrontroller {

    @Resource
    AppDao appDao;

    @GetMapping("/aa")
    @ApiOperation("查询短剧信息")
    public void selectCourse() {
        appDao.selectNewApp();
        log.info("333");
    }
}
