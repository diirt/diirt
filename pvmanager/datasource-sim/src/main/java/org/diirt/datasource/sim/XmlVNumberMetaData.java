/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import java.text.NumberFormat;
import javax.xml.bind.annotation.XmlAttribute;

import org.epics.util.stats.Range;
import org.epics.vtype.Display;
import org.epics.vtype.DisplayProvider;

/**
 *
 * @author carcassi
 */
class XmlVNumberMetaData extends XmlVMetaData implements DisplayProvider {

    @XmlAttribute
    String units;
    @XmlAttribute
    Double lowerDisplayLimit;
    @XmlAttribute
    Double lowerCtrlLimit;
    @XmlAttribute
    Double lowerAlarmLimit;
    @XmlAttribute
    Double lowerWarningLimit;
    @XmlAttribute
    Double upperWarningLimit;
    @XmlAttribute
    Double upperAlarmLimit;
    @XmlAttribute
    Double upperCtrlLimit;
    @XmlAttribute
    Double upperDisplayLimit;

    public Double getLowerDisplayLimit() {
        return lowerDisplayLimit;
    }

    public Double getLowerCtrlLimit() {
        return lowerCtrlLimit;
    }

    public Double getLowerAlarmLimit() {
        return lowerAlarmLimit;
    }

    public Double getLowerWarningLimit() {
        return lowerWarningLimit;
    }

    public String getUnits() {
        return units;
    }

    public NumberFormat getFormat() {
        // TODO fix
        return NumberFormat.getNumberInstance();
    }

    public Double getUpperWarningLimit() {
        return upperWarningLimit;
    }

    public Double getUpperAlarmLimit() {
        return upperAlarmLimit;
    }

    public Double getUpperCtrlLimit() {
        return upperCtrlLimit;
    }

    public Double getUpperDisplayLimit() {
        return upperDisplayLimit;
    }

    public Display getDisplay() {
        return Display.of(Range.of(getLowerDisplayLimit(), getUpperDisplayLimit()),
                          Range.of(getLowerAlarmLimit(), getUpperAlarmLimit()),
                          Range.of(getLowerWarningLimit(), getUpperWarningLimit()),
                          Range.of(getLowerCtrlLimit(), getUpperCtrlLimit()),
                          getUnits(),
                          getFormat());
    }
}
