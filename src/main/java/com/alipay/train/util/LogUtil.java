/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.train.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author dalong.wdl
 * @version $Id: LogUtil.java, v 0.1 2019年08月29日 2:05 PM dalong.wdl Exp $
 */
public class LogUtil {

    public static void initLogger() {
        final String newFormat = "HH:mm:ss.SSS";

        ConsoleHandler consoleHandler = new ConsoleHandler();
        //SimpleFormatter f = new SimpleFormatter();
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord r) {
                Date date = new Date(r.getMillis());

                SimpleDateFormat dateFormat = new SimpleDateFormat(newFormat);

                String sDate = dateFormat.format(date);
                String lineSperator = System.getProperty("line.separator");

                StringBuilder sb = new StringBuilder();
                sb.append("[" + sDate + "]" + "[" + r.getLevel() + "]");
                sb.append(r.getMessage());
                //在一条日志结束后采用常量方式的系统换行符，因为 “\n” 形式可能不识别
                sb.append(lineSperator);

                return sb.toString();
            }
        });

        Logger LOGGER = Logger.getLogger("main");
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(consoleHandler);

        LOGGER = Logger.getLogger("com");
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(consoleHandler);
    }
}