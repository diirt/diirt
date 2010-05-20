/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.epics;

import gov.bnl.nsls2.pvmanager.TimeStamp;

/**
 *
 * @author carcassi
 */
public interface DbrTime {
    TimeStamp getTimeStamp();
    void setTimeStamp(TimeStamp timeStamp);
}
