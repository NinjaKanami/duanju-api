package com.sqx.modules.urlAddress.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.urlAddress.entity.UrlAddress;

public interface UrlAddressService extends IService<UrlAddress> {

    UrlAddress selectUrlAddressOne();

}
