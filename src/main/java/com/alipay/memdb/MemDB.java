/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.memdb;

import java.util.List;

/**
 * 一个简单的内存KV数据库，只支持根据kv更新。
 *
 * ！！！面试需要注意，虽然是内存数据，单底层实现很不靠谱，经常抛异常！！！
 *
 * @author dalong.wdl
 * @version $Id: MemDB.java, v 0.1 2019年08月29日 10:11 AM dalong.wdl Exp $
 */
public interface MemDB {

    /**
     * 插入对象
     *
     * @param key - 主键
     * @param row - 插入对象
     * @return
     */
    boolean insert(String key, Object row);

    /**
     * 更新对象
     *
     * @param key - 更新主键
     * @param row - 更新数据行
     * @return
     */
    boolean update(String key, Object row);

    /**
     *
     * @param key - 删除主键
     * @return
     */
    boolean remove(String key);

    /**
     * 根据key 查询对象
     *
     * @param key 数据主键
     * @param <T>
     * @return
     */
    <T> T getObject(String key);

    /**
     * 根据主键前缀查询服务
     *
     * @param prefix - 主键前缀
     * @param limit - limit
     * @return
     */
    <T> List<T> scan(String prefix, int limit);

    /**
     * 数据库提交操作
     */
    void commit();

    /**
     * 内部测试使用，获取所有数据库对象。禁止业务使用
     *
     *
     * @return
     */
    @Deprecated
    List allDBObject();
}