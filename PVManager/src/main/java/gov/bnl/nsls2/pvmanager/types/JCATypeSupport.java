/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.types;

import gov.aps.jca.dbr.DBR_TIME_Double;
import gov.aps.jca.dbr.Severity;
import gov.aps.jca.dbr.Status;
import gov.bnl.nsls2.pvmanager.NullUtils;
import gov.bnl.nsls2.pvmanager.TimedTypeSupport;
import gov.bnl.nsls2.pvmanager.TypeSupport;
import gov.bnl.nsls2.pvmanager.TypeSupport.Notification;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author carcassi
 */
public class JCATypeSupport {
    public static void addJCATypeSupport() {
        TypeSupport.addTypeSupport(DBR_TIME_Double.class, new TimedTypeSupport<DBR_TIME_Double>() {

            @Override
            public BigDecimal timestampOf(DBR_TIME_Double object) {
                return object.getTimeStamp().asBigDecimal();
            }

            @Override
            public Notification<DBR_TIME_Double> prepareValue(DBR_TIME_Double oldValue, DBR_TIME_Double newValue) {
                // Initialize value if never initialized
                if (oldValue == null)
                    oldValue = new DBR_TIME_Double();

                // If it's the same timestamp and the same value, we
                // assume nothing has to be changed
                if (NullUtils.equalsOrBothNull(oldValue.getTimeStamp().asBigDecimal(), newValue.getTimeStamp().asBigDecimal())
                        && oldValue.getDoubleValue()[0] == newValue.getDoubleValue()[1]) {
                    return new Notification<DBR_TIME_Double>(false, null);
                }

                // Update old value and notify
                oldValue.getDoubleValue()[0] = newValue.getDoubleValue()[0];
                oldValue.setTimeStamp(newValue.getTimeStamp());
                oldValue.setSeverity(newValue.getSeverity());
                oldValue.setStatus(newValue.getStatus());
                return new Notification<DBR_TIME_Double>(true, oldValue);
            }
        });
    }
}
