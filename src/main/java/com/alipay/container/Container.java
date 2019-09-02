/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.container;

/**
 * 容器服务定义接口
 *
 * @author dalong.wdl
 * @version $Id: Container.java, v 0.1 2019年08月29日 10:00 AM dalong.wdl Exp $
 */
public interface Container {

    /**
     * 通过bean Id 查询服务
     *
     * @param beanId - bean ID
     * @param <T> 返回服务注册的实现
     * @return bean服务
     */
    <T> T getBean(String beanId);

    /**
     * 通过接口查询服务
     *
     * @param clazz - 服务的接口定义
     * @param <T> 返回服务注册的实现
     * @return bean服务
     */
    <T> T getBean(Class<T> clazz);
}