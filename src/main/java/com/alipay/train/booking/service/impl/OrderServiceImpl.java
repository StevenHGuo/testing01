/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.train.booking.service.impl;

import com.alipay.container.DefaultContainer;
import com.alipay.container.annotation.ServiceProvider;
import com.alipay.container.annotation.ServiceReference;
import com.alipay.memdb.MemDB;
import com.alipay.memdb.SimpleDB;
import com.alipay.train.booking.model.BookingOrder;
import com.alipay.train.booking.model.enums.OrderStatus;
import com.alipay.train.booking.service.OrderService;
import com.alipay.train.booking.service.interceptor.UserInfoCheck;
import com.alipay.train.mock.MockService;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @author dalong.wdl
 * @version $Id: OrderServiceImpl.java, v 0.1 2019年08月29日 9:46 AM dalong.wdl Exp $
 */
@ServiceProvider(id = "orderService", aop = UserInfoCheck.class)
public class OrderServiceImpl implements OrderService {

    @ServiceReference(ref = "simple_db")
    private MemDB db = DefaultContainer.getInstance().getBean(SimpleDB.class);

    private Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

    /**
     * // TODO
     * // 多并发操作单例服务类。
     * 使用单例服务类的原因：JVM在执行方法见调用的时候使用的是将执行代码以帧栈的方式记录在内存中并运行。
     * 这样可以减少实例化对象的个数。但是相应的就需要保证多线程之间公用变量的可见行、一致性，原子性。
     * <p>
     * 实现方式有：
     * synchornized、voliate和java原生提供的AtomicXXX相关的对象。
     * <p>
     * synchronized是一个重度锁，锁的是当前的实例对象，使用该方式会导致性能下降。
     * <p>
     * voliate可以保证多线程见的可见行、一致性。但是保证不了原子性。因为JVM在敌营voliate的语义的时候：
     * 被voliate修饰的变量，会优先查找公共内存中的数据的只。而不是去查找L1、L2缓存中的值。那么对于：
     * i++这样的操作，在实际的执行过程中仍然会被解析会3个JVM的执行命令，那么就无法保证原子性。
     * <p>
     * AtomicInterger相关的关键字使用的是CAS（Compare AS Swap）,也就是在执行的时候，JVM会去拿寄存器中的值和
     * 内存中的值做对比，如果相同了，才会将其修改为新的值。而具体的实现是通过CPU提供的相应命令来实现的，这个
     * 在不同的CPU内核中有不同的实现。但是使用AtomicInteger会出现ABA问题，就是一个值在执行的时候，从A->B->A先变为
     * B最后又变成了A。为了避免这个问题，我们可以使用AtomicStampedReference，为 其加上版本号。
     * 在当前的方法中，使用AtomicInteger对象足够了。
     * <p>
     * 当前场景i的作用主要是为不同的订单产生不同的编号，来表示唯一。当前程序使用的是一台机器，该方式足以适用。
     * <p>
     * 未来的规划：
     * 第一点：分布式运行环境ID生成策略
     * 但是在实际的运行过程中，程序都是运行在分布式环境中，对于分布式环境中，一种比较精简的ID设计和生成方式是
     * 使用雪花算法。ID在生成的时候，使用的Java类型一般选择为Long型，雪花算法将Long型所代表的64位做了不同
     * 位数的划分代表不同的值：关键元素的选择有：时间戳、计算机标识符、线程标识符、业务标识符。以此来表示全局
     * 唯一。同时又满足了分布式的横向扩展。
     * <p>
     * 第二点：DB执行成功率的记录
     * 记录DB操作的成功率，并设置相应的告警（比如钉钉实时通知），这样当程序的失败率高的时候，我们可以提前感知，
     * 不必等到真正到用户感知到之后我们才发现程序的的异常，具体的配置信息需要根据实际的运行去调整优化，寻找一个
     * 一个我们可以发现异常但是这个异常又不会被用户感知到的配置具体实现点。
     */
    private AtomicInteger i = new AtomicInteger(0);

    /**
     * 总票数，100W
     * 用户判断当前的票数有没有消费完
     * // TODO
     * 后续规划
     * 实现通过接口的方式可以自动补充票数（也可以称为8月1日开放20W张票、8月2日开放20W张票的操作），这样我们
     * 可以不需要重新部署服务就可以实现程序的持续、可服用运行。
     */
    private AtomicLong TOTAL_TICKET_COUNT = new AtomicLong(1000000);

    /**
     * 所对象，通过指定一个锁对象，程序就不会锁定当前对象
     */
    private final Object lock = new Object();

    /**
     * 记录已经购票的用户
     */
    private Queue<String> BUY_USERS = new ConcurrentLinkedQueue<String>();

    /**
     * 记录已经退票以此的用户
     */
    private List<String> LEFT_USER = new ArrayList<String>();

    @Override
    public BookingOrder buy(String userName, int ticketCount) {
        if (BUY_USERS.contains(userName)) {
            logger.info("当前用户已经购买:username:" + userName);
            return null;
        }
        // 判断票是否已经购买完毕
        synchronized (lock) {
            // 判断余票是否足够，如果不足够，那么将不能够进行购票
            if (TOTAL_TICKET_COUNT.intValue() - ticketCount < 0) {
                return null;
            }
            long leftTicket = TOTAL_TICKET_COUNT.longValue();
            leftTicket -= ticketCount;
            TOTAL_TICKET_COUNT.set(leftTicket);
            BUY_USERS.add(userName);
        }

        BookingOrder order = new BookingOrder();
        order.setOrderNo("biz_" + System.currentTimeMillis() + "_" + userName + "_" + i.incrementAndGet());
        order.setUserName(userName);
        order.setTicketCount(ticketCount);
        order.setStatus(OrderStatus.BOOKING_OK);

        try {
            db.insert(order.getOrderNo(), order);
        } catch (Exception e) {
            try {
                db.insert(order.getOrderNo(), order);
            } catch (Exception e1) {
                // 记录日志便于
                logger.warning(e1.getMessage());
                return null;
            }
        }

        return order;
    }

    @Override
    public boolean unbuy(String orderNo) {

        BookingOrder order = db.getObject(orderNo);
        String userName = order.getUserName();
        if (LEFT_USER.contains(userName)) {
            logger.info("当前用户已经退票过一次，不可以再次退票：username:" + userName);
            return false;
        }
        synchronized (lock) {
            try {
                db.remove(orderNo);
                long leftTicket = TOTAL_TICKET_COUNT.longValue();
                leftTicket += order.getTicketCount();
                TOTAL_TICKET_COUNT.set(leftTicket);
                LEFT_USER.add(userName);
                BUY_USERS.remove(userName);
                logger.info("退票操作：username:{}" + userName);
                return true;
            } catch (Exception e) {
                logger.info("订单删除执行失败：" + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public List<BookingOrder> listBookingOrder(int pageNo, int pageSize) {
        return null;
    }
}