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
public class PVANTNDArray extends PVAPVStructure {

        public PVANTNDArray(PVStructure ntNdArray, boolean disconnected)
        {
                super(ntNdArray, disconnected);
        }

}
