/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import org.epics.pvmanager.expression.SourceRateExpression;
import org.epics.pvmanager.expression.SourceRateExpressionImpl;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.epics.util.time.TimeDuration;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class ErrorHandlingTest {

    public ErrorHandlingTest() {
    }

    @BeforeClass
    public static void modifyDefaultSource() {
        PVManager.setDefaultDataSource(SimulationDataSource.simulatedData());
    }

    public static DesiredRateExpression<VDouble> exception(DesiredRateExpression<VDouble> expression) {
        return new DesiredRateExpressionImpl<VDouble>(expression, new Function<VDouble>() {

            @Override
            public VDouble getValue() {
                throw new UnsupportedOperationException("Mistakes were made");
            }
        }, "test");
    }

    public static SourceRateExpression<VDouble> exception(SourceRateExpression<VDouble> expression) {
        return new SourceRateExpressionImpl<VDouble>(expression, new Function<VDouble>() {

            @Override
            public VDouble getValue() {
                throw new UnsupportedOperationException("Mistakes were made");
            }
        }, "test");
    }

    private boolean notificationReceived;
    private String error;

    @Test
    public void exceptionInFunction() throws Exception {
        final PVReader<VDouble> pv = PVManager.read(exception(latestValueOf(vDouble("gaussian()")))).maxRate(TimeDuration.ofHertz(10));
        notificationReceived = false;

        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                notificationReceived = true;
                Exception ex = pv.lastException();
                if (ex == null) {
                    pv.close();
                    error = "Didn't get exception";
                }
                if (pv.lastException() != null) {
                    pv.close();
                    error = "Exception was not cleared";
                }
                if (!ex.getMessage().equals("Mistakes were made")) {
                    pv.close();
                    error = "Didn't get right exception";
                }
            }
        });

        Thread.sleep(1000);
        pv.close();
        if (notificationReceived == false) {
            fail("Didn't receive notifications");
        }
        if (error != null) {
            fail(error);
        }
    }

    @Test
    public void exceptionSourceRateFunction() throws Exception {
        final PVReader<VDouble> pv = PVManager.read(exception(latestValueOf(vDouble("gaussian()")))).maxRate(TimeDuration.ofHertz(10));
        notificationReceived = false;

        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                notificationReceived = true;
                Exception ex = pv.lastException();
                if (ex == null) {
                    pv.close();
                    error = "Didn't get exception";
                }
                if (pv.lastException() != null) {
                    pv.close();
                    error = "Exception was not cleared";
                }
                if (!ex.getMessage().equals("Mistakes were made")) {
                    pv.close();
                    error = "Didn't get right exception";
                }
            }
        });

        Thread.sleep(1000);
        pv.close();
        if (notificationReceived == false) {
            fail("Didn't receive notifications");
        }
        if (error != null) {
            fail(error);
        }
    }

}