/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.container.annotation;

import com.alipay.container.aop.MethodInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 简单容器的服务提供注解，类似spring中bean 标签， 支持在服务中直接配置拦截器。
 *
 * @author dalong.wdl
 * @version $Id: ServiceReference.java, v 0.1 2019年08月29日 9:16 AM dalong.wdl Exp $
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceProvider {
    String id();

    Class<? extends MethodInterceptor> aop() default MethodInterceptor.class;
}