package com.sqx.modules.urlAddress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.urlAddress.dao.UrlAddressDao;
import com.sqx.modules.urlAddress.entity.UrlAddress;
import com.sqx.modules.urlAddress.service.UrlAddressService;
import org.springframework.stereotype.Service;


@Service
public class UrlAddressServiceImpl extends ServiceImpl<UrlAddressDao, UrlAddress> implements UrlAddressService {

    @Override
    public UrlAddress selectUrlAddressOne(){
        //获取最少数量的
        UrlAddress urlAddress = baseMapper.selectOne(new QueryWrapper<UrlAddress>().last(" order by num asc limit 1"));
        urlAddress.setNum(urlAddress.getNum()==null?1:urlAddress.getNum()+1);
        baseMapper.updateById(urlAddress);
        return urlAddress;
    }


}
