/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

import java.util.List;

/**
 * Multi channel array.
 *
 * @author carcassi
 */
public interface MultiChannel<T> {
    List<T> getValues();
    void setValues(List<T> value);
    ConnectionStatus getConnectionStatus();
    void setConnectionStatus(ConnectionStatus connectionStatus);
    String getConnectionStatusMessage();
    void setConnectionStatusMesage(String connectionStatusMessage);

}
