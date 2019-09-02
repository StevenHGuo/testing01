/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.container.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 简单服务引用标签，类似spring的bean ref 依赖计算。
 *
 * @author dalong.wdl
 * @version $Id: ServiceReference.java, v 0.1 2019年08月29日 9:16 AM dalong.wdl Exp $
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceReference {

    String ref();
}