/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.web.pods.common;

/**
 *
 * @author carcassi
 */
public class MessageTestConstants {
    public static String subscribe1Json = "{"
            + "\"message\":\"subscribe\","
            + "\"id\":1,"
            + "\"pv\":\"sim://noise\""
            + "}";
    public static MessageSubscribe subscribe1Message = new MessageSubscribe(1, "sim://noise", null, -1, true);

    public static String subscribe2Json = "{"
            + "\"message\":\"subscribe\","
            + "\"id\":1,"
            + "\"pv\":\"sim://noise\","
            + "\"type\":\"VDouble\","
            + "\"maxRate\":10,"
            + "\"readOnly\":false"
            + "}";
    public static MessageSubscribe subscribe2Message = new MessageSubscribe(1, "sim://noise", "VDouble", 10, false);
    
    public static String unsubscribe1Json = "{"
            + "\"message\":\"unsubscribe\","
            + "\"id\":1"
            + "}";
    public static MessageUnsubscribe unsubscribe1Message = new MessageUnsubscribe(1);
    
    public static String write1Json = "{"
            + "\"message\":\"write\","
            + "\"id\":1,"
            + "\"value\":3.14"
            + "}";
    public static MessageWrite write1Message = new MessageWrite(1, 3.14);
    

}
