/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.dbr.CTRL;
import gov.aps.jca.dbr.PRECISION;
import gov.aps.jca.dbr.TIME;
import java.text.NumberFormat;

import org.epics.util.stats.Range;
import org.epics.util.text.NumberFormats;
import org.epics.vtype.Display;
import org.epics.vtype.DisplayProvider;

/**
 *
 * @author carcassi
 */
class VNumberMetadata<TValue extends TIME, TMetadata extends CTRL> extends VMetadata<TValue> implements DisplayProvider {

    private final TMetadata metadata;
    private final boolean honorZeroPrecision;

    VNumberMetadata(TValue dbrValue, TMetadata metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, connPayload);
        this.metadata = metadata;
        this.honorZeroPrecision = connPayload.getJcaDataSource().isHonorZeroPrecision();
    }

    public Double getLowerDisplayLimit() {
        return (Double) metadata.getLowerDispLimit();
    }

    public Double getLowerCtrlLimit() {
        return (Double) metadata.getLowerCtrlLimit();
    }

    public Double getLowerAlarmLimit() {
        return (Double) metadata.getLowerAlarmLimit();
    }

    public Double getLowerWarningLimit() {
        return (Double) metadata.getLowerWarningLimit();
    }

    public String getUnits() {
        return metadata.getUnits();
    }

    public NumberFormat getFormat() {
        int precision = -1;
        if (metadata instanceof PRECISION) {
            precision = ((PRECISION) metadata).getPrecision();
        }

        // If precision is 0 or less, we assume full precision
        if (precision < 0) {
            return NumberFormats.toStringFormat();
        } else if (precision == 0) {
            if (honorZeroPrecision) {
                return NumberFormats.precisionFormat(0);
            } else {
                return NumberFormats.toStringFormat();
            }
        } else {
            return NumberFormats.precisionFormat(precision);
        }
    }

    public Double getUpperWarningLimit() {
        return (Double) metadata.getUpperWarningLimit();
    }

    public Double getUpperAlarmLimit() {
        return (Double) metadata.getUpperAlarmLimit();
    }

    public Double getUpperCtrlLimit() {
        return (Double) metadata.getUpperCtrlLimit();
    }

    public Double getUpperDisplayLimit() {
        return (Double) metadata.getUpperDispLimit();
    }

    @Override
    public Display getDisplay() {
        return Display.of(Range.of(getLowerDisplayLimit(), getUpperDisplayLimit()),
                          Range.of(getLowerAlarmLimit(), getUpperAlarmLimit()),
                          Range.of(getLowerWarningLimit(), getUpperWarningLimit()),
                          Range.of(getLowerCtrlLimit(), getUpperCtrlLimit()),
                          getUnits(),
                          getFormat());
    }

}
