/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.train.booking.service.interceptor;

import com.alipay.container.aop.MethodInterceptor;
import com.alipay.container.aop.MethodInvocation;
import com.alipay.train.booking.model.BookingOrder;

/**
 * @author dalong.wdl
 * @version $Id: UserInfoCheck.java, v 0.1 2019年08月29日 9:51 AM dalong.wdl Exp $
 */
public class UserInfoCheck implements MethodInterceptor {

    /**
     * 未登录用户的默认名
     */
    private static String DEFAULT_UN_LOGIN_USERNAME = "no_login";

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (invocation.getMethod() == null) {
            return null;
        }
        Object[] arguments = invocation.getArguments();
        if (arguments == null || arguments.length <= 0 || arguments[0] == null) {
            return null;
        }
        String userName = String.valueOf(arguments[0]);
        // TODO
        // 未来规划。NO_SING类用户的验证将其防止在前段页面进行判断，这样可以减少很多无效的请求。避免
        // 对服务器造成过大的压力。使服务器在计算的时候尽量是进行有效计算。
        // 但是这个代码仍然需要保留：保证代码的健壮性。因为可能存在黑客攻击，发送大量的这种无效请求
        // 使服务器瘫痪
        if (userName.startsWith(DEFAULT_UN_LOGIN_USERNAME)) {
            // 未登录用户不能够购买车票
            return null;
        }
        return invocation;
    }
}