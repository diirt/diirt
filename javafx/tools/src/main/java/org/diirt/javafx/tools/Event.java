/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.time.Instant;

/**
 *
 * @author carcassi
 */
public interface Event {

    public Instant getTimestamp();

    public String getPvName();

    public abstract Object getEvent();
}
