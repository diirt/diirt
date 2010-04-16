/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.jca;

import gov.aps.jca.dbr.DBR;
import gov.aps.jca.dbr.DBR_TIME_Double;
import gov.bnl.nsls2.pvmanager.ConnectionManager;
import gov.bnl.nsls2.pvmanager.Expression;
import gov.bnl.nsls2.pvmanager.NullUtils;
import gov.bnl.nsls2.pvmanager.TimeStamp;
import gov.bnl.nsls2.pvmanager.TimedTypeSupport;
import gov.bnl.nsls2.pvmanager.TypeSupport;
import gov.bnl.nsls2.pvmanager.TypeSupport.Notification;
import java.util.ArrayList;
import java.util.List;

/**
 * Adds support for CA types as defined in JCA.
 *
 * @author carcassi
 */
public class JCASupport {

    public static ConnectionManager jca() {
        return JCAConnectionManager.INSTANCE;
    }
    
    static {
        install();
    }

    static void install() {
        TypeSupport.addTypeSupport(DBR_TIME_Double.class, new TimedTypeSupport<DBR_TIME_Double>() {

            @Override
            public TimeStamp extractTimestamp(DBR_TIME_Double object) {
                return TimeStamp.epicsTime(object.getTimeStamp().secPastEpoch(), object.getTimeStamp().nsec());
            }

            @Override
            public Notification<DBR_TIME_Double> prepareNotification(DBR_TIME_Double oldValue, DBR_TIME_Double newValue) {
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

    public static <T extends DBR> Expression<T> epicsPv(String name, Class<T> epicsType) {
        return new Expression<T>(name, epicsType);
    }

    public static <T extends DBR> List<Expression<T>> epicsPvs(List<String> names, Class<T> epicsType) {
        List<Expression<T>> expressions = new ArrayList<Expression<T>>();
        for (String name : names) {
            expressions.add(epicsPv(name, epicsType));
        }
        return expressions;
    }
}
