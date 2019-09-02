package com.alipay.container.aop;

import com.alipay.container.annotation.ServiceProvider;
import com.alipay.container.aop.impl.MethodInvocationImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 拦截器的具体实现
 *
 * @author GuoHongYin
 * @date 2019/09/01
 */
public class InterceptorHandler implements InvocationHandler {
    private Object object;

    public InterceptorHandler(Object object) throws Exception {
        if (object == null) {
            throw new Exception("空对象异常");
        }
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServiceProvider serviceProvider = object.getClass().getAnnotation(ServiceProvider.class);
        Class<? extends MethodInterceptor> methodInterceptor = serviceProvider.aop();
        MethodInterceptor methodInterceptorInstance = methodInterceptor.newInstance();
        if (!MethodInterceptor.class.getName().equals(methodInterceptor.getName())) {
            // 用户配置了拦截器信息，需要执行
            Object object = methodInterceptorInstance.invoke(new MethodInvocationImpl(method, args));
            if (object == null) {
                // 被拦截
                return null;
            }

        }
        return method.invoke(object, args);
    }
}
