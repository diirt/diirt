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
    public static String subscribeJson = "{"
            + "\"message\":\"subscribe\","
            + "\"id\":1,"
            + "\"pv\":\"sim://noise\""
            + "}";
    public static MessageSubscribe subscribeMessage = new MessageSubscribe(1, "sim://noise", null, -1, true);
}
