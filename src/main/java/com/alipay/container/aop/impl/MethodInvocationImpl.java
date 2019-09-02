package com.alipay.container.aop.impl;

import com.alipay.container.aop.MethodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * @author GuoHongYin
 * @date 2019/09/01
 */
public class MethodInvocationImpl implements MethodInvocation {
    Logger logger = Logger.getLogger(MethodInvocationImpl.class.getName());

    private Method method;

    private Object[] arguments;

    public MethodInvocationImpl(Method method, Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public Object process() {
        try {
            return getMethod().invoke(getArguments());
        } catch (IllegalAccessException e) {
            logger.warning("权限不够，不能够访问当前对象：" + getMethod().getName());
        } catch (InvocationTargetException e) {
            logger.warning("方法执行异常：" + e.getMessage());
        }
        return null;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }
}
