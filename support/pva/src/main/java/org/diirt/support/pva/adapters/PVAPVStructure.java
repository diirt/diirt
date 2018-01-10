/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.adapters;

import org.epics.pvdata.pv.PVStructure;

/**
 * @author msekoranja
 *
 */
public class PVAPVStructure extends PVAPVField {

        public PVAPVStructure(PVStructure pvStructure, boolean disconnected)
        {
                super(pvStructure, disconnected);
        }

        public PVStructure getPVStructure() {
                return (PVStructure)pvField;
        }

}
