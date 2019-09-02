/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.train.mock;

/**
 *
 * @author dalong.wdl
 * @version $Id: MockUser.java, v 0.1 2019年08月29日 10:29 AM dalong.wdl Exp $
 */
public class MockUser {
    /** 用户名 */
    private String name;

    /** 用户购票数量 */
    private int   ticketCount;

    /**
     * Getter method for property name.
     *
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property counterType.
     *
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
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
}