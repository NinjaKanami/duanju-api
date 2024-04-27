package com.sqx.modules.sdk.controller;


import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.sdk.service.SdkInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wucahng
 * @since 2023-02-20
 */
@RestController
@RequestMapping("/app/sdkInfo/")
public class AppSdkInfoController {
    @Autowired
    private SdkInfoService infoService;

    /**
     * 卡密兑换
     *
     * @return
     */
    @Login
    @PostMapping("sdkExchange")
    public Result sdkExchange(@RequestAttribute("userId") Long userId, String sdkContent) {
        return infoService.sdkExchange(userId, sdkContent);
    }




}

