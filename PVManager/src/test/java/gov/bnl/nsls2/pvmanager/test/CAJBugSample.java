/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.test;

import gov.aps.jca.Channel;
import gov.aps.jca.Context;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;
import gov.aps.jca.dbr.DBRType;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;

/**
 *
 * @author carcassi
 */
public class CAJBugSample {
    public static void main(String[] args) throws Exception {
        JCALibrary jca = JCALibrary.getInstance();
        Context ctxt = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
        Channel ch = ctxt.createChannel("pvk01");
        //ctxt.pendIO(1.0);
        //Thread.sleep(1000);
        ch.addMonitor(DBRType.DOUBLE, 1, Monitor.VALUE, new MonitorListener() {

            @Override
            public void monitorChanged(MonitorEvent ev) {
                System.out.println("1 -> " + ev.getDBR().getValue());
            }
        });
        //Thread.sleep(1000);

        Channel ch2 = ctxt.createChannel("pvk01");
        ch2.addMonitor(DBRType.DOUBLE, 1, Monitor.VALUE, new MonitorListener() {

            @Override
            public void monitorChanged(MonitorEvent ev) {
                System.out.println("2 -> " + ev.getDBR().getValue());
            }
        });
        Thread.sleep(5000);
        ch.destroy();
        ch2.destroy();
        ctxt.destroy();
    }
}
