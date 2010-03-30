/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 * Payload the represents statistical information.
 *
 * @author carcassi
 */
public class DoubleStatistics {
    private double average;
    private double min;
    private double max;
    private double stdDev;

    /**
     * Returns the average
     * @return the average
     */
    public double getAverage() {
        return average;
    }

    /**
     * Returns the maximum value
     * @return max value
     */
    public double getMax() {
        return max;
    }

    /**
     * Returns the minimum value
     * @return min value
     */
    public double getMin() {
        return min;
    }

    /**
     * Returns the standard deviation
     * @return standard deviation
     */
    public double getStdDev() {
        return stdDev;
    }

    /**
     * Changes the statistical value in one step, so that notification is done once
     * with a complete new snapshot.
     *
     * @param average the new average
     * @param min the new minum
     * @param max the new maximum
     * @param stdDev the new standard deviation
     */
    public void setStatistics(double average, double min, double max, double stdDev) {
        if (average != this.average || min != this.min || max != this.max || stdDev != this.stdDev) {
            this.average = average;
            this.min = min;
            this.max = max;
            this.stdDev = stdDev;
        }
    }

    void setTo(DoubleStatistics newValue) {
        setStatistics(newValue.average, newValue.min, newValue.max, newValue.stdDev);
    }

    @Override
    public int hashCode() {
        return new Double(average).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DoubleStatistics) {
            DoubleStatistics other = (DoubleStatistics) obj;
            return average == other.average && min == other.min && max == other.max && stdDev == other.stdDev;
        }

        return false;
    }

}
