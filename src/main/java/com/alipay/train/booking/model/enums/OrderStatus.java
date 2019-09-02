/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.train.booking.model.enums;

/**
 * 订单状态，
 *
 * !!!面试开发时，根据需要增加!!
 *
 * @author dalong.wdl
 * @version $Id: OrderStatus.java, v 0.1 2019年08月29日 9:36 AM dalong.wdl Exp $
 */
public enum OrderStatus {

    /** 初始化车票的预定 */
    BOOKING_INIT,

    /** 预定成功，可以使用状态 */
    BOOKING_OK,

    /** 预定失败*/
    BOOKING_FAILING;
}