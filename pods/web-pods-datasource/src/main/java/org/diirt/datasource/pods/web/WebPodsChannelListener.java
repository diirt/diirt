/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import javax.websocket.CloseReason;

/**
 *
 * @author carcassi
 */
public class WebPodsChannelListener {

    public void onConnectionEvent(boolean connected, boolean writeConnected) {

    }

    public void onValueEvent(Object value) {

    }

    public void onErrorEvent(String errorMessage) {

    }

    public void onWriteCompletedEvent(boolean successful, String error){

    }

    public void onDisconnect(CloseReason reason) {

    }

}
