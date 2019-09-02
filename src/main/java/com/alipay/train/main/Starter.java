/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.train.main;

import com.alipay.container.Container;
import com.alipay.container.DefaultContainer;
import com.alipay.memdb.MemDB;
import com.alipay.memdb.SimpleDB;
import com.alipay.train.booking.model.BookingOrder;
import com.alipay.train.booking.service.OrderService;
import com.alipay.train.booking.service.impl.OrderServiceImpl;
import com.alipay.train.mock.MockService;
import com.alipay.train.mock.MockUser;
import com.alipay.train.util.LogUtil;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * // TODO
 * 后续规划
 *
 *
 * @author dalong.wdl
 * @version $Id: Starter.java, v 0.1 2019年08月29日 9:56 AM dalong.wdl Exp $
 */
public class Starter {
    private static Logger LOGGER = Logger.getLogger("main");

    public static void main(String[] args) throws InterruptedException {
        LogUtil.initLogger();

        LOGGER.info("初始化火车票订票模拟系统，服务容器....");
        Container container = new DefaultContainer("beans.properties");

        final OrderService orderService = container.getBean(OrderServiceImpl.class);

        LOGGER.info("订单服务加载，bean:" + orderService);

        final MemDB memDB = container.getBean(SimpleDB.class);

        final MockService mockService = new MockService();
        //初始化10万用户列表
        List<MockUser> userList = mockService.getAllUser();

        LOGGER.info("初始化用户数量:" + userList.size());

        //创建服务窗口
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(MockService.MAX_USER_COUNT));

        //提交所有购票用户
        for (final MockUser user : userList) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    BookingOrder order = orderService.buy(user.getName(), user.getTicketCount());
                    if (order != null && order.getUserName().startsWith(MockService.HUANG_NIU)) {

                        //黄牛随机性退票
                        if (mockService.randomEvent(50)) {
                            orderService.unbuy(order.getOrderNo());
                        }
                    }
                }
            });
        }

        //等待1分钟购票完成
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        //盘点购票订单信息
        List allTicketCount = memDB.allDBObject();

        mockService.printOrderSummary(allTicketCount);

        LOGGER.info("火车票订购系统模拟结束.");
        LOGGER.info("DONE");
    }

}