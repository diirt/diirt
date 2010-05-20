/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

import gov.bnl.pvmanager.TimeStamp;

/**
 * Time information.
 *
 * @author carcassi
 */
public interface Time {
    TimeStamp getTimeStamp();
    void setTimeStamp(TimeStamp timeStamp);
}
