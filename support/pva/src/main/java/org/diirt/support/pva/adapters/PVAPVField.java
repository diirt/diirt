/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.adapters;

import org.epics.pvdata.pv.PVField;

/**
 * @author msekoranja
 *
 */
public class PVAPVField {

        protected final PVField pvField;

        public PVAPVField(PVField pvField, boolean disconnected)
        {
                this.pvField = pvField;
        }

        public PVField getPVField() {
                return pvField;
        }

        @Override
        public String toString() {
                return pvField.toString();
        }


}
