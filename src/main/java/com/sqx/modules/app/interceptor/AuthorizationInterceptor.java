package com.sqx.modules.app.interceptor;


import com.sqx.common.context.RequestContext;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.IPUtils;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import com.sqx.modules.app.annotation.Login;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

/**
 * 权限(Token)验证
 *
 */
@Slf4j
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    public static final String USER_KEY = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Login annotation;
        //上下文初始化
        RequestContext.init(UserEntity.class)
                .setRemoteAddress(IPUtils.getIpAddr(request)).setUrl(request.getRequestURI());
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        }else{
            return true;
        }

        try {
            if (annotation == null) {
                return true;
            }

            //获取用户凭证
            String token = request.getHeader(jwtUtils.getHeader());
            if (StringUtils.isBlank(token)) {
                token = request.getParameter(jwtUtils.getHeader());
            }

            //凭证为空
            if (StringUtils.isBlank(token)) {
                throw new SqxException(jwtUtils.getHeader() + "不能为空", HttpStatus.UNAUTHORIZED.value());
            }

            Claims claims = jwtUtils.getClaimByToken(token);
            if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
                throw new SqxException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
            }

            //设置userId到request里，后续根据userId，获取用户信息
            long userId = Long.parseLong(claims.getSubject());
            request.setAttribute(USER_KEY, userId);
            //记录用户最后一次调用接口的时间
            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(userId);
            userEntity.setOnLineTime(DateUtils.format(new Date()));
            userService.updateById(userEntity);

            RequestContext.base().setUser(userEntity).setToken(token);

            return true;
        } finally {
            RequestContext.Base<UserEntity> base = RequestContext.base();
            log.info("url:{},userId:{},ip:{},token:{}", base.getUrl(), Optional.ofNullable(base.getUser()).orElse(new UserEntity()).getUserId(), IPUtils.getIpAddr(request), base.getToken());
        }
    }
}
