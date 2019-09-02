/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.train.mock;

import com.alipay.train.booking.model.BookingOrder;
import com.alipay.train.booking.model.enums.OrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

/**
 * @author dalong.wdl
 * @version $Id: MockService.java, v 0.1 2019年08月29日 10:30 AM dalong.wdl Exp $
 */
public class MockService {
    private static Logger LOGGER = Logger.getLogger("main.mock");

    /**
     * 购票用户数
     */
    public static final int MAX_USER_COUNT = 100000;

    /**
     * 订单系统最大购票数量
     */
    public static final long MAX_TICKET_COUNT = 1000000L;

    private final static Random RANDOM = new Random();

    public final static String HUANG_NIU = "huang_niu";
    private String NO_LOGIN_USER = "no_login";
    private String USER = "user";

    private UserSummary userSummary = null;

    public List<MockUser> getAllUser() {

        // TODO
        // 明确指定容量大小，如果不指定使用的是默认的操作,程序会在执行过程中不断的进行扩容操作
        List<MockUser> userList = new ArrayList<MockUser>();

        Map<String, Integer> userTicet = new HashMap<String, Integer>();

        for (int i = 0; i < MAX_USER_COUNT; i++) {
            String userType = USER;

            if (i % 1000 == 0) {
                userType = NO_LOGIN_USER;
            } else if (i % 100 == 99) {
                userType = HUANG_NIU;
            }

            MockUser user = new MockUser();
            user.setName(userType + "_" + genNumBetween(1, MAX_USER_COUNT / 2));

            if (userType.equals(USER)) {
                if (userTicet.containsKey(user.getName())) {
                    user.setTicketCount(userTicet.get(user.getName()));
                } else {
                    int count = genNumBetween(5, 50);
                    userTicet.put(user.getName(), count);
                    user.setTicketCount(count);
                }
            } else {
                user.setTicketCount(genNumBetween(100, 1000));
            }

            userList.add(user);
        }

        UserSummary summary = count(userList);

        LOGGER.info("模拟用户信息摘要，有效用户数量:" + summary.userCount + ", 有效购票需求:" + summary.ticketCount + ","
                + " 黄牛数量:" + summary.huangUserCount + ", 黄牛票数:" + summary.huangTicketCount);

        userSummary = summary;
        return userList;
    }

    public void printOrderSummary(List orderList) {
        OrderSummary summary = createSummary(orderList);

        LOGGER.info("订单模拟信息摘要，生成订单数:" + summary.orderCount + ", 最终有效订单数:" + summary.orderOkCount + ","
                + " 订单票量:" + summary.orderOkTicktCount + ", 未注册人数:" + summary.noLoginUserOrderCount
                + ", 未注册购票数量:" + summary.noLoginUserOrderTicktCount
                + ", 正常用户订单量:" + summary.userOrderCount
                + ", 正常用户订票量:" + summary.userOrderTicktCount
                + ", 黄牛订单数:" + summary.niuOrderCount
                + ", 黄牛票量:" + summary.niuOrderTicktCount
        );

        if (summary.orderOkTicktCount > MAX_TICKET_COUNT) {
            LOGGER.warning(">>总票数超出计划数量:" + summary.orderOkTicktCount + " > " + MAX_TICKET_COUNT);
        }
        if (summary.noLoginUserOrderCount > 0) {
            LOGGER.warning(">>未注册用户购票成功:" + summary.noLoginUserOrderCount);
        }

        if (summary.niuOrderCount > 0) {
            LOGGER.warning(">>黄牛数量:" + summary.niuOrderCount);
        }

        if (summary.userOrderCount > userSummary.userCount) {
            LOGGER.warning(">>存在重复购票情况，用户订单:" + summary.userOrderCount + " > 用户数:" + userSummary.userCount);
        }
    }

    public OrderSummary createSummary(List orderList) {

        List<BookingOrder> bookingOrders = new ArrayList<BookingOrder>();

        for (Object o : orderList) {
            if (o instanceof BookingOrder) {
                bookingOrders.add((BookingOrder) o);
            }
        }


        OrderSummary summary = new OrderSummary();
        summary.orderCount = bookingOrders.size();

        for (BookingOrder o : bookingOrders) {
            if (o.getStatus() != OrderStatus.BOOKING_OK) {
                continue;
            }

            summary.orderOkCount++;
            summary.orderOkTicktCount += o.getTicketCount();


            if (o.getUserName().startsWith(NO_LOGIN_USER)) {
                summary.noLoginUserOrderCount++;
                summary.noLoginUserOrderTicktCount += o.getTicketCount();
            } else if (o.getUserName().startsWith(USER)) {
                summary.userOrderCount++;
                summary.userOrderTicktCount += o.getTicketCount();
            } else if (o.getUserName().startsWith(HUANG_NIU)) {
                summary.niuOrderCount++;
                summary.niuOrderTicktCount += o.getTicketCount();
            }
        }

        return summary;
    }

    public static int genNumBetween(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public boolean randomEvent(int percent) {
        return genNumBetween(1, 100) < percent;
    }

    private UserSummary count(List<MockUser> userList) {
        Set<String> user = new TreeSet<String>();
        Set<String> huang = new TreeSet<String>();

        UserSummary summary = new UserSummary();

        for (MockUser u : userList) {
            if (u.getName().startsWith(NO_LOGIN_USER)) {
                continue;
            }
            if (u.getName().startsWith(USER) && user.contains(u.getName())) {
                continue;
            }
            if (u.getName().startsWith(HUANG_NIU) && huang.contains(u.getName())) {
                continue;
            }

            if (u.getName().startsWith(USER)) {
                summary.ticketCount += u.getTicketCount();
                user.add(u.getName());
            } else if (u.getName().startsWith(HUANG_NIU)) {
                summary.huangTicketCount += u.getTicketCount();
                huang.add(u.getName());
            }
        }

        summary.userCount = user.size();
        summary.huangUserCount = huang.size();

        return summary;

    }

    public static class UserSummary {
        /**
         * 有效合法用户数，去除重复账号
         */
        public int userCount;

        /**
         * 有效购买需求数量，
         */
        public int ticketCount;

        /**
         * 黄牛数量
         */
        public int huangUserCount;

        /**
         * 黄牛票数
         */
        public int huangTicketCount;
    }

    public static class OrderSummary {
        /**
         * 所有订单数量
         */
        public int orderCount;

        /**
         * 有效订单数量
         */
        public int orderOkCount;

        /**
         * 有效票数
         */
        public int orderOkTicktCount;

        /**
         * 合法用户订单数量
         */
        public int userOrderCount;

        /**
         * 合法用户订单数量
         */
        public int userOrderTicktCount;

        /**
         * 未登录用户订单数量
         */
        public int noLoginUserOrderCount;

        /**
         * 未登录用户购票数
         */
        public int noLoginUserOrderTicktCount;

        /**
         * 黄牛订单数
         */
        public int niuOrderCount;

        /**
         * 黄牛订单数购票
         */
        public int niuOrderTicktCount;
    }

}