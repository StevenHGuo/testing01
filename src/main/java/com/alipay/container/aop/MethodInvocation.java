/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.container.aop;

import java.lang.reflect.Method;

/**
 *
 * @author dalong.wdl
 * @version $Id: MethodInvocation.java, v 0.1 2019年08月29日 9:23 AM dalong.wdl Exp $
 */
public interface MethodInvocation {

    Object process();

    Method getMethod();

    Object[] getArguments();
}