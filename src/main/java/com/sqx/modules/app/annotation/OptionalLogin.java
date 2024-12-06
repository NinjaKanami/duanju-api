package com.sqx.modules.app.annotation;

import java.lang.annotation.*;

/**
 * 可选登录效验
 *
 * @author Zbc
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptionalLogin {
}
