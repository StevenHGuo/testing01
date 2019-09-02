/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.train.booking.service;

import com.alipay.train.booking.model.BookingOrder;

import java.util.List;

/**
 * 火车票订单服务定义，支持购票、退票、查询所有订单功能。
 *
 * @author dalong.wdl
 * @version $Id: OrderService.java, v 0.1 2019年08月29日 9:30 AM dalong.wdl Exp $
 */
public interface OrderService {

    /**
     * 购买火车票，为了简化测试。服务不抛出异常，如果购票失败返回null订单。
     *
     * @param userName - 购票用户名
     * @param ticketCount - 购票数量
     * @return 返回购票订单，如果购票失败返回null
     */
    BookingOrder buy(String userName, int ticketCount);

    /**
     * 退票操作
     *
     * @param orderNo - 购买订单号
     * @return 返回是否退票成功。
     */
    boolean unbuy(String orderNo);

    /**
     * 查询所有订票订单
     *
     * @param pageNo - 分页编号，从 1开始
     * @param pageSize - 分页大小，默认 10
     * @return 返回查询结果
     */
    List<BookingOrder> listBookingOrder(int pageNo, int pageSize);
}