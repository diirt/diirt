/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import javax.xml.bind.annotation.XmlAttribute;
import org.epics.vtype.VDouble;

/**
 *
 * @author carcassi
 */
class XmlVDouble extends XmlVNumberMetaData {

    @XmlAttribute
    Double value;

    public Double getValue() {
        return value;
    }

    public VDouble getVDouble() {
        return VDouble.of(getValue(), getAlarm(), getTime(), getDisplay());
    }
}
