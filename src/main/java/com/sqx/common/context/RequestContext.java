package com.sqx.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author zouqi
 * @version Id: RequestContext, v 0.1 2023/4/14 23:47 zouqi Exp $
 */
public class RequestContext {

    private static final ThreadLocal<Base> BASE = new TransmittableThreadLocal<>();


    public static <T> Base init(T t) {
        Base<T> base = new Base();
        BASE.set(base);
        return base;
    }


    public static Base base() {
        return BASE.get();
    }

    public static void clear() {
        BASE.remove();
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class Base<T> {

        /**
         * 用户基础信息
         */
        private T user;

        /**
         * 登录的ip
         */
        private String remoteAddress;

        /**
         * token
         */
        private String token;

        /**
         * 请求URL
         */
        private String url;


        /**
         * 签名(对外)
         */
        private String signature;


    }

}
