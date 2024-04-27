package com.sqx.modules.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.app.entity.App;

import java.util.List;

/**
 * 升级
 *
 */
public interface AppService extends IService<App> {

    App selectAppById(Long id);

    int insertApp(App app);

    int updateAppById(App app);

    int deleteAppById(Long id);

    List<App> selectNewApp();

}
