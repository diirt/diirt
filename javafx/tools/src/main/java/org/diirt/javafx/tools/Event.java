/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import org.diirt.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public interface Event {

    public Timestamp getTimestamp();

    public String getPvName();

    public abstract Object getEvent();
}
