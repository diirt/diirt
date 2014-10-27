/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.integration;

import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderEvent;

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
