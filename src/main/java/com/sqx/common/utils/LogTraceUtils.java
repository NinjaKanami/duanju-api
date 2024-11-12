package com.sqx.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * 日志类工具
 * @author zouqi
 * @version Id: LogUtils, v 0.1 2020/4/19 16:02 zouqi Exp $
 */
@Slf4j
public class LogTraceUtils {

    /**
     * 跟踪链
     */
    public static final String  TRACE_ID      = "Trace-ID";

    /**
     * 单次跟踪链
     */
    public static final String  TRACE_SPAN_ID = "TRACE_SPAN_ID";
    /**
     * 模块
     */
    public static final String  MODULE        = "LOGGER_MODULE";
    /**
     * 模块分类
     */
    public static final String  CATEGORY      = "LOGGER_CATEGORY";
    /**
     * 子模块分类
     */
    public static final String  SUBCATEGORY   = "LOGGER_SUBCATEGORY";
    /**
     * 过滤条件1
     */
    private static final String FILTER1       = "LOGGER_FILTER1";
    /**
     * 过滤条件2
     */
    private static final String FILTER2       = "LOGGER_FILTER2";

    private LogTraceUtils() {

    }

    /**
     *
     */
    public static void removeAll() {
        MDC.clear();

    }

    /**
     * Remove trace.
     */
    public static void removeTraceId() {
        MDC.remove(TRACE_ID);
    }

    /**
     * Gets trace.
     *
     * @return the trace
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    /**
     * Sets trace id.
     *
     * @param traceId the trace id
     */
    public static void setTraceId(String traceId) {
        if (StringUtils.isBlank(traceId)) {
            MDC.remove(TRACE_ID);
            return;
        }
        MDC.put(TRACE_ID, traceId);
    }

    /**
     * Gets trace.
     *
     * @return the trace
     */
    public static String getTraceSpanId() {
        return MDC.get(TRACE_SPAN_ID);
    }

    /**
     * Sets trace id.
     *
     * @param traceSpanId the trace id
     */
    public static void setTraceSpanId(String traceSpanId) {
        if (StringUtils.isBlank(traceSpanId)) {
            MDC.remove(TRACE_SPAN_ID);
            return;
        }
        MDC.put(TRACE_SPAN_ID, traceSpanId);
    }

    /**
     *
     * @param filter1
     */
    public static void setFilter1(String filter1) {
        if (StringUtils.isBlank(filter1)) {
            MDC.remove(FILTER1);
        } else {
            MDC.put(FILTER1, filter1);
        }
    }

    /**
     *
     * @param filter2
     */
    public static void setFilter2(String filter2) {
        if (StringUtils.isBlank(filter2)) {
            MDC.remove(FILTER2);
        } else {
            MDC.put(FILTER2, filter2);
        }
    }

    /**
     * Sets module.
     *
     * @param module the module
     */
    public static void setModule(String module) {
        if (StringUtils.isNotEmpty(module)) {
            MDC.put(MODULE, module);
        } else {
            removeModule();
        }
    }

    /**
     * Remove module.
     */
    public static void removeModule() {
        MDC.remove(MODULE);
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public static void setCategory(String category) {
        if (StringUtils.isBlank(category)) {
            MDC.remove(CATEGORY);
            return;
        }
        MDC.put(CATEGORY, category);
    }

    /**
     *
     * @param subCategory
     */
    public static void setSubCategory(String subCategory) {
        if (StringUtils.isBlank(subCategory)) {
            MDC.remove(SUBCATEGORY);
        } else {
            MDC.put(SUBCATEGORY, subCategory);
        }
    }

    /**
     * Remove category.
     */
    public static void removeCategory() {
        MDC.remove(CATEGORY);
    }

}
