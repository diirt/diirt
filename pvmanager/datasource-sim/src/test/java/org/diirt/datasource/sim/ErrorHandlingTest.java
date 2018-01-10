/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.ReadFunction;
import static org.diirt.datasource.ExpressionLanguage.*;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import org.diirt.vtype.VDouble;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.DesiredRateExpressionImpl;
import org.diirt.datasource.expression.SourceRateExpression;
import org.diirt.datasource.expression.SourceRateExpressionImpl;
import org.diirt.util.time.TimeDuration;
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
        return new DesiredRateExpressionImpl<VDouble>(expression, new ReadFunction<VDouble>() {

            @Override
            public VDouble readValue() {
                throw new UnsupportedOperationException("Mistakes were made");
            }
        }, "test");
    }

    public static SourceRateExpression<VDouble> exception(SourceRateExpression<VDouble> expression) {
        return new SourceRateExpressionImpl<VDouble>(expression, new ReadFunction<VDouble>() {

            @Override
            public VDouble readValue() {
                throw new UnsupportedOperationException("Mistakes were made");
            }
        }, "test");
    }

    private boolean notificationReceived;
    private String error;

    @Test
    public void exceptionInFunction() throws Exception {
        final PVReader<VDouble> pv = PVManager.read(exception(latestValueOf(vDouble("gaussian()"))))
                .readListener(new PVReaderListener<VDouble>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VDouble> event) {
                        PVReader<VDouble> pv = event.getPvReader();
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
                })
                .maxRate(TimeDuration.ofHertz(10));
        notificationReceived = false;

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
        notificationReceived = false;
        final PVReader<VDouble> pv = PVManager.read(exception(latestValueOf(vDouble("gaussian()"))))
                .readListener(new PVReaderListener<VDouble>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VDouble> event) {
                        PVReader<VDouble> pv = event.getPvReader();
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
                })
                .maxRate(TimeDuration.ofHertz(10));

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