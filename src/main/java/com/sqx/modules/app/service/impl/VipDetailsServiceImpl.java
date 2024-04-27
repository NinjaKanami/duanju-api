package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.VipDetailsDao;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.VipDetailsService;
import org.springframework.stereotype.Service;

@Service
public class VipDetailsServiceImpl extends ServiceImpl<VipDetailsDao, VipDetails> implements VipDetailsService {



    @Override
    public Result selectVipDetails() {
        return Result.success().put("data", baseMapper.selectList(null));
    }

    @Override
    public Result insertVipDetails(VipDetails vipDetails) {
        int cpunt = baseMapper.insert(vipDetails);
        if (cpunt > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }




}
