/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

/**
 *
 * @author asbarber
 */
public class ProfileAll {
    public static void main(String[] args){
        ProfileHistogram1D.main(null);
        ProfileIntensityGraph2D.main(null);
        ProfileLineGraph2D.main(null);
        //ProfileLockHistogram1D.main(null);      //Does not save statistics
        //ProfileParallelHistogram1D.main(null);  //Does not save statistics
        ProfileScatterGraph2D.main(null);
        ProfileSparklineGraph2D.main(null);
    }
}
