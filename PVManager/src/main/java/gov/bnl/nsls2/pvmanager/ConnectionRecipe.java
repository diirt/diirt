/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.Set;

/**
 * Parameters needed to setup the connection notification on the PV.
 *
 * @author carcassi
 */
class ConnectionRecipe {
    /**
     * The PV to setup the notification on.
     */
    PV<?> pv;

    /**
     * The list of channels name that the PV depends on.
     */
    Set<String> channelNames;
}
