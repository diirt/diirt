/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public class TypeStatistics extends PVType<TypeStatistics> {
    private double average;
    private double min;
    private double max;
    private double stdDev;

    public double getAverage() {
        return average;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getStdDev() {
        return stdDev;
    }

    public void setStatistics(double average, double min, double max, double stdDev) {
        if (average != this.average || min != this.min || max != this.max || stdDev != this.stdDev) {
            this.average = average;
            this.min = min;
            this.max = max;
            this.stdDev = stdDev;
            firePvValueChanged();
        }
    }

    @Override
    void setTo(TypeStatistics newValue) {
        synchronized(newValue) {
            setStatistics(newValue.average, newValue.min, newValue.max, newValue.stdDev);
        }
    }

}
