package com.sqx.modules.app.service;


import com.sqx.common.utils.Result;

public interface IAppleService {

        Result getAppleUserInfo(String identityToken) throws Exception;

}
