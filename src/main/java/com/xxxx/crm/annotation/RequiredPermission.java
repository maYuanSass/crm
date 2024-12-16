package com.xxxx.crm.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解：
 *  定义控制层方法所需要对应资源的权限码
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {

    //权限码
    String code() default "";
}
