/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

import java.util.List;

/**
 *
 * @author carcassi
 */
public interface DbrTimeEnum extends DbrStsEnum {
    List<String> getLabels();
    void setLabels(List<String> labels);
}
