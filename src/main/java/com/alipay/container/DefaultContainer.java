/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.container;

import com.alipay.container.annotation.ServiceProvider;
import com.alipay.container.aop.InterceptorHandler;
import com.alipay.container.aop.MethodInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 简单服务容器
 *
 * @author dalong.wdl
 * @version $Id: DefaultContainer.java, v 0.1 2019年08月29日 10:03 AM dalong.wdl Exp $
 */
public class DefaultContainer implements Container {
    private static Logger LOGGER = Logger.getLogger(DefaultContainer.class.getName());

    private String             resourceName = "";
    private Map<Class, Object> localCache   = new HashMap<Class, Object>();

    private static Container ins;

    public DefaultContainer(String configResource) {
        this.resourceName = configResource;

        ins = this;
    }

    @Override
    public <T> T getBean(String beanId) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (localCache.containsKey(clazz)) {
            return (T) localCache.get(clazz);
        }

        if (!clazz.isInterface()) {
            try {
                Object instance = clazz.newInstance();
                Annotation[] annotations = instance.getClass().getAnnotations();
                for(Annotation annotation : annotations) {
                    if (annotation.annotationType().equals(ServiceProvider.class)){
                        Object proxyInstance = Proxy.newProxyInstance(instance.getClass().getClassLoader(),
                                instance.getClass().getInterfaces(), new InterceptorHandler(instance));
                        localCache.put(clazz, proxyInstance);
                        return (T) localCache.get(clazz);
                    }
                }
                localCache.put(clazz, instance);
                // 生成代理类
                return (T) localCache.get(clazz);
            } catch (Exception e) {
                LOGGER.log(Level.FINEST, e.getMessage(), e);
            }
        }

        return null;
    }

    public static Container getInstance() {
        return ins;
    }

}