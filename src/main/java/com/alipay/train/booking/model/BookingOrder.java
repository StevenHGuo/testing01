/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.train.booking.model;

import com.alipay.train.booking.model.enums.OrderStatus;

/**
 * 车票订单模型，可以根据需要增加属性。已经存在的属性不要修改。
 *
 * @author dalong.wdl
 * @version $Id: BookingOrder.java, v 0.1 2019年08月29日 9:31 AM dalong.wdl Exp $
 */
public class BookingOrder {

    private String orderNo;

    private String userName;

    private int ticketCount;

    private OrderStatus status;

    /**
     * Getter method for property orderNo.
     *
     * @return property value of orderNo
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * Setter method for property counterType.
     *
     * @param orderNo value to be assigned to property orderNo
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * Getter method for property userName.
     *
     * @return property value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter method for property counterType.
     *
     * @param userName value to be assigned to property userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter method for property ticketCount.
     *
     * @return property value of ticketCount
     */
    public int getTicketCount() {
        return ticketCount;
    }

    /**
     * Setter method for property counterType.
     *
     * @param ticketCount value to be assigned to property ticketCount
     */
    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    /**
     * Getter method for property status.
     *
     * @return property value of status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Setter method for property counterType.
     *
     * @param status value to be assigned to property status
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}