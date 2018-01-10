/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;

/**
 *
 * @author carcassi
 */
public class PVReaderValueCondition extends PVReaderCondition<Object> {

    private final VTypeMatchMask mask;
    private final Object expectedValue;

    public PVReaderValueCondition(VTypeMatchMask mask, Object value) {
        this.mask = mask;
        this.expectedValue = value;
    }

    @Override
    public boolean accept(PVReader<Object> reader, PVReaderEvent<Object> event) {
        Object actualValue = reader.getValue();
        return mask.match(expectedValue, actualValue) == null;
    }

}
