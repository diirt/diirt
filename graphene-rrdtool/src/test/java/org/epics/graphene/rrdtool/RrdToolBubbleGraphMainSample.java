/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.rrdtool;

/**
 *
 * @author carcassi
 */
public class RrdToolBubbleGraphMainSample {
    public static void main(String[] args) throws Exception {
        RrdToolBubbleGraphMain.main(new String[] {"/media/sf_Shared/aru41_setpoint_5602.rrd:Temp1:AVERAGE",
            "/media/sf_Shared/aru41_setpoint_5602.rrd:Temp2:AVERAGE",
            "/media/sf_Shared/aru41_setpoint_5602.rrd:Temp3:AVERAGE",
            "/media/sf_Shared/dc2-5-17_cpu_system_54823.rrd:cpu_system:AVERAGE",
            "-s", "20121201000000", "-e", "20121215000000", "-o", "test"});
    }
}
