/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carcassi
 */
@XmlRootElement(name="values")
class XmlValues {

    @XmlElements({
        @XmlElement(name = "vDouble", type = XmlVDouble.class)
    })
    protected List<ReplayValue> value;

    public List<ReplayValue> getValues() {
        if (value == null) {
            value = new ArrayList<ReplayValue>();
        }
        return this.value;
    }
}
