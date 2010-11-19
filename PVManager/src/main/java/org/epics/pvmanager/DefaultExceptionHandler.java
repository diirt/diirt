/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

/**
 *
 * @author carcassi
 */
class DefaultExceptionHandler extends ExceptionHandler {

    private final PV<?> pv;

    DefaultExceptionHandler(PV<?> pv) {
        this.pv = pv;
    }

    @Override
    public void handleException(Exception ex) {
        pv.setLastException(ex);
        pv.firePvValueChanged();
    }

}
