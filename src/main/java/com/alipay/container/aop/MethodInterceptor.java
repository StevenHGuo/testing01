/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.container.aop;

/**
 *
 * @author dalong.wdl
 * @version $Id: invocation.java, v 0.1 2019年08月29日 9:22 AM dalong.wdl Exp $
 */
public interface MethodInterceptor {

    /**
     *
     */
    Object invoke(MethodInvocation invocation) throws Throwable;

}