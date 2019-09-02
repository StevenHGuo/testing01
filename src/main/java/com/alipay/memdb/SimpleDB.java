/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.memdb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * // TODO
 * 可以改进的点：
 * Map的具体实现，
 * 第一种方式：使用ConcurrentHashMap替代HashMap，具体的原因如下：
 * <p>
 * 关于锁的实现：
 * 如果使用HashMap，在并发的执行的情况下，需要通过Synchronized锁来保证同步。而synchronized是对整个对象
 * （当前的代码中是针对SimpleDB）进行加锁。synchronized加锁的方式monitorenter、monitorexit命令来实现
 * 同步。该同步的实现是一种隐式的同步实现，对于实例来说加锁的是this对象；对于静态变量来说，加锁的是Class
 * 对象。monitorenter、monitorexit的具体实现是对象会记录当前加锁的次数。当一个并发由于了该对象的锁之后，
 * 其它线程只能等待。
 * <p>
 * 而ConcurrentHashMap相比Synchronized的实现更加的细粒度化。ConcurrentHashMap的同步实现是采用的分离锁，
 * 对ConcurrentHashMap内部的Segment(具体的实现是HashEntry数组)，数据在存储的时候是采用分段的方式，而对
 * 数据进行加锁的时候也是采用对对应的Segment进行加锁。而HashEntry的同步使用的是volatile关键字保证可见行，
 * 也利用不可变对象的机制改进，以最优化性能。
 * <p>
 * 扩容：
 * HashMap扩容的时候是整体的扩容；而ConcurrentHashMap是Segment局部扩容。性能更优。
 * <p>
 * <p>
 * Remove、Get需要实现同步
 * remove，get也需要保证同步，不然会造成计算的错误。原因：JAVA程序的多线程执行在执行的时候，是采用时间分片的方式执行的，
 * 一个线程执行一段时间之后可能会停止换成另外一个线程执行。两个线程之间执行的顺序是没有语义支持的，JVM执行的时候会对其
 * 进行优化，可能会讲一个实际发生顺序为：remove("A"),get("A")的对象变为get("A"),remove("B")的实际执行，那么就会对
 * 调用方造成误解。所以这两种方式也都需要对其进行同步语义的规定。针对HashMap对象来说同步的保证就是synchronized的方式。
 * 最有的方式还是如前边说的使用：ConcurrentHashMap。
 *
 * @author dalong.wdl
 * @version $Id: SimpleDB.java, v 0.1 2019年08月29日 10:20 AM dalong.wdl Exp $
 */
public class SimpleDB implements MemDB {
    private Map<String, Object> data = new ConcurrentHashMap<String, Object>();

    @Override
    public boolean insert(String key, Object row) {
        data.put(key, row);
        return true;
    }

    @Override
    public boolean update(String key, Object row) {
        data.put(key, row);
        return true;
    }

    /**

     */
    @Override
    public boolean remove(String key) {
        data.remove(key);
        return true;
    }

    @Override
    public <T> T getObject(String key) {
        return (T) data.get(key);
    }

    @Override
    public <T> List<T> scan(String prefix, int limit) {
        return null;
    }

    public List allDBObject() {
        Collection values = data.values();
        return new ArrayList(values);
    }

    @Override
    public void commit() {

    }
}