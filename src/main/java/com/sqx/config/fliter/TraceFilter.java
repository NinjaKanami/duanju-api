package com.sqx.config.fliter;


import com.sqx.common.context.RequestContext;
import com.sqx.common.utils.IPUtils;
import com.sqx.common.utils.LogTraceUtils;
import com.sqx.modules.app.entity.UserEntity;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

@Order(1)
@WebFilter
@Component
@Slf4j
public class TraceFilter implements Filter {

    /**
     * Called by the web container to indicate to a filter that it is
     * being placed into service.
     *
     * <p>The servlet container calls the init
     * method exactly once after instantiating the filter. The init
     * method must complete successfully before the filter is asked to do any
     * filtering work.
     *
     * <p>The web container cannot place the filter into service if the init
     * method either
     * <ol>
     * <li>Throws a ServletException
     * <li>Does not return within a time period defined by the web container
     * </ol>
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * taken out of service.
     *
     * <p>This method is only called once all threads within the filter's
     * doFilter method have exited or after a timeout period has passed.
     * After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter.
     *
     * <p>This method gives the filter an opportunity to clean up any
     * resources that are being held (for example, memory, file handles,
     * threads) and make sure that any persistent state is synchronized
     * with the filter's current state in memory.
     */
    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            //链路
            String traceId = request.getHeader(LogTraceUtils.TRACE_ID);
            LogTraceUtils.setTraceId(StringUtils.defaultString(traceId, UUID.randomUUID().toString()));
            long startTime = System.currentTimeMillis();
            // 设置请求开始时间
            request.setAttribute("startTime", startTime);

            //记录日志
            HttpServletRequest requestWrapper = getRequestWrapper(request);
            //环境拦截
////            verifyService.checkEnv(request);
            filterChain.doFilter(requestWrapper, servletResponse);
        } else {
            LogTraceUtils.setTraceId(UUID.randomUUID().toString());
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    private HttpServletRequest getRequestWrapper(HttpServletRequest request) {
        try {

            return request;
//            long time = System.currentTimeMillis();
//            log.info("Request param is : [{}]", parseParams(request));
//            log.info("日志转换耗时1:{}", System.currentTimeMillis() - time);
//            return request;
//            RequestWrapper requestWrapper = new RequestWrapper(request);
//            log.info("Request body is : [{}]", new String(requestWrapper.getBody()));
//            log.info("日志转换耗时2:{}", System.currentTimeMillis() - time);
//            return requestWrapper;
        } catch (Exception e) {
            log.error("流转换异常,", e);
            return request;
        }
    }

    public String parseParams(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            request.getParameter(name);
            stringBuilder.append(name).append("=").append(
                    request.getParameter(name)
            ).append(";");
        }
        return stringBuilder.toString();
    }

}
