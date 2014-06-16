/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import org.epics.util.stats.Ranges;

/**
 * TODO: finalize names
 *
 * @author carcassi
 */
public class AxisRanges {
    
    private AxisRanges() {
    }
    
    public static AxisRange absolute(final double min, final double max) {
        final Range absoluteRange = RangeUtil.range(min, max);
        return new Absolute(absoluteRange);
    }
    
    private static class Absolute implements AxisRange {
        
        private final AxisRange axisRange = this;
        private final Range absoluteRange;

        private Absolute(Range absoluteRange) {
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
        
    }
    
    public static AxisRange data() {
        return DATA;
    }
    
    private static Data DATA = new Data();
    
    private static class Data implements AxisRange {
        
        private final AxisRange axisRange = this;

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
        
    }
    
    public static AxisRange integrated() {
        return INTEGRATED;
    }
    
    private static Integrated INTEGRATED = new Integrated();
    
    private static class Integrated implements AxisRange {

        private final AxisRange axisRange = this;

        @Override
        public AxisRangeInstance createInstance() {
            return new AxisRangeInstance() {

                private Range aggregatedRange;

                @Override
                public Range axisRange(Range dataRange, Range displayRange) {
                    aggregatedRange = RangeUtil.aggregateRange(dataRange, aggregatedRange);
                    if (Ranges.overlap(aggregatedRange, dataRange) < 0.8) {
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
    }
    
    public static AxisRange display() {
        return DISPLAY;
    }
    
    private static Display DISPLAY = new Display();
    
    private static class Display implements AxisRange {
            
        private final AxisRange axisRange = this;

        @Override
        public AxisRangeInstance createInstance() {
            return new AxisRangeInstance() {
                
                private Range previousDataRange;

                @Override
                public Range axisRange(Range dataRange, Range displayRange) {
                    if (Ranges.isValid(displayRange)) {
                        return displayRange;
                    }
                }

                @Override
                public AxisRange getAxisRange() {
                    return axisRange;
                }
            };
        }
    }
}
