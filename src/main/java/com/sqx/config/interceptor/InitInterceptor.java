package com.sqx.config.interceptor;

import com.sqx.common.context.RequestContext;
import com.sqx.common.utils.IPUtils;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.sys.entity.SysUserEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author zouqi
 * @version Id: InitInterceptor, v 0.1 2023/4/7 23:49 zouqi Exp $
 */
@Slf4j
public class InitInterceptor extends HandlerInterceptorAdapter {

    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return super.preHandle(request, response, handler);
    }

    /**
     * This implementation is empty.
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        RequestContext.Base<?> base = RequestContext.base();
        Long userId = null;
        String mobile = null;
        if (base.getUser() instanceof UserEntity) {
            userId = ((UserEntity) base.getUser()).getUserId();
            mobile = ((UserEntity) base.getUser()).getPhone();
            //临时兼容管理后台，后期迁移
        } else if (base.getUser() instanceof SysUserEntity) {
            userId = ((SysUserEntity) base.getUser()).getUserId();
            mobile = ((SysUserEntity) base.getUser()).getMobile();
        }
        log.info("url:{},userId:{},mobile:{},ip:{},请求耗时:{}", request.getRequestURI(), userId, mobile, base.getRemoteAddress(), endTime - startTime);
    }
}
