package com.sqx.modules.common.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.sys.entity.SysUserEntity;

/**
 * @author fang
 * @date 2020/7/8
 */
public interface CommonInfoService extends IService<CommonInfo> {

    /**
     * 保存对象
     *
     * @param
     */
    Result update(CommonInfo commonInfo, SysUserEntity user);

    /**
     * 获取一个对象
     */
    CommonInfo findOne(int id);

    /**
     * 删除一个
     */
    Result delete(long id);

    /**
     * 修改
     */
    Result updateBody(CommonInfo commonInfo);
    /**
     * 通过类型查询
     */
    Result findByType(Integer type);

    /**
     * 通过类型查询
     */
    Result findByTypeAndCondition(String condition);

    Result selectTypeList(JSONArray jsonArray);

}