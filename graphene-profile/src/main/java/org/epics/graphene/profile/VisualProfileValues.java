/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import org.epics.graphene.profile.impl.*;

public final class VisualProfileValues {
    
    /**
     * Package location for <code>ProfileGraph2D</code> subclasses.
     */
    public static final String PROFILE_PATH = "org.epics.graphene.profile.impl";
    
    /**
     * Java class names of all <code>ProfileGraph2D</code> subclasses.
     */
    public static final String[] SUPPORTED_PROFILERS = {"AreaGraph2D",
                                                        "BubbleGraph2D",
                                                        "Histogram1D",
                                                        "IntensityGraph2D",
                                                        "LineGraph2D",
                                                        "MultiYAxisGraph2D",
                                                        "MultilineGraph2D",
                                                        "NLineGraphs2D",
                                                        "ScatterGraph2D",
                                                        "SparklineGraph2D"
                                                       };    
    
    public static final String FRAME_TITLE = "Visual Profiler";
        
    public static ProfileGraph2D factory(String strClass){
        switch (strClass) {
            case "AreaGraph2D":
                return new ProfileAreaGraph2D();
            case "BubbleGraph2D":
                return new ProfileBubbleGraph2D();
            case "Histogram1D":
                return new ProfileHistogram1D();
            case "IntensityGraph2D":
                return new ProfileIntensityGraph2D();
            case "LineGraph2D":
                return new ProfileLineGraph2D();
            case "MultiYAxisGraph2D":
                return new ProfileMultiYAxisGraph2D();
            case "MultilineGraph2D":
                return new ProfileMultilineGraph2D();
            case "NLineGraphs2D":
                return new ProfileNLineGraphs2D();
            case "ScatterGraph2D":
                return new ProfileScatterGraph2D();
            case "SparklineGraph2D":
                return new ProfileSparklineGraph2D();
        }
        
        return null;    
    }
}
