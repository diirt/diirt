/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Basic type for statistical information of numeric types. The methods never return
 * null, even if no connection was ever made. One <b>must always look</b>
 * at the alarm severity to be able to correctly interpret the value.
 * <p>
 * This type can be used regardless of the method used to calculate the average
 * (instances: &Sigma;<i>x<sub>i</sub>/N</i>,
 * time: &Sigma;<i>x<sub>i</sub>&Delta;t<sub>i</sub>/&Delta;t</i>,
 * time with linear interpolation, exponential backoff, ...).
 * <p>
 * Coding to {@code Statistics<T extends Number>} allows to create a client that works on statistics,
 * regardless of the type.
 *
 * @author carcassi
 */
public interface Statistics<T extends Number> {
    T getAverage();
    T getStdDev();
    T getMin();
    T getMax();
}
