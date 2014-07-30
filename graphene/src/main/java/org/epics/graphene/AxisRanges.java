/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.util.Objects;
import org.epics.util.stats.Range;
import org.epics.util.stats.Ranges;

/**
 * Standard implementation for the logic to calculate the data range to
 * be displayed in a graph.
 *
 * @author carcassi
 */
public class AxisRanges {
    
    private AxisRanges() {
    }
    
    public static AxisRange fixed(final double min, final double max) {
        final Range fixedRange = Ranges.range(min, max);
        return new Fixed(fixedRange);
    }
    
    public static class Fixed implements AxisRange {
        
        private final AxisRange axisRange = this;
        private final Range absoluteRange;

        private Fixed(Range absoluteRange) {
            this.absoluteRange = absoluteRange;
        }

        @Override
        public AxisRangeInstance createInstance() {
            return new AxisRangeInstance() {

                @Override
                public Range axisRange(Range dataRange, Range displayRange) {
                        return absoluteRange;
                }

                @Override
                public AxisRange getAxisRange() {
                    return axisRange;
                }
            };
        }

        @Override
        public String toString() {
            return "fixed(" + absoluteRange.getMinimum() + ", " + absoluteRange.getMaximum() + ")";
        }

        public Range getAbsoluteRange() {
            return absoluteRange;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Fixed) {
                return Ranges.equals(getAbsoluteRange(), ((Fixed) obj).getAbsoluteRange());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.absoluteRange);
            return hash;
        }
        
    }
    
    public static AxisRange data() {
        return DATA;
    }
    
    private static Data DATA = new Data();
    
    public static class Data implements AxisRange {
        
        private final AxisRange axisRange = this;

        private Data() {
        }

        @Override
        public AxisRangeInstance createInstance() {
            return new AxisRangeInstance() {

                @Override
                public Range axisRange(Range dataRange, Range displayRange) {
                    return dataRange;
                }

                @Override
                public AxisRange getAxisRange() {
                    return axisRange;
                }
            };
        }

        @Override
        public String toString() {
            return "data";
        }
    }
    
    public static AxisRange auto() {
        return AUTO;
    }
    
    public static AxisRange auto(double minUsage) {
        return new Auto(minUsage);
    }
    
    private static final Auto AUTO = new Auto(0.8);
    
    public static class Auto implements AxisRange {

        private final AxisRange axisRange = this;
        private final double minUsage;

        private Auto(double minUsage) {
            this.minUsage = minUsage;
        }

        @Override
        public AxisRangeInstance createInstance() {
            return new AxisRangeInstance() {

                private Range aggregatedRange;

                @Override
                public Range axisRange(Range dataRange, Range displayRange) {
                    aggregatedRange = Ranges.aggregateRange(dataRange, aggregatedRange);
                    if (Ranges.overlap(aggregatedRange, dataRange) < minUsage) {
                        aggregatedRange = dataRange;
                    }
                    return aggregatedRange;
                }

                @Override
                public AxisRange getAxisRange() {
                    return axisRange;
                }
            };
        }

        @Override
        public String toString() {
            return "auto(" + (int) (minUsage * 100) + "%)";
        }

        public double getMinUsage() {
            return minUsage;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Auto) {
                return getMinUsage() == ((Auto) obj).getMinUsage();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.getMinUsage());
            return hash;
        }
        
    }
    
    public static AxisRange suggested() {
        return SUGGESTED;
    }
    
    private static final Suggested SUGGESTED = new Suggested();
    
    public static class Suggested implements AxisRange {
            
        private final AxisRange axisRange = this;

        private Suggested() {
        }

        @Override
        public AxisRangeInstance createInstance() {
            return new AxisRangeInstance() {
                
                private Range previousDataRange;

                @Override
                public Range axisRange(Range dataRange, Range displayRange) {
                    if (Ranges.isValid(displayRange)) {
                        return displayRange;
                    } else if (previousDataRange == null) {
                        previousDataRange = dataRange;
                        return previousDataRange;
                    } else {
                        return previousDataRange;
                    }
                }

                @Override
                public AxisRange getAxisRange() {
                    return axisRange;
                }
            };
        }

        @Override
        public String toString() {
            return "suggested";
        }
    }
}
